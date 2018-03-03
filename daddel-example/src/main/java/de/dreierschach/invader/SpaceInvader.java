package de.dreierschach.invader;

import java.rmi.dgc.Lease;
import java.util.Random;

import de.dreierschach.daddel.Daddel;
import de.dreierschach.daddel.audio.Audio;
import de.dreierschach.daddel.gfx.Gfx;
import de.dreierschach.daddel.gfx.roll.Roll;
import de.dreierschach.daddel.gfx.sprite.ImageSprite;
import de.dreierschach.daddel.gfx.sprite.Sprite;
import de.dreierschach.daddel.gfx.text.TextSprite;
import de.dreierschach.daddel.listener.CollisionListener;
import de.dreierschach.daddel.model.Highscore;
import de.dreierschach.daddel.model.Pos;
import de.dreierschach.daddel.model.SpriteGameLoop;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.FontWeight;

public class SpaceInvader extends Daddel {

	// ---------- Einstellungen für Rakete --

	private static double raketeRichtung = 0;
	private static double raketeGeschwindigkeit = 15f; // raster / s
	private static double raketeLaserGeschwindigkeit = 30f; // raster / s

	private static long raketeLaserVerzoegerung = 100; // ms
	private long raketeLaserVerbleibendeWartezeit = 0; // ms

	private static long raketeSchutzschirmDauer = 2000;
	private long raketeSchutzschirm = raketeSchutzschirmDauer;

	private static long neueRaketeVerzoegerungDauer = 2000;
	private long neueRaketeVerzoegerung = 0;

	// ---------- Einstellungen für Gegner --

	private double gegnerLaserGeschwindigkeit = 20f; // raster / s

	// ---------- Sprites --

	private ImageSprite raketeSprite;
	private TextSprite punkteAnzeige;
	private TextSprite lebenAnzeige;

	private static int TYP_STERN = 0;
	private static int TYP_SPIELER = 1;
	private static int TYP_FLOTTE = 2;
	private static int TYP_FEIND = 3;
	private static int TYP_LASER = 4;
	private static int TYP_GEGNERISCHER_LASER = 5;
	private static int TYP_EXPLOSION = 6;

	// ---------- Spielvariablen --

	private int anzahlFeinde;
	private int punkte = 0;
	private int leben = 5;

	// ---------- Highscore --

	private Highscore highscore = new Highscore();
	
	// ---------- Level Builder --
	
	private SpaceInvaderLevelBuilder levelBuilder;
	
	// ---------- Titel-Bildschirm --

	@Override
	public void initGame() {
		levelBuilder = new SpaceInvaderLevelBuilder(this);
		
		toTitle(() -> titel());
		// toIntro(() -> intro());
		toMenu(() -> hauptmenu());
		// toSetup(() -> einstellungen());
		toHighscore(() -> highscore());
		// toWinGame(() -> gewonnen());
		toGameOver(() -> gameover());
		// toCredits(() -> abspann());
		toLevelIntro(() -> levelIntro());
		toLevel(() -> starteLevel());
		toCredits(() -> abspann());
	}

	public void titel() {
		initHighscore();

		grid(-16, 16, -10, 10);
		erzeugeFrontalScrollendeSterne();

		textParticle("I N V A D E R", 4000, "sans-serif", 1.5f, Color.RED).pos(0, 2f).weight(FontWeight.BOLD)
				.size(0.01f, 1.5f).endOfLifeStrategy(PARTICLE_STOP);

		textParticle("SPACE", 4000, "sans-serif", 3f, Color.YELLOW).pos(0, -3.5f).weight(FontWeight.BLACK).size(30f, 7f)
		.endOfLifeStrategy(PARTICLE_STOP);

		particle(TYP_FEIND, 10000, 3, Gfx.UFO_1).rotation(0, 360).pos(-7f, 2f).endOfLife(PARTICLE_RESTART).alpha(0.5f,
				1f);

		particle(TYP_FEIND, 15000, 3, Gfx.UFO_2).rotation(360, 0).pos(7f, 2f).endOfLife(PARTICLE_RESTART).alpha(1f,
				0.5f);

		particle(TYP_SPIELER, 0, 3, Gfx.ROCKET).pos(0, -0.2f);

		key(KeyCode.ENTER, (keyCode) -> toIntro());
		key(KeyCode.ESCAPE, (keyCode) -> toCredits());
	}

	// ---------- Intro-Bildschirm --

	// public void intro() {
	// toMenu();
	// }

	// ---------- Menu-Bildschirm --

