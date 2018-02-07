package de.dreierschach.invader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import de.dreierschach.daddel.Daddel;
import de.dreierschach.daddel.gfx.sprite.ImageSprite;
import de.dreierschach.daddel.gfx.text.TextSprite;
import de.dreierschach.daddel.listener.CollisionListener;
import de.dreierschach.daddel.model.EndOfLifeStrategy;
import de.dreierschach.daddel.model.OutsideGridStrategy;
import de.dreierschach.daddel.model.Pos;
import de.dreierschach.daddel.model.SpriteGameLoop;
import javafx.geometry.VPos;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

public class SpaceInvader extends Daddel {

	// ---------- Einstellungen für Rakete --

	private static float raketeRichtung = 0;
	private static float raketeGeschwindigkeit = 15f; // raster / s
	private static float raketeLaserGeschwindigkeit = 30f; // raster / s

	private static long raketeLaserVerzoegerung = 100; // ms
	private long raketeLaserVerbleibendeWartezeit = 0; // ms

	private static long raketeSchutzschirmDauer = 2000;
	private long raketeSchutzschirm = raketeSchutzschirmDauer;

	private static long neueRaketeVerzoegerungDauer = 2000;
	private long neueRaketeVerzoegerung = 0;

	// ---------- Einstellungen für Gegner --

	private float gegnerLaserGeschwindigkeit = 20f; // raster / s

	// ---------- Sprites --

	private ImageSprite raketeSprite;
	private TextSprite punkteAnzeige;
	private TextSprite lebenAnzeige;

	private static int TYP_STERN = 0;
	private static int TYP_SPIELER = 1;
	private static int TYP_FEIND = 2;
	private static int TYP_LASER = 3;
	private static int TYP_GEGNERISCHER_LASER = 4;
	private static int TYP_EXPLOSION = 5;

	// ---------- Spielvariablen --

	private int anzahlFeinde;
	private int punkte = 0;
	private int leben = 5;

	// ---------- Highscore-Liste --

	private List<HighscoreEintrag> highscoreListe = new ArrayList<>();
	private static String SETUP_HIGHSCORE_LISTE = "setup_highscore_liste";

	// ---------- Titel-Bildschirm --

	@Override
	public void initGame() {
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
	}

	public void titel() {
		initHighscore();

		grid(-16, 16, -10, 10);
		erzeugeFrontalScrollendeSterne();

		textParticle("SPACE", 4000, "sans-serif", 3f, Color.YELLOW).pos(new Pos(0, -3.5f))
				.weight(FontWeight.BLACK).size(30f, 7f).endOfLifeStrategy(EndOfLifeStrategy.stop);

		textParticle("I N V A D E R", 4000, "sans-serif", 1.5f, Color.RED).pos(new Pos(0, 2f))
				.weight(FontWeight.BOLD).size(0.01f, 1.5f).endOfLifeStrategy(EndOfLifeStrategy.stop);

		particle(TYP_FEIND, 10000, 3, GFX_UFO_1).rotation(0, 360).pos(new Pos(-7f, 2f))
				.endOfLifeStrategy(EndOfLifeStrategy.restart).alpha(0.5f, 1f);

		particle(TYP_FEIND, 15000, 3, GFX_UFO_2).rotation(360, 0).pos(new Pos(7f, 2f))
				.endOfLifeStrategy(EndOfLifeStrategy.restart).alpha(1f, 0.5f);

		particle(TYP_SPIELER, 0, 3, GFX_ROCKET).pos(new Pos(0, -0.2f));

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
		text("SPACE", "sans-serif", 3f, Color.YELLOW).pos(new Pos(0, -7)).weight(FontWeight.BLACK);
		text("INVADER", "sans-serif", 1.7f, Color.RED).pos(new Pos(0, -4.5f)).weight(FontWeight.BLACK);

		menu().pos(new Pos(0, 0)).color(Color.GREEN, Color.WHITE).weight(FontWeight.BLACK, FontWeight.BLACK)
				.size(1.5f, 1.5f).lineHeight(2).family("sans-serif", "sans-serif")
				.item("new game", (keyCode) -> toLevelIntro()).item("highscore", (keyCode) -> toHighscore())
				.item("setup", (keyCode) -> toSetup()).item("title", (keyCode) -> toTitle()).item("exit", (keyCode) -> toCredits()).create();
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
		text("HIGHSCORE", "sans-serif", 2f, Color.YELLOW).pos(new Pos(0, -8)).weight(FontWeight.BLACK);
		for (int i = 0; i < highscoreListe.size(); i++) {
			HighscoreEintrag eintrag = highscoreListe.get(i);
			text(String.format("%2d. %20s %10d", i + 1, eintrag.name, eintrag.score), "monospaced", 1f, Color.WHITE)
					.pos(new Pos(0, 1.4f * i - 4.5f)).weight(FontWeight.BLACK);
		}

		key(KeyCode.ENTER, (keyCode) -> toMenu());
		key(KeyCode.ESCAPE, (keyCode) -> toCredits());
	}

