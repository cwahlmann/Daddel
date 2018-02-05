package de.dreierschach.tutorial;

import java.util.Random;

import de.dreierschach.daddel.Daddel;
import de.dreierschach.daddel.gfx.sprite.ImageSprite;
import de.dreierschach.daddel.gfx.sprite.Sprite;
import de.dreierschach.daddel.model.Pos;
import de.dreierschach.daddel.model.SpriteGameLoop;
import javafx.scene.input.KeyCode;

//Das Spiel erweitert die Spiele-API Daddel
public class TutorialEnimies extends Daddel {

	// Sprites können einen Typ haben, z.B. einen für Spieler und einen für Gegner
	private final static int TYP_SPIELER = 1;
	private final static int TYP_GEGNER = 2;

	// Die Größe der Rakete wird in Spielraster-Punkten angegeben
	private final static float RAKETE_GROESSE = 2f;
	private final static float GEGNER_GROESSE = 2f;

	// Die Geschwindigkeit der Rakete in Rasterpunkten pro Sekunde
	private final static float RAKETE_GESCHWINDIGKEIT = 5f;
	private final static float GEGNER_GESCHWINDIGKEIT = 3f;

	// Ein Enum ist einfach eine Aufzählung. Diese Aufzählung beinhaltet die
	// möglichen Richtungen der Rakete
	enum Richtung {
		stop, links, rechts, hoch, runter
	};

	// In dieser Variablen wird die aktuelle Richtung der Rakete gespeichert
	private Richtung raketeRichtung = Richtung.stop;

	// In dieser Variablen merke ich mir den Sprite Rakete
	private ImageSprite rakete;

	// In dieser Methode wird das Spiel einmal initialisiert.
	@Override
	public void initGame() {
		// Das Grid ist ein Raster, das über den ganzen Bildschirm gelegt wird. Die
		// Kästchen sind quadratisch.
		grid(-10, 10, -5, 5);

		// Für jede Phase des Spiels kann eine Methode festgelegt werden. Hier reicht
		// die Phase Level, also das Spielen eines Levels.
		toLevel(() -> startLevel());
	}

	// Hier wird ein Level gestartet
	private void startLevel() {
		// erzeuge die Rakete
		rakete = sprite(TYP_SPIELER, RAKETE_GROESSE, GFX_ROCKET, GFX_ROCKET_SCHIRM);

		// erzeuge das 4 Ufos
		for (int i = 0; i < 4; i++) {
			// zufällige Position
			Pos pos = new Pos((float) Math.random() * 20f - 10f, (float) Math.random() * 10f - 5f);
			sprite(TYP_GEGNER, GEGNER_GROESSE, GFX_UFO_1) //
					.relativePos(pos) //
					.gameLoop((ufo, totaltime, deltatime) -> bewegeUfo(ufo, deltatime));
		}

		// Je nach Taste wird eine andere Richtung eingeschlagen
		key(KeyCode.LEFT, keyCode -> raketeRichtung = Richtung.links);
		key(KeyCode.RIGHT, keyCode -> raketeRichtung = Richtung.rechts);
		key(KeyCode.UP, keyCode -> raketeRichtung = Richtung.hoch);
		key(KeyCode.DOWN, keyCode -> raketeRichtung = Richtung.runter);
		key(KeyCode.CONTROL, keyCode -> raketeRichtung = Richtung.stop);

		// Wenn die Taste ESC gedrückt wird, wird das Programm beendet
		key(KeyCode.ESCAPE, keyCode -> exit());
	}

	// Dies ist die sogenannte Spielschleife. Sie wird während eines rund 50 mal pro
	// Sekunde während eines Levels ausgeführt @Override
	public void gameLoop(long gesamtZeit, long deltaZeit) {
		// In der Spielschleife wird die Rakete bewegt

		// Die Strecke kann mit der vordefinierten Methode stercke() aus delta-Zeit und
		// Geschwindigkeit errechnet werden
		float strecke = strecke(deltaZeit, RAKETE_GESCHWINDIGKEIT);
		bewegeRakete(strecke);
	}

	// Methode, um die Rakete in die richtige Richtung zu bewegen.
	private void bewegeRakete(float strecke) {
		Pos neuePosition = rakete.relativePos().add(getPosRichtung(raketeRichtung, strecke));
		if (neuePosition.x() < -9 || neuePosition.x() > 9 || neuePosition.y() < -4 || neuePosition.y() > 4) {
			// Wenn die Rakete aus dem Bildschirm fliegen würde, wird sie gestoppt
			raketeRichtung = Richtung.stop;
		} else {
			// Ansonsten wird die Position verändert
			rakete.relativePos(neuePosition);
		}
	}

	// Diese Methode gibt je nach Richtung die richtige Positions-Veränderung zurück
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

	// Dies Methode wird von der Sprite-Spielschleife jedes Ufos aufgerufen
	private void bewegeUfo(Sprite ufo, long deltatime) {
		// berechne die Strecke aus Zeitspanne und Geschwindigkeit
		float strecke = strecke(deltatime, GEGNER_GESCHWINDIGKEIT);

		// berechne die neue Position
		Pos neuePosition = ufo.relativePos().add(new Pos(0, strecke));

		// wenn das Ufo unten ankommt, wird es an den oberen Bildschirmrand gesetzt. Die
		// X-Position ist zufällig.
		if (neuePosition.y() > 6) {
			neuePosition = new Pos((float) Math.random() * 20f - 10f, -6);
		}
		// setze neue Position
		ufo.relativePos(neuePosition);

	}
	// ===================== Standart-Main-Methode, um das Programm zu starten

	// Diese Methode muss vorhanden sein, damit das Spiel überhaupt gestartet werden
	// kann.
	// Sie ist immer gleich.
	public static void main(String[] args) {
		launch(args);
	}
}