	public void hauptmenu() {
		initLevel();
		grid(-16, 16, -10, 10);
		erzeugeHochscrollendeSterne();
		text("SPACE", "sans-serif", 3f, Color.YELLOW).pos(0, -7).weight(FontWeight.BLACK);
		text("INVADER", "sans-serif", 1.7f, Color.RED).pos(0, -4.5f).weight(FontWeight.BLACK);

		menu().pos(0, 0).color(Color.GREEN, Color.WHITE).weight(FontWeight.BLACK, FontWeight.BLACK).size(1.5f, 1.5f)
				.lineHeight(2).family("sans-serif", "sans-serif").item("new game", (keyCode) -> toLevelIntro())
				.item("highscore", (keyCode) -> toHighscore()).item("setup", (keyCode) -> toSetup())
				.item("title", (keyCode) -> toTitle()).item("exit", (keyCode) -> toCredits()).create();
		key(KeyCode.ESCAPE, (keyCode) -> toCredits());
	}

	// ---------- Setup-Bildschirm --
	//
	// public void einstellungen() {
	// toMenu();
	// }

	// ---------- Highscore-Bildschirm --

	public void highscore() {
		grid(-16, 16, -10, 10);
		erzeugeHochscrollendeSterne();
		text("HIGHSCORE", "sans-serif", 2f, Color.YELLOW).pos(0, -8).weight(FontWeight.BLACK);
		text(highscore.toString(), "monospaced", 1f, Color.WHITE).pos(0, -4.5f).weight(FontWeight.BLACK)
				.align(ALIGN_CENTER, VALIGN_TOP);

		key(KeyCode.ENTER, (keyCode) -> toMenu());
		key(KeyCode.ESCAPE, (keyCode) -> toCredits());
	}

	// ---------- Gewonnen-Bildschirm --

	// public void gewonnen() {
	// }

	// ---------- Verloren-Bildschirm --

	public void gameover() {
		grid(-16, 16, -10, 10);

		erzeugeHochscrollendeSterne();

		text("GAME OVER", "sans-serif", 3f, Color.RED).pos(0, 0).weight(FontWeight.BLACK);

		if (highscore.isNewHighscore(punkte)) {
			text("You got a new highscore!!", "sans-serif", 1f, Color.YELLOW).pos(0, 3).weight(FontWeight.BLACK);
			text("enter your name: ", "monospaced", 1f, Color.YELLOW).pos(0, 7).weight(FontWeight.BLACK)
					.align(ALIGN_RIGHT, VALIGN_CENTER);

			TextSprite inputText = text("_", "monospaced", 1f, Color.WHITE).pos(0, 7).weight(FontWeight.BLACK)
					.align(ALIGN_LEFT, VALIGN_CENTER);

			input(20, input -> {
				inputText.text(input.toUpperCase() + "_");
			});

			key(KeyCode.ENTER, (keyCode) -> {
				noInput();
				neuerHighscore(input().toUpperCase().trim(), punkte);
				toHighscore();
			});
		} else {
			text("press <ENTER> to continue", "sans-serif", 1f, Color.YELLOW).pos(0, 3).weight(FontWeight.BLACK);
			key(KeyCode.ENTER, (keyCode) -> toHighscore());
		}
	}

	// ---------- Abspann-Bildschirm --

	public void abspann() {
		erzeugeSchraegScrollendeSterne();
		erzeugeSaturn();
		Roll roll = roll().speed(4);
		roll.text("SPACE INVADER\n").size(2.5).family("sans-serif").weight(FontWeight.EXTRA_BOLD).color(Color.YELLOW);
		roll.sprite(3, Gfx.ROCKET);
		roll.text("\nEin Spiel auf die guten alten Zeiten!\n\n\n").size(1).color(Color.ORANGE);
		;
		roll.text("* Grafik *").size(1.5).color(Color.WHITE);
		;
		roll.text("\n\n" //
				+ "Ufos - firestorm200 (openclipart.org)\n\n" //
				+ "Rakete - rygle (openclipart.org)\n\n" //
				+ "Explosionen - dominiquechappard (openclipart.org)\n\n" //
				+ "Planet Erde - barretr (openclipart.org)\n\n" //
				+ "Mond - rg1024 (openclipart.org)\n\n" //
				+ "Stern, Laserstrahlen, Schutzschirm - Christian Wahlmann\n\n\n").size(1).color(Color.ORANGE);
		roll.text("* Sound Effekte *").size(1.5).color(Color.WHITE);
		;
		roll.text("\n\n" //
				+ "mit bestem Dank von https://www.zapsplat.com\n\n\n").size(1).color(Color.ORANGE);
		roll.sprite(2, Gfx.STERN);
		roll.text("\n\n\nerstellt mit\n\n").size(1.5).color(Color.WHITE);
		roll.text("DADDEL").size(4).color(Color.RED);
		roll.text("\n\ndie easy-going 2D-game-API mit JavaFX\n\n\n\n\n").size(1).color(Color.RED);
		roll.text("(c) 2018 Christian Wahlmann").size(1.25).color(Color.ORANGE);
		roll.text("\nhttp://www.github.com/cwahlmann/daddel").size(1).color(Color.ORANGE);

		roll.onFinished(r -> {
			sound(Audio.UFO_EXPLOSION);
			particle(0, 1000, 30, Gfx.EXPLOSION).speedAnimation(4).onDeath(p -> {
				killallSprites();
				timer(4000, () -> exit());
			});
			timer(200, () -> sound(Audio.ROCKET_EXPLOSION));
			timer(400, () -> sound(Audio.UFO_EXPLOSION));
		});
		key(KeyCode.ESCAPE, (keyCode) -> exit());
	}

