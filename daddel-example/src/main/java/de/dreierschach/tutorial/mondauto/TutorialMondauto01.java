package de.dreierschach.tutorial.mondauto;

import de.dreierschach.daddel.Daddel;
import de.dreierschach.daddel.gfx.Gfx;
import de.dreierschach.daddel.gfx.tilemap.Entity;
import de.dreierschach.daddel.gfx.tilemap.TileMap;
import de.dreierschach.daddel.model.MapPos;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;

public class TutorialMondauto01 extends Daddel {

	// Initialisieren des Spiels

	@Override
	public void initGame() {
		background(Color.BLACK);
		grid(-9, 9, -5, 5);

		// wenn ein Level gestartet werden soll...
		toLevel(() -> starteLevel());
	}

	// Methode, um einen neuen Level zu starten

	private void starteLevel() {
		// <Esc> beendet das Spiel
		key(KeyCode.ESCAPE, keyCode -> exit());
	}

	// Standart-Main-Methode, um das Programm zu starten

	public static void main(String[] args) {
		launch(args);
	}
}
