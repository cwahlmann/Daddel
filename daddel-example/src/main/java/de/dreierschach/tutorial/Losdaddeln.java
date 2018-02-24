package de.dreierschach.tutorial;

import de.dreierschach.daddel.Daddel;
import de.dreierschach.daddel.gfx.Gfx;
import de.dreierschach.daddel.gfx.sprite.ImageSprite;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;

//Das Spiel erweitert die Spiele-API Daddel
public class Losdaddeln extends Daddel {

	// Hier wird das Programm initialisiert
	
	@Override
	public void initGame() {
		grid(-10, 10, -5, 5);
		background(Color.rgb(0, 64, 255));
		toLevel(() -> {
			sprite(1, 4, Gfx.ROCKET).direction(90).move(2);
			
			key(KeyCode.ESCAPE, keyCode -> exit());
		});
	}

	// Standart-Main-Methode, um das Programm zu starten

	public static void main(String[] args) {
		launch(args);
	}
}