	// ---------- Level-Intro-Bildschirm --

	public void levelIntro() {
		grid(-16, 16, -10, 10);
		erzeugeHochscrollendeSterne();

		text(String.format("LEVEL %02d", level()), "sans-serif", 1.5f, Color.YELLOW).pos(0, -1)
				.weight(FontWeight.BLACK);

		text("press <ENTER> when ready", "sans-serif", 0.6f, Color.WHITE).pos(0, 1).weight(FontWeight.BLACK);

		key(KeyCode.ENTER, (keyCode) -> toLevel());
		key(KeyCode.ESCAPE, (keyCode) -> exit());
	}

	// ---------- Level starten --

	public void starteLevel() {
		// Spielraster setzen

		grid(-16, 16, -10, 10);

		// Sprites erzeugen
		
//		levelBuilder.erzeugeLevel();

		erzeugeHochscrollendeSterne();
		erzeugeErdeUndMond();
		erzeugeRakete(0f, 7.5f);
		erzeugeFeinde();

		punkteAnzeige = text("SCORE:     ", "sans-serif", 1.0f, Color.YELLOW).pos(-16f, 9.5f)
				.align(ALIGN_LEFT, VALIGN_CENTER).weight(FontWeight.BLACK);
		punkteAnzeigen();

		lebenAnzeige = text("LEBEN: ", "sans-serif", 1.0f, Color.YELLOW).pos(16f, 9.5f)
				.align(ALIGN_RIGHT, VALIGN_CENTER).weight(FontWeight.BLACK);
		lebenAnzeigen();

		// Auf Tasten reagieren

		key(KeyCode.LEFT, (keyCode) -> left());
		key(KeyCode.RIGHT, (keyCode) -> right());
		key(KeyCode.DOWN, (keyCode) -> stop());
		key(KeyCode.SPACE, (keyCode) -> raketeLaserAbfeuern());
		key(KeyCode.ESCAPE, (keyCode) -> toCredits());
	}

	// ---------- Spielschleife --

	@Override
	public void gameLoop(long gesamtZeit, long deltaZeit) {
		if (leben == 0) {
			toGameOver();
			return;
		}
		raketeLaserVerbleibendeWartezeit -= deltaZeit;
		if (raketeSchutzschirm > 0) {
			raketeSchutzschirm -= deltaZeit;
		}
		if (!raketeSprite.alive()) {
			if (neueRaketeVerzoegerung > 0) {
				neueRaketeVerzoegerung -= deltaZeit;
			} else {
				erzeugeRakete(raketeSprite.pos().x(), raketeSprite.pos().y());
			}
		}

		if (anzahlFeinde <= 0) {
			nextLevel();
		}
	}

	// ============= Hilfs-Methoden =======================

	// ------------- neues Spiel initialisieren --

	private void initLevel() {
		punkte = 0;
		leben = 5;
		level(1);
	}

	// ------------- Highscore laden bzw. neu erzeugen --

	public void initHighscore() {
		if (!getSetup().contains(Highscore.SETUP_HIGHSCORE)) {
			highscore.init();
			getSetup().set(Highscore.SETUP_HIGHSCORE, highscore);
			setupSave();
		}
		highscore = getSetup().get(Highscore.SETUP_HIGHSCORE, Highscore.class);
	}

	// ------------- neuen Highscore eintragen --

	public void neuerHighscore(String name, int score) {
		highscore.insert(name, score);
		getSetup().set(Highscore.SETUP_HIGHSCORE, highscore);
		setupSave();
	}

	// ------------- Punkte anzeigen --

