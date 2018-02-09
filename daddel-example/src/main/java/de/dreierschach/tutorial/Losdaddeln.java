package de.dreierschach.tutorial;

import de.dreierschach.daddel.Daddel;
import de.dreierschach.daddel.gfx.sprite.Particle;
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
			sprite(1, 4f, GFX_ROCKET);
			Particle pacman = particle(1, 8000, 1.5f, GFX_PAC_PACMAN_L0, GFX_PAC_PACMAN_L1, GFX_PAC_PACMAN_L2,
					GFX_PAC_PACMAN_L3).pos(-11, 3).rotation(180).speed(4f).endOfLife(PARTICLE_RESTART)
							.outsideGrid(PARTICLE_IGNORE);
			text(". . . los-daddeln", "sans-serif", 1, Color.WHITE).parent(pacman).pos(-1.5f, 0).align(ALIGN_RIGHT,
					VALIGN_CENTER);
			key(KeyCode.ESCAPE, keyCode -> exit());
		});
	}

	// Standart-Main-Methode, um das Programm zu starten

	public static void main(String[] args) {
		launch(args);
	}
}
