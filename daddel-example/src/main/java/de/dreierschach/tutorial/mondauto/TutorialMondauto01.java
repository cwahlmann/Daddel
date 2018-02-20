package de.dreierschach.tutorial.mondauto;

import de.dreierschach.daddel.Daddel;
import de.dreierschach.daddel.gfx.Gfx;
import de.dreierschach.daddel.gfx.tilemap.Entity;
import de.dreierschach.daddel.gfx.tilemap.TileMap;
import de.dreierschach.daddel.model.MapPos;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;

//Das Spiel erweitert die Spiele-API Daddel
public class TutorialMondauto01 extends Daddel {

	// Hier wird das Programm initialisiert

	private TileMap map;
	private static int BODEN_DUNKEL_ID = ':';
	private static int BODEN_HELL_ID = '.';
	private static int MARSEI_ID = 'o';
	private static int MARSEIERSCHALE_ID = 'c';
	private static int WAND_ID = '#';
	private static int BODEN_TYPE = 0;
	private static int WAND_TYPE = 1;
	private static int MARSEI_TYPE = 2;
	private static int MARSEIERSCHALE_TYPE = 3;
	private static int MARSIPOLINCHEN_TYPE = 4;
	private static int MONDAUTO_TYPE = 5;

	private static final String[][] level = { { //
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
			}, { //
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
			} };

	private Entity mondauto;

	@Override
	public void initGame() {
		background(Color.BLACK);

		grid(-9, 9, -5, 5);

		toLevel(() -> {
			map = tilemap(1);
			map.tile(BODEN_DUNKEL_ID, BODEN_TYPE, Gfx.MOON_KRATER_0000);
			map.tile(BODEN_HELL_ID, BODEN_TYPE, Gfx.MOON_KRATER_1111);
			map.tile(WAND_ID, WAND_TYPE, Gfx.MOON_WAND);
			map.tile(MARSEI_ID, MARSEI_TYPE, Gfx.MOON_MARSEI);
			map.tile(MARSEIERSCHALE_ID, MARSEIERSCHALE_TYPE, Gfx.MOON_MARSEIERSCHALE);
			map.defaultTile(BODEN_HELL_ID);
			map.initMap(level);
			key(KeyCode.ESCAPE, keyCode -> exit());

			mondauto = entity(MONDAUTO_TYPE, 1.5, Gfx.MOON_MONDAUTO_L0, Gfx.MOON_MONDAUTO_L1, Gfx.MOON_MONDAUTO_L2,
					Gfx.MOON_MONDAUTO_L3);
			mondauto.mapPos(new MapPos(5, 5, 1));
			mondauto.animation().imageStart(0).imageEnd(3).speed(2);
			map.focus(mondauto);

			entity(MARSIPOLINCHEN_TYPE, 1.5, Gfx.MOON_MARSIPOLINCHEN).mapPos(new MapPos(7, 6, 1));
		});
	}

	// Standart-Main-Methode, um das Programm zu starten

	public static void main(String[] args) {
		launch(args);
	}
}
