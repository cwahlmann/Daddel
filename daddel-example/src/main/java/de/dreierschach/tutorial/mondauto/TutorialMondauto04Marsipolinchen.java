package de.dreierschach.tutorial.mondauto;

import de.dreierschach.daddel.Daddel;
import de.dreierschach.daddel.gfx.Gfx;
import de.dreierschach.daddel.gfx.tilemap.Entity;
import de.dreierschach.daddel.gfx.tilemap.TileMap;
import de.dreierschach.daddel.model.MapPos;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;

public class TutorialMondauto04Marsipolinchen extends Daddel {

	// Variablen

	private TileMap map;
	private Entity mondauto;
	private Entity marsipolinchen;

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

		erzeugeMarsipolinchen(new MapPos(7, 6, 1));
		erzeugeMondauto(new MapPos(5, 5, 1));

		// der Kartenausschnitt soll dem Mondauto folgen
		map.focus(mondauto);

		// <Esc> beendet das Spiel
		key(KeyCode.ESCAPE, keyCode -> exit());

		// mit <F1> und <F2> kann die Kamera zwischen Mondauto und Marsipolinchen
		// umgeschaltet werden
		key(KeyCode.F1, keyCode -> map.focus(mondauto));
		key(KeyCode.F2, keyCode -> map.focus(marsipolinchen));
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

	private void erzeugeMarsipolinchen(MapPos pos) {
		marsipolinchen = entity(MARSIPOLINCHEN_TYPE, 1.5, Gfx.MOON_MARSIPOLINCHEN).mapPos(pos).moveSpeed(4);
	}

	private void erzeugeMondauto(MapPos pos) {
		mondauto = entity(MONDAUTO_TYPE, 1.5, Gfx.MOON_MONDAUTO_L0, Gfx.MOON_MONDAUTO_L1, Gfx.MOON_MONDAUTO_L2,
				Gfx.MOON_MONDAUTO_L3, Gfx.MOON_MONDAUTO_R0, Gfx.MOON_MONDAUTO_R1, Gfx.MOON_MONDAUTO_R2,
				Gfx.MOON_MONDAUTO_R3) //
						.moveSpeed(5).mapPos(pos);
		mondauto.animation().imageStart(0).imageEnd(3).speed(2);
	}

	// Standart-Main-Methode, um das Programm zu starten

	public static void main(String[] args) {
		launch(args);
	}
}
