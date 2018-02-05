package de.dreierschach.tutorial;

import de.dreierschach.daddel.Daddel;
import de.dreierschach.daddel.gfx.sprite.ImageSprite;
import javafx.scene.input.KeyCode;

public class TutorialRakete extends Daddel {

	private final static int TYP_SPIELER = 1;
	private final static float RAKETE_GROESSE = 1f;
	
	private ImageSprite rakete;
	
	@Override
	public void initGame() {
		grid(-10, 10, -5, 5);
		toLevel(() -> startLevel());
	}

	private void startLevel() {
		rakete = sprite(TYP_SPIELER, RAKETE_GROESSE, GFX_ROCKET, GFX_ROCKET_SCHIRM);
		key(KeyCode.ESCAPE, keyCode -> exit());
	}
	
	@Override
	public void gameLoop(long gesamtZeit, long deltaZeit) {
		// Nichts zu tun
	}

	// ===================== Standart-Main-Methode, um das Programm zu starten =========

	public static void main(String[] args) {
		launch(args);
	}
}
