package de.dreierschach.game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import de.dreierschach.engine.JavaGame;
import de.dreierschach.engine.gfx.sprite.ImageSprite;
import de.dreierschach.engine.gfx.sprite.SpriteGameLoop;
import de.dreierschach.engine.gfx.text.TextSprite;
import de.dreierschach.engine.listener.CollisionListener;
import de.dreierschach.engine.model.EndOfLifeStrategy;
import de.dreierschach.engine.model.OutsideGridStrategy;
import de.dreierschach.engine.model.Pos;
import javafx.geometry.VPos;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

public class SpaceInvader extends JavaGame {

	// ---------- Einstellungen für Rocket --

	private static float xwingRichtung = 0;
	private static float xwingGeschwindigkeit = 15f; // raster / s
	private static float xwingLaserGeschwindigkeit = 30f; // raster / s

	private static long rocketLaserVerzoegerung = 100; // ms
	private long rocketLaserVerbleibendeWartezeit = 0; // ms

	private static long rocketSchutzschirmDauer = 2000;
	private long rocketSchutzschirm = rocketSchutzschirmDauer;

	private static long neueRocketVerzoegerungDauer = 2000;
	private long neueRocketVerzoegerung = 0;

	// ---------- Einstellungen für Gegner --

	private float gegnerLaserGeschwindigkeit = 20f; // raster / s

	// ---------- Sprites --

	private ImageSprite rocketSprite;
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
	public void init() {
		initTitleScreen();
		initIntroScreen();
		initMenuScreen();
		initSetupScreen();
		initHighscoreScreen();
		initWinGameScreen();
		initGameOverScreen();
		initCreditsScreen();
		initLevelIntroScreen();
		initLevelScreen();
	}

	public void initTitleScreen() {
		toTitle(() -> {
			initHighscore();

			grid(-16, 16, -10, 10);
			erzeugeFrontalScrollendeSterne();

			textParticle("SPACE", 4000, "sans-serif", 3f, Color.YELLOW).relativePos(new Pos(0, -2.5f))
					.weight(FontWeight.BLACK).size(20f, 3.5f).endOfLifeStrategy(EndOfLifeStrategy.stop);

			textParticle("I N V A D E R", 4000, "sans-serif", 1.5f, Color.RED).relativePos(new Pos(0, 1.5f))
					.weight(FontWeight.BOLD).size(0.01f, 1.3f).endOfLifeStrategy(EndOfLifeStrategy.stop);

			particle(TYP_FEIND, 10000, 3, GFX_UFO_1).rotation(0, 360).relativePos(new Pos(-7f, 1.5f))
					.endOfLifeStrategy(EndOfLifeStrategy.restart).alpha(0.5f, 1f);

			particle(TYP_FEIND, 15000, 3, GFX_UFO_2).rotation(360, 0).relativePos(new Pos(7f, 1.5f))
					.endOfLifeStrategy(EndOfLifeStrategy.restart).alpha(1f, 0.5f);

			particle(TYP_SPIELER, 0, 3, GFX_ROCKET).relativePos(new Pos(0, -0.2f));

			key(KeyCode.ENTER, () -> toIntro());
			key(KeyCode.ESCAPE, () -> toCredits());
		});
	}

	// ---------- Intro-Bildschirm --

	public void initIntroScreen() {
		toIntro(() -> toMenu());
	}

	// ---------- Menu-Bildschirm --

	public void initMenuScreen() {
		toMenu(() -> {
			initGame();
			grid(-16, 16, -10, 10);
			erzeugeHochscrollendeSterne();
			text("SPACE", "sans-serif", 3f, Color.YELLOW).relativePos(new Pos(0, -7)).weight(FontWeight.BLACK);
			text("INVADER", "sans-serif", 1.7f, Color.RED).relativePos(new Pos(0, -4.5f)).weight(FontWeight.BLACK);

			menu().pos(new Pos(0, 0)).color(Color.GREEN, Color.WHITE).weight(FontWeight.BLACK, FontWeight.BLACK)
					.size(1.5f, 1.5f).lineHeight(2).family("sans-serif", "sans-serif")
					.item("new game", () -> toLevelIntro()).item("highscore", () -> toHighscore())
					.item("setup", () -> toSetup()).item("title", () -> toTitle()).item("exit", () -> toCredits())
					.create();
			key(KeyCode.ESCAPE, () -> toCredits());
		});
	}

