package de.dreierschach.tutorial.mondauto;

import de.dreierschach.daddel.Daddel;
import de.dreierschach.daddel.gfx.Gfx;
import de.dreierschach.daddel.gfx.tilemap.TileMap;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;

public class TutorialMondauto02Karte extends Daddel {

	// Variablen

	private TileMap map;

	// Konstanten

	private static int BODEN_DUNKEL_ID = ':';
	private static int BODEN_HELL_ID = '.';
	private static int MARSEI_ID = 'o';
	private static int MARSEIERSCHALE_ID = 'c';
	private static int WAND_ID = '#';
	private static int QUECKSILBER_ID = '~';
	private static int BODEN_TYPE = 0;
	private static int WAND_TYPE = 1;
	private static int MARSEI_TYPE = 2;
	private static int MARSEIERSCHALE_TYPE = 3;
	private static int MARSIPOLINCHEN_TYPE = 4;
	private static int MONDAUTO_TYPE = 5;
	private static int QUECKSILBER_TYPE = 6;

	private static final String[] LEVEL_HINTERGRUND = { //
			"....:::........", //
			"...::::........", //
			"....:::::......", //
			".::::::::::::..", //
			".:::::::::::::.", //
			"......:::::::..", //
			".......:::::...", //
			"........::.....", //
			"...............", //
			"...............", //
	};

	private static final String[] LEVEL_1 = { //
			"###############", //
			"# o  o        #", //
			"#o########### #", //
			"# # c    #    #", //
			"# ###### # ####", //
			"#     #  #c   #", //
			"# ###    #### #", //
			"# # # #o #  # #", //
			"# #   #  #    #", //
			"###############", //
	};

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
		erzeugeKarte();

		// <Esc> beendet das Spiel
		key(KeyCode.ESCAPE, keyCode -> exit());
	}

	private void erzeugeKarte() {
		// Karte erzeugen (Größe einer Kachel: 1)
		map = tilemap(1)//
				.tile(BODEN_DUNKEL_ID, BODEN_TYPE, Gfx.MOON_KRATER_0000) //
				.tile(BODEN_HELL_ID, BODEN_TYPE, Gfx.MOON_KRATER_1111) //
				.tile(WAND_ID, WAND_TYPE, Gfx.MOON_WAND) //
				.tile(MARSEI_ID, MARSEI_TYPE, Gfx.MOON_MARSEI) //
				.tile(MARSEIERSCHALE_ID, MARSEIERSCHALE_TYPE, Gfx.MOON_MARSEIERSCHALE) //
				.tile(QUECKSILBER_ID, QUECKSILBER_TYPE, Gfx.MOON_QUECKSILBER) //
				.defaultTile(BODEN_HELL_ID) //
				.initMap(LEVEL_HINTERGRUND, LEVEL_1);
	}

	// Standart-Main-Methode, um das Programm zu starten

	public static void main(String[] args) {
		launch(args);
	}
}