	// ---------- Gewonnen-Bildschirm --

	// public void gewonnen() {
	// // TODO Auto-generated method stub
	// }

	// ---------- Verloren-Bildschirm --

	public void gameover() {
		grid(-16, 16, -10, 10);

		erzeugeHochscrollendeSterne();

		TextSprite gameoverText = text("GAME OVER", "sans-serif", 3f, Color.RED).pos(new Pos(0, 0))
				.weight(FontWeight.BLACK);

		if (istNeuerHighscore()) {
			HighscoreEintrag eintrag = new HighscoreEintrag("", punkte);
			text("You got a new highscore!!", "sans-serif", 1f, Color.YELLOW).pos(new Pos(0, 3))
					.weight(FontWeight.BLACK);
			text("enter your name: ", "monospaced", 1f, Color.YELLOW).pos(new Pos(0, 7))
					.weight(FontWeight.BLACK).align(TextAlignment.RIGHT, VPos.CENTER);

			TextSprite inputText = text("", "monospaced", 1f, Color.WHITE).pos(new Pos(0, 7))
					.weight(FontWeight.BLACK).align(TextAlignment.LEFT, VPos.CENTER);

			input(20, input -> {
				inputText.text(input.toUpperCase());
				eintrag.name = input.toUpperCase();
			});

			key(KeyCode.ENTER, (keyCode) -> {
				noInput();
				neuerHighscore(eintrag);
				toHighscore();
			});
		} else {
			text("press <ENTER> to continue", "sans-serif", 1f, Color.YELLOW).pos(new Pos(0, 3))
					.weight(FontWeight.BLACK);
			key(KeyCode.ENTER, (keyCode) -> toHighscore());
		}
	}

	// ---------- Abspann-Bildschirm --

	// public void abspann() {
	// exit();
	// // TODO Auto-generated method stub
	// }

	// ---------- Level-Intro-Bildschirm --

	public void levelIntro() {
		grid(-16, 16, -10, 10);
		erzeugeHochscrollendeSterne();

		text(String.format("LEVEL %02d", level()), "sans-serif", 1.5f, Color.YELLOW).pos(new Pos(0, -1))
				.weight(FontWeight.BLACK);

		text("press <ENTER> when ready", "sans-serif", 0.6f, Color.WHITE).pos(new Pos(0, 1))
				.weight(FontWeight.BLACK);

		key(KeyCode.ENTER, (keyCode) -> toLevel());
		key(KeyCode.ESCAPE, (keyCode) -> exit());
	}

	// ---------- Level starten --

	public void starteLevel() {
		// Spielraster setzen

		grid(-16, 16, -10, 10);

		// Sprites erzeugen

		erzeugeHochscrollendeSterne();
		erzeugeRakete(new Pos(0f, 7.5f));
		erzeugeFeinde();

		punkteAnzeige = text("SCORE:     ", "sans-serif", 1.0f, Color.YELLOW).pos(new Pos(-16f, 9.5f))
				.align(TextAlignment.LEFT, VPos.CENTER).weight(FontWeight.BLACK);
		punkteAnzeigen();

		lebenAnzeige = text("LEBEN: ", "sans-serif", 1.0f, Color.YELLOW).pos(new Pos(16f, 9.5f))
				.align(TextAlignment.RIGHT, VPos.CENTER).weight(FontWeight.BLACK);
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
			toGameover();
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
				erzeugeRakete(raketeSprite.effektivePos());
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
		if (!getSetup().contains(SETUP_HIGHSCORE_LISTE)) {
			highscoreListe.clear();
			for (int i = 0; i < 10; i++) {
				highscoreListe.add(new HighscoreEintrag("FREDVOMJUPITER", 10000 - 1000 * i));
			}
			getSetup().set(SETUP_HIGHSCORE_LISTE, highscoreListe);
			setupSave();
		}
		highscoreListe = new ArrayList<>(
				Arrays.asList(getSetup().get(SETUP_HIGHSCORE_LISTE, HighscoreEintrag[].class)));
	}

	// ------------- neuen Highscore eintragen --

	public void neuerHighscore(HighscoreEintrag eintrag) {
		highscoreListe.add(eintrag);
		Collections.sort(highscoreListe);
		highscoreListe.remove(highscoreListe.size() - 1);
		getSetup().set(SETUP_HIGHSCORE_LISTE, highscoreListe);
		setupSave();
	}

	// ------------- ist das ein neuer Highscore? --

	public boolean istNeuerHighscore() {
		return punkte > highscoreListe.get(highscoreListe.size() - 1).score;
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
		particleSwarmBuilder(200, TYP_STERN, GFX_STERN).initialPosRange(new Pos(-16, -10), new Pos(16, 10))
				.sizeRange(0.01f, 0.2f, 4).direction(90).speedRange(1f, 5f)
				.outsideGridStrategy(OutsideGridStrategy.reappear).create();
	}

	// ------------- frontal-scrollende Sterne erzeugen --