	public void punkteAnzeigen() {
		this.punkteAnzeige.text(String.format("SCORE: %010d", punkte));
	}

	// ------------- Leben anzeigen --

	public void lebenAnzeigen() {
		this.lebenAnzeige.text(String.format("LEBEN: %02d", leben));
	}

	// ------------- hoch-scrollende Sterne erzeugen --

	public void erzeugeHochscrollendeSterne() {
		particleSwarmBuilder(200, TYP_STERN, Gfx.STERN).initialPosRange(new Pos(-16, -10), new Pos(16, 10))
				.sizeRange(0.01f, 0.2f, 4).direction(90).speedRange(1f, 5f).outsideGrid(PARTICLE_REAPPEAR).create();
	}

	// ------------- schräg-scrollende Sterne erzeugen --

	public void erzeugeSchraegScrollendeSterne() {
		particleSwarmBuilder(200, TYP_STERN, Gfx.STERN).initialPosRange(new Pos(-16, -10), new Pos(16, 10))
				.sizeRange(0.01f, 0.2f, 4).direction(30).speedRange(1f, 5f).outsideGrid(PARTICLE_REAPPEAR).create();
	}

	// ------------- Erde und Mond erzeugen --

	public void erzeugeErdeUndMond() {
		Sprite erde = sprite(TYP_STERN, 7, Gfx.ERDE).pos(0, -2);
		particle(TYP_STERN, 0, 2, Gfx.MOND).parent(erde).gameLoop((me, gesamtZeit, deltaZeit) -> {
			Pos pos = circlePosition(gesamtZeit, 20000, new Pos(-10, -6), new Pos(10, 6));
			me.pos(pos);
		});
	}

	// ------------- Saturn erzeugen --

	public void erzeugeSaturn() {
		sprite(TYP_STERN, 1, 10, Gfx.SATURN).pos(0, -2);
	}

	// ------------- frontal-scrollende Sterne erzeugen --

	public void erzeugeFrontalScrollendeSterne() {
		particleSwarmBuilder(400, TYP_STERN, Gfx.STERN).initialPosRange(new Pos(0, 0), new Pos(0, 0))
				.sizeRange(0.02f, 0.4f, 4).directionRange(0, 359).speedStartRange(0.1f, 5f).speedEndRange(5f, 10f)
				.alphaStart(0f).alphaEnd(1f).lifeSpan(5000).endOfLife(PARTICLE_IGNORE).outsideGrid(PARTICLE_RESTART)
				.create();
	}

	// ------------- Rakete erzeugen --

	public void erzeugeRakete(double x, double y) {
		raketeSchutzschirm = raketeSchutzschirmDauer;
		raketeSprite = sprite(TYP_SPIELER, 4f, Gfx.ROCKET, Gfx.ROCKET_SCHIRM).pos(x, y).gameLoop(raketeAnimieren).r(1f);
		raketeRichtung = 0;
	}

	// ------------- Rakete animieren

	private SpriteGameLoop raketeAnimieren = (sprite, gesamtZeit, deltaZeit) -> {
		double s = raketeRichtung * strecke(deltaZeit, raketeGeschwindigkeit);
		if (raketeSprite.effektivePos().x() + s < -14.5) {
			stop();
			s = 0;
		}
		if (raketeSprite.effektivePos().x() + s > 14.5) {
			stop();
			s = 0;
		}
		raketeSprite.pos(raketeSprite.effektivePos().x() + s, raketeSprite.effektivePos().y())
				.actualImage(raketeSchutzschirm > 0 ? 1 : 0);
	};

	// ------------- Gegner erzeugen --

	private SpriteGameLoop flotteKreisen = (me, total, delta) -> {
		me.pos(circlePosition(total + 5000, 10000, new Pos(-10, -7), new Pos(10, 1)));
	};

	private SpriteGameLoop flotteIntervallKreisen = (me, total, delta) -> {
		int phase = (int) ((total / 10000) % 4);
		switch (phase) {
		case 0:
			me.pos(circlePosition(total + 5000, 10000, new Pos(-10, -4), new Pos(10, 4)));
			break;
		case 1:
			break;
		case 2:
			me.pos(circlePosition(1000 - total, 2000, new Pos(-6, -2), new Pos(6, 2)));
			break;
		case 3:
			break;
		default:
		}
	};

	private SpriteGameLoop ufoKreisen = (me, total, delta) -> {
		double strecke = strecke(delta, 6);
		me.direction(me.direction() + 5);
		me.move(strecke);
	};

	private void erzeugeFeinde() {
		switch (level()) {
		case 1:
		default:
			erzeugeFeindeLevel1();
		}
	}