	// ---------- Setup-Bildschirm --

	public void initSetupScreen() {
		toSetup(() -> toMenu());
	}

	// ---------- Highscore-Bildschirm --

	public void initHighscoreScreen() {
		toHighscore(() -> {
			grid(-16, 16, -10, 10);
			erzeugeHochscrollendeSterne();
			text("HIGHSCORE", "sans-serif", 2f, Color.YELLOW).relativePos(new Pos(0, -8)).weight(FontWeight.BLACK);
			for (int i = 0; i < highscoreListe.size(); i++) {
				HighscoreEintrag eintrag = highscoreListe.get(i);
				text(String.format("%2d. %20s %10d", i + 1, eintrag.name, eintrag.score), "monospaced", 1f, Color.WHITE)
						.relativePos(new Pos(0, 1.4f * i - 4.5f)).weight(FontWeight.BLACK);
			}

			key(KeyCode.ENTER, () -> toMenu());
			key(KeyCode.ESCAPE, () -> toCredits());
		});
	}

	// ---------- Gewonnen-Bildschirm --

	public void initWinGameScreen() {
		toWinGame(() -> {
		});

		// TODO Auto-generated method stub
	}

	// ---------- Verloren-Bildschirm --

	public void initGameOverScreen() {
		toGameover(() -> {
			grid(-16, 16, -10, 10);

			erzeugeHochscrollendeSterne();

			TextSprite gameoverText = text("GAME OVER", "sans-serif", 3f, Color.RED).relativePos(new Pos(0, 0))
					.weight(FontWeight.BLACK);

			if (istNeuerHighscore()) {
				HighscoreEintrag eintrag = new HighscoreEintrag("", punkte);
				text("You got a new highscore!!", "sans-serif", 1f, Color.YELLOW).relativePos(new Pos(0, 3))
						.weight(FontWeight.BLACK);
				text("enter your name: ", "monospaced", 1f, Color.YELLOW).relativePos(new Pos(0, 7))
						.weight(FontWeight.BLACK).align(TextAlignment.RIGHT, VPos.CENTER);

				TextSprite inputText = text("", "monospaced", 1f, Color.WHITE).relativePos(new Pos(0, 7))
						.weight(FontWeight.BLACK).align(TextAlignment.LEFT, VPos.CENTER);

				input(20, input -> {
					inputText.text(input.toUpperCase());
					eintrag.name = input.toUpperCase();
				});

				key(KeyCode.ENTER, () -> {
					noInput();
					neuerHighscore(eintrag);
					toHighscore();
				});
			} else {
				text("press <ENTER> to continue", "sans-serif", 1f, Color.YELLOW).relativePos(new Pos(0, 3))
						.weight(FontWeight.BLACK);
				key(KeyCode.ENTER, () -> toHighscore());
			}
		});
	}

	// ---------- Abspann-Bildschirm --

	public void initCreditsScreen() {
		toCredits(() -> exit());
		// TODO Auto-generated method stub
	}

	// ---------- Level-Intro-Bildschirm --

	public void initLevelIntroScreen() {
		toLevelIntro(() -> {
			grid(-16, 16, -10, 10);
			erzeugeHochscrollendeSterne();

			text(String.format("LEVEL %02d", level()), "sans-serif", 1.5f, Color.YELLOW).relativePos(new Pos(0, -1))
					.weight(FontWeight.BLACK);

			text("press <ENTER> when ready", "sans-serif", 0.6f, Color.WHITE).relativePos(new Pos(0, 1))
					.weight(FontWeight.BLACK);

			key(KeyCode.ENTER, () -> toLevel());
			key(KeyCode.ESCAPE, () -> exit());
		});
	}

