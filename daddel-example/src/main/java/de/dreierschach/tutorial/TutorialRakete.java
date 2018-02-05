package de.dreierschach.tutorial;

import de.dreierschach.daddel.Daddel;
import de.dreierschach.daddel.gfx.sprite.ImageSprite;
import javafx.scene.input.KeyCode;

// Das Spiel erweitert die Spiele-API Daddel
public class TutorialRakete extends Daddel {

	// Sprites können einen Typ haben, z.B. einen für Spieler und einen für Gegner
	private final static int TYP_SPIELER = 1;

	// Die Größe der Rakete wird in Spielraster-Punkten angegeben
	private final static float RAKETE_GROESSE = 1f;

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
		// Der Sprite Rakete wird erzeugt und angezeigt
		rakete = sprite(TYP_SPIELER, RAKETE_GROESSE, GFX_ROCKET, GFX_ROCKET_SCHIRM);

		// Wenn die Taste ESC gedrückt wird, wird das Programm beendet
		key(KeyCode.ESCAPE, keyCode -> exit());
	}

	// Dies ist die sogenannte Spielschleife. Sie wird während eines rund 50 mal pro
	// Sekunde während eines Levels ausgeführt
	@Override
	public void gameLoop(long gesamtZeit, long deltaZeit) {
		// Nichts zu tun
	}

	// ===================== Standart-Main-Methode, um das Programm zu starten

	// Diese Methode muss vorhanden sein, damit das Spiel überhaupt gestartet werden kann.
	// Sie ist immer gleich.
	public static void main(String[] args) {
		launch(args);
	}
}
