package de.dreierschach.tutorial;

import de.dreierschach.daddel.Daddel;
import de.dreierschach.daddel.gfx.sprite.ImageSprite;
import de.dreierschach.daddel.model.Pos;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;

//Das Spiel erweitert die Spiele-API Daddel
public class Tutorial02RaketeSteuerung extends Daddel {

	// Sprites können einen Typ haben, z.B. einen für Spieler und einen für Gegner
	private final static int TYP_SPIELER = 1;

	// Die Größe der Rakete wird in Spielraster-Punkten angegeben
	private final static float RAKETE_GROESSE = 2f;

	// Die Geschwindigkeit der Rakete in Rasterpunkten pro Sekunde
	private final static float RAKETE_GESCHWINDIGKEIT = 5f;

	// Startposition der Rakete
	private final static Pos RAKETE_STARTPOS = new Pos(0, 3.5f);

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

		// Bestimme die Hintergrundfarbe
		background(Color.rgb(0, 0, 32));

		// Für jede Phase des Spiels kann eine Methode festgelegt werden. Hier reicht
		// die Phase Level, also das Spielen eines Levels.
		toLevel(() -> startLevel());
	}

	// Hier wird ein Level gestartet
	private void startLevel() {
		erzeugeRakete();
		definiereSteuerung();
	}

	private void erzeugeRakete() {
		// erzeuge die Rakete
		rakete = sprite(TYP_SPIELER, RAKETE_GROESSE, GFX_ROCKET, GFX_ROCKET_SCHIRM) //
				.pos(RAKETE_STARTPOS) //
				// In der Spielschleife der Rakete wird diese bewegt
				.gameLoop((me, totaltime, deltatime) -> {
					// Die Strecke kann mit der vordefinierten Methode strecke() aus delta-Zeit und
					// Geschwindigkeit errechnet werden
					float strecke = strecke(deltatime, RAKETE_GESCHWINDIGKEIT);
					bewegeRakete(strecke);
				});
	}

	private void definiereSteuerung() {
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
	// Sekunde während eines Levels ausgeführt
	@Override
	public void gameLoop(long gesamtZeit, long deltaZeit) {
		// Nichts zu tun
	}

	// Methode, um die Rakete in die richtige Richtung zu bewegen.
	private void bewegeRakete(float strecke) {
		Pos neuePosition = rakete.pos().add(getPosRichtung(raketeRichtung, strecke));
		if (!onGrid(neuePosition, rakete.r())) {
			// Wenn die Rakete aus dem Bildschirm fliegen würde, wird sie gestoppt
			raketeRichtung = Richtung.stop;
		} else {
			// Ansonsten wird die Position verändert
			rakete.pos(neuePosition);
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

	// ===================== Standart-Main-Methode, um das Programm zu starten

	// Diese Methode muss vorhanden sein, damit das Spiel überhaupt gestartet werden
	// kann.
	// Sie ist immer gleich.
	public static void main(String[] args) {
		launch(args);
	}
}