	// ---------- Level starten --

	public void initLevelScreen() {
		toLevel(() -> {
			// Spielraster setzen

			grid(-16, 16, -10, 10);

			// Sprites erzeugen

			erzeugeHochscrollendeSterne();
			erzeugeRocket(new Pos(0f, 7.5f));
			erzeugeFeinde();

			punkteAnzeige = text("SCORE:     ", "sans-serif", 1.0f, Color.YELLOW).relativePos(new Pos(-16f, 9.5f))
					.align(TextAlignment.LEFT, VPos.CENTER).weight(FontWeight.BLACK);
			punkteAnzeigen();

			lebenAnzeige = text("LEBEN: ", "sans-serif", 1.0f, Color.YELLOW).relativePos(new Pos(16f, 9.5f))
					.align(TextAlignment.RIGHT, VPos.CENTER).weight(FontWeight.BLACK);
			lebenAnzeigen();

			// Auf Tasten reagieren

			key(KeyCode.LEFT, () -> left());
			key(KeyCode.RIGHT, () -> right());
			key(KeyCode.DOWN, () -> stop());
			key(KeyCode.SPACE, () -> rocketLaserAbfeuern());
			key(KeyCode.ESCAPE, () -> toCredits());
		});
	}

	// ---------- Spielschleife --

	@Override
	public void gameLoop(long gesamtZeit, long deltaZeit) {
		if (leben == 0) {
			toGameover();
			return;
		}
		rocketLaserVerbleibendeWartezeit -= deltaZeit;
		if (rocketSchutzschirm > 0) {
			rocketSchutzschirm -= deltaZeit;
		}
		if (!rocketSprite.alive()) {
			if (neueRocketVerzoegerung > 0) {
				neueRocketVerzoegerung -= deltaZeit;
			} else {
				erzeugeRocket(rocketSprite.pos());
			}
		}

		if (anzahlFeinde <= 0) {
			nextLevel();
		}
	}

	// ============= Hilfs-Methoden =======================

	// ------------- neues Spiel initialisieren --

	private void initGame() {
		punkte = 0;
		leben = 5;
		level(1);
	}

	// ------------- Highscore laden bzw. neu erzeugen --

