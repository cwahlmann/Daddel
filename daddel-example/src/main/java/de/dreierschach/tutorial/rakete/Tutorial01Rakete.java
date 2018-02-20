package de.dreierschach.tutorial.rakete;

import de.dreierschach.daddel.Daddel;
import de.dreierschach.daddel.gfx.Gfx;
import de.dreierschach.daddel.gfx.sprite.ImageSprite;
import de.dreierschach.daddel.model.Pos;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;

//Das Spiel erweitert die Spiele-API Daddel
public class Tutorial01Rakete extends Daddel {

	// Sprites können einen Typ haben, z.B. einen für Spieler und einen für Gegner
	private final static int TYP_SPIELER = 1;

	// Die Größe der Rakete wird in Spielraster-Punkten angegeben
	private final static float RAKETE_GROESSE = 2f;

	// Startposition der Rakete
	private final static Pos RAKETE_STARTPOS = new Pos(0, 3.5f);

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
		rakete = sprite(TYP_SPIELER, RAKETE_GROESSE, Gfx.ROCKET, Gfx.ROCKET_SCHIRM) //
				.pos(RAKETE_STARTPOS);
	}

	private void definiereSteuerung() {
		// Wenn die Taste ESC gedrückt wird, wird das Programm beendet
		key(KeyCode.ESCAPE, keyCode -> exit());
	}

	// ===================== Standart-Main-Methode, um das Programm zu starten

	// Diese Methode muss vorhanden sein, damit das Spiel überhaupt gestartet werden
	// kann.
	// Sie ist immer gleich.
	public static void main(String[] args) {
		launch(args);
	}
}