	public void erzeugeFrontalScrollendeSterne() {
		particleSwarmBuilder(400, TYP_STERN, GFX_STERN).initialPosRange(new Pos(0, 0), new Pos(0, 0))
				.sizeRange(0.02f, 0.4f, 4).directionRange(0, 359).speedStartRange(0.1f, 5f).speedEndRange(5f, 10f)
				.alphaStart(0f).alphaEnd(1f).lifeSpan(5000).endOfLifeStrategy(EndOfLifeStrategy.ignore)
				.outsideGridStrategy(OutsideGridStrategy.restart).create();
	}

	// ------------- Rakete erzeugen --

	public void erzeugeRakete(Pos pos) {
		raketeSchutzschirm = raketeSchutzschirmDauer;
		raketeSprite = sprite(TYP_SPIELER, 4f, GFX_ROCKET, GFX_ROCKET_SCHIRM).pos(pos)
				.gameLoop(raketeAnimieren).r(1f);
		raketeRichtung = 0;
	}

	// ------------- Rakete animieren

	private SpriteGameLoop raketeAnimieren = (sprite, gesamtZeit, deltaZeit) -> {
		float s = raketeRichtung * strecke(deltaZeit, raketeGeschwindigkeit);
		if (raketeSprite.effektivePos().x() + s < -14.5) {
			stop();
			s = 0;
		}
		if (raketeSprite.effektivePos().x() + s > 14.5) {
			stop();
			s = 0;
		}
		raketeSprite.pos(new Pos(raketeSprite.effektivePos().x() + s, raketeSprite.effektivePos().y()))
				.actualImage(raketeSchutzschirm > 0 ? 1 : 0);
	};

	// ------------- Gegner erzeugen --

	public void erzeugeFeinde() {
		Random random = new Random();
		anzahlFeinde = 0;
		for (int i = -4; i <= 4; i++) {
			for (int j = -3; j <= -1; j++) {
				String enimy;
				double rotation;
				switch (random.nextInt(2)) {
				case 0:
					enimy = GFX_UFO_1;
					rotation = 6 * Math.random() - 3;
					break;
				default:
				case 1:
					enimy = GFX_UFO_2;
					rotation = 3 * Math.random() - 1.5;
					break;
				}

				// Gegner erzeugen und animieren

				sprite(TYP_FEIND, 2.5f, enimy).pos(new Pos(((float) i) * 3f, ((float) j) * 3f))
						.gameLoop((spr, ticks, deltatime) -> {
							spr.rotate(-rotation);
							spr.direction(-spr.rotation());
							spr.move((float) (rotation / 20));
							if (Math.random() < 0.004) {
								gegnerischenLaserAbfeuern(new Pos(spr.effektivePos().x(), spr.effektivePos().y() + 1.5f));
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
		erzeugeRaketeLaser(new Pos(raketeSprite.effektivePos().x() - 0.8f, raketeSprite.effektivePos().y() - 1.5f));
		erzeugeRaketeLaser(new Pos(raketeSprite.effektivePos().x() + 0.8f, raketeSprite.effektivePos().y() - 1.5f));
	}

	public void erzeugeRaketeLaser(Pos pos) {
		particle(TYP_LASER, 0, 1.5f, GFX_LASER).pos(pos).collision(raketeTrefferBehandeln)
				.direction(-90).outsideRasterStrategy(OutsideGridStrategy.kill)
				.speed(raketeLaserGeschwindigkeit, raketeLaserGeschwindigkeit);
		sound(AUDIO_ROCKET_LASER);
	}

	// ------------- gegnerischen Laser abfeuern --

	public void gegnerischenLaserAbfeuern(Pos pos) {
		particle(TYP_GEGNERISCHER_LASER, 0, 1.5f, GFX_LASER_GEGNER).pos(pos)
				.collision(gegnerischeTrefferBehandeln).direction(90)
				.outsideRasterStrategy(OutsideGridStrategy.kill)
				.speed(gegnerLaserGeschwindigkeit, gegnerLaserGeschwindigkeit);
		sound(AUDIO_UFO_LASER, 0.2);
	}

	// ------------- Treffer durch Rakete behandeln --

	private CollisionListener raketeTrefferBehandeln = (laserSprite, getroffenerSprite) -> {
		if (getroffenerSprite.type() == TYP_FEIND) {

			if (getroffenerSprite.alive()) {
				getroffenerSprite.kill();
				anzahlFeinde--;
				punkte += 150;
				punkteAnzeigen();

				particle(TYP_EXPLOSION, 400, 4f, GFX_EXPLOSION).speedAnimation(10f)
						.pos(getroffenerSprite.effektivePos());
				sound(AUDIO_UFO_EXPLOSION, 0.8);
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

				particle(TYP_EXPLOSION, 400, 6f, GFX_EXPLOSION).speedAnimation(10f)
						.pos(getroffenerSprite.effektivePos());
				sound(AUDIO_ROCKET_EXPLOSION, 1.0);
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