	public void initHighscore() {
		if (!getSetup().contains(SETUP_HIGHSCORE_LISTE)) {
			highscoreListe.clear();
			for (int i = 0; i < 10; i++) {
				highscoreListe.add(new HighscoreEintrag("CHRISTIAN", 10000 - 1000 * i));
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

	// ------------- Xwing-Raumschiff erzeugen --

	public void erzeugeRocket(Pos pos) {
		rocketSchutzschirm = rocketSchutzschirmDauer;
		rocketSprite = sprite(TYP_SPIELER, 4f, GFX_ROCKET, GFX_ROCKET_SCHIRM).relativePos(pos).gameLoop(xwingAnimieren);
		xwingRichtung = 0;
	}

	// ------------- XWingRaumschiff animieren

	private SpriteGameLoop xwingAnimieren = (sprite, gesamtZeit, deltaZeit) -> {
		float s = xwingRichtung * strecke(deltaZeit, xwingGeschwindigkeit);
		if (rocketSprite.pos().x() + s < -14.5) {
			stop();
			s = 0;
		}
		if (rocketSprite.pos().x() + s > 14.5) {
			stop();
			s = 0;
		}
		rocketSprite.relativePos(new Pos(rocketSprite.pos().x() + s, rocketSprite.pos().y()))
				.actualImage(rocketSchutzschirm > 0 ? 1 : 0);
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

				sprite(TYP_FEIND, 2.5f, enimy).relativePos(new Pos(((float) i) * 3f, ((float) j) * 3f))
						.gameLoop((spr, ticks, deltatime) -> {
							spr.rotate(-rotation);
							spr.direction(-spr.rotation());
							spr.move((float) (rotation / 20));
							if (Math.random() < 0.004) {
								gegnerischenLaserAbfeuern(new Pos(spr.pos().x(), spr.pos().y() + 1.5f));
							}
						});
				anzahlFeinde++;
			}
		}
	}

	// ------------- Rocket-Raumschiff-Steuerung --

	public void left() {
		xwingRichtung = -1;
	}

	public void right() {
		xwingRichtung = 1;
	}

	public void stop() {
		xwingRichtung = 0;
	}

	// ------------- Rocket-Laser abfeuern --

	public void rocketLaserAbfeuern() {
		if (rocketSchutzschirm > 0 || rocketLaserVerbleibendeWartezeit > 0 || !rocketSprite.alive()) {
			return;
		}
		rocketLaserVerbleibendeWartezeit = rocketLaserVerzoegerung;
		erzeugeRocketLaser(new Pos(rocketSprite.pos().x() - 0.8f, rocketSprite.pos().y() - 1.5f));
		erzeugeRocketLaser(new Pos(rocketSprite.pos().x() + 0.8f, rocketSprite.pos().y() - 1.5f));
	}

	public void erzeugeRocketLaser(Pos pos) {
		particle(TYP_LASER, 0, 1.5f, GFX_LASER).relativePos(pos).collisionListener(xwingTrefferBehandeln).direction(-90)
				.outsideRasterStrategy(OutsideGridStrategy.kill)
				.speed(xwingLaserGeschwindigkeit, xwingLaserGeschwindigkeit);
		sound(AUDIO_ROCKET_LASER);
	}

	// ------------- gegnerischen Laser abfeuern --

	public void gegnerischenLaserAbfeuern(Pos pos) {
		particle(TYP_GEGNERISCHER_LASER, 0, 1.5f, GFX_LASER_GEGNER).relativePos(pos)
				.collisionListener(gegnerischeTrefferBehandeln).direction(90)
				.outsideRasterStrategy(OutsideGridStrategy.kill)
				.speed(gegnerLaserGeschwindigkeit, gegnerLaserGeschwindigkeit);
		sound(AUDIO_UFO_LASER, 0.2);
	}

	// ------------- Treffer durch Rocket behandeln --

	private CollisionListener xwingTrefferBehandeln = (laserSprite, getroffenerSprite) -> {
		if (getroffenerSprite.type() == TYP_FEIND) {

			if (getroffenerSprite.alive()) {
				getroffenerSprite.kill();
				anzahlFeinde--;
				punkte += 150;
				punkteAnzeigen();

				particle(TYP_EXPLOSION, 400, 4f, GFX_EXPLOSION).speedAnimation(10f)
						.relativePos(getroffenerSprite.pos());
				sound(AUDIO_UFO_EXPLOSION, 0.8);
			}
			laserSprite.kill();
		}
	};

	// ------------- gegnerische Treffer behandeln --

	private CollisionListener gegnerischeTrefferBehandeln = (laserSprite, getroffenerSprite) -> {
		if (getroffenerSprite.type() == TYP_SPIELER) {

			if (getroffenerSprite.alive() && rocketSchutzschirm <= 0) {
				getroffenerSprite.kill();
				leben--;
				lebenAnzeigen();

				particle(TYP_EXPLOSION, 400, 6f, GFX_EXPLOSION).speedAnimation(10f)
						.relativePos(getroffenerSprite.pos());
				sound(AUDIO_ROCKET_EXPLOSION, 1.0);
				neueRocketVerzoegerung = neueRocketVerzoegerungDauer;
			}
			laserSprite.kill();
		}
	};

	// ===================== main-Methode, um das Programm zu starten =========

	public static void main(String[] args) {
		launch(args);
	}
}
