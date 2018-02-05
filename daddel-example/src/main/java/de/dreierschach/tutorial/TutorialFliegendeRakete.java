package de.dreierschach.tutorial;

import de.dreierschach.daddel.Daddel;
import de.dreierschach.daddel.gfx.sprite.ImageSprite;
import de.dreierschach.daddel.model.Pos;
import javafx.scene.input.KeyCode;

public class TutorialFliegendeRakete extends Daddel {

	private final static int TYP_SPIELER = 1;
	private final static float RAKETE_GROESSE = 2f;
	private final static float RAKETE_GESCHWINDIGKEIT = 5f;

	enum Richtung {
		stop, links, rechts, hoch, runter
	};

	private Richtung raketeRichtung = Richtung.stop;
	ImageSprite rakete;

	@Override
	public void initGame() {
		grid(-10, 10, -5, 5);
		toLevel(() -> startLevel());
	}

	private void startLevel() {
		rakete = sprite(TYP_SPIELER, RAKETE_GROESSE, GFX_ROCKET, GFX_ROCKET_SCHIRM);
		
		key(KeyCode.LEFT, keyCode -> raketeRichtung = Richtung.links);
		key(KeyCode.RIGHT, keyCode -> raketeRichtung = Richtung.rechts);
		key(KeyCode.UP, keyCode -> raketeRichtung = Richtung.hoch);
		key(KeyCode.DOWN, keyCode -> raketeRichtung = Richtung.runter);
		key(KeyCode.CONTROL, keyCode -> raketeRichtung = Richtung.stop);

		key(KeyCode.ESCAPE, keyCode -> exit());
	}

	@Override
	public void gameLoop(long gesamtZeit, long deltaZeit) {
		bewegeRakete(strecke(deltaZeit, RAKETE_GESCHWINDIGKEIT));
	}

	private void bewegeRakete(float strecke) {
		Pos neuePosition = rakete.relativePos().add(getPosRichtung(raketeRichtung, strecke));
		if (neuePosition.x() < -9 || neuePosition.x() > 9 || neuePosition.y() < -4 || neuePosition.y() > 4) {
			raketeRichtung = Richtung.stop;
		} else {
			rakete.relativePos(neuePosition);
		}
	}

	private Pos getPosRichtung(Richtung richtung, float strecke) {
		switch (raketeRichtung) {
		case links:
			return new Pos(-strecke, 0);
		case rechts:
			return new Pos(strecke, 0);
		case hoch:
			return new Pos(0, -strecke);
		case runter:
			return new Pos(0, strecke);
		case stop:
		default:
			return new Pos(0, 0);
		}
	}

	// ===================== Standart-Main-Methode, um das Programm zu starten
	// =========

	public static void main(String[] args) {
		launch(args);
	}
}