	private void erzeugeFeindeLevel1() {
		anzahlFeinde = 0;
		Sprite parent = invisibleSprite(TYP_FLOTTE, 0.5).pos(new Pos(0, -2));
		erzeugeFlotte(new int[] { 9, 9, 9 }, flotteIntervallKreisen, parent);
	}

	private void erzeugeFlotte(int[] ufos, SpriteGameLoop steuerung, Sprite parent) {
		Sprite flotte = invisibleSprite(TYP_FLOTTE, 0.5).parent(parent).gameLoop(steuerung);
		Random random = new Random();
		for (int zeile = 0; zeile < ufos.length; zeile++) {
			double y = zeile - ufos.length / 2;
			for (int spalte = 0; spalte < ufos[zeile]; spalte++) {
				double x = (double) spalte - (double) (ufos[zeile] - 1) / 2d;
				sprite(TYP_FEIND, 2.5f, random.nextBoolean() ? Gfx.UFO_1 : Gfx.UFO_2).pos(x * 3f, y * 3f).parent(flotte)
						.gameLoop((spr, ticks, deltatime) -> {
							if (Math.random() < 0.004) {
								gegnerischenLaserAbfeuern(spr.effektivePos().add(new Pos(0, 1.5f)));
							}
						});
				anzahlFeinde++;
			}
		}
	}

	// ------------- Rakete Steuerung --

	public void left() {
		raketeRichtung = -1;
	}

	public void right() {
		raketeRichtung = 1;
	}

	public void stop() {
		raketeRichtung = 0;
	}

	// ------------- Rakete-Laser abfeuern --

	public void raketeLaserAbfeuern() {
		if (raketeSchutzschirm > 0 || raketeLaserVerbleibendeWartezeit > 0 || !raketeSprite.alive()) {
			return;
		}
		raketeLaserVerbleibendeWartezeit = raketeLaserVerzoegerung;
		erzeugeRaketeLaser(raketeSprite.pos().add(new Pos(-0.8f, -1.5f)));
		erzeugeRaketeLaser(raketeSprite.pos().add(new Pos(+0.8f, -1.5f)));
	}

	public void erzeugeRaketeLaser(Pos pos) {
		particle(TYP_LASER, 0, 1.5f, Gfx.LASER).pos(pos).collision(raketeTrefferBehandeln).direction(-90)
				.outsideGrid(PARTICLE_KILL).speed(raketeLaserGeschwindigkeit, raketeLaserGeschwindigkeit);
		sound(Audio.ROCKET_LASER);
	}

	// ------------- gegnerischen Laser abfeuern --

	public void gegnerischenLaserAbfeuern(Pos pos) {
		particle(TYP_GEGNERISCHER_LASER, 0, 1.5f, Gfx.LASER_GEGNER).pos(pos).collision(gegnerischeTrefferBehandeln)
				.direction(90).outsideGrid(PARTICLE_KILL).speed(gegnerLaserGeschwindigkeit, gegnerLaserGeschwindigkeit);
		sound(Audio.UFO_LASER, 0.2);
	}

	// ------------- Treffer durch Rakete behandeln --

	private CollisionListener raketeTrefferBehandeln = (laserSprite, getroffenerSprite) -> {
		if (getroffenerSprite.type() == TYP_FEIND) {

			if (getroffenerSprite.alive()) {
				getroffenerSprite.kill();
				anzahlFeinde--;
				punkte += 150;
				punkteAnzeigen();

				particle(TYP_EXPLOSION, 400, 4f, Gfx.EXPLOSION).speedAnimation(10f)
						.pos(getroffenerSprite.effektivePos());
				sound(Audio.UFO_EXPLOSION, 0.8);
			}
			laserSprite.kill();
		}
	};

	// ------------- gegnerische Treffer behandeln --

	private CollisionListener gegnerischeTrefferBehandeln = (laserSprite, getroffenerSprite) -> {
		if (getroffenerSprite.type() == TYP_SPIELER) {

			if (getroffenerSprite.alive() && raketeSchutzschirm <= 0) {
				getroffenerSprite.kill();
				leben--;
				lebenAnzeigen();

				particle(TYP_EXPLOSION, 400, 6f, Gfx.EXPLOSION).speedAnimation(10f)
						.pos(getroffenerSprite.effektivePos());
				sound(Audio.ROCKET_EXPLOSION, 1.0);
				neueRaketeVerzoegerung = neueRaketeVerzoegerungDauer;
			}
			laserSprite.kill();
		}
	};

	// ===================== main-Methode, um das Programm zu starten =========

	public static void main(String[] args) {
		launch(args);
	}
}
