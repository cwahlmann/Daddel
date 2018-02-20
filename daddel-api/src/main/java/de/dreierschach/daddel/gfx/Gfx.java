package de.dreierschach.daddel.gfx;

public interface Gfx {
	// Sprites

	final static String ROCKET = "gfx/invader/rocket.png";
	final static String ROCKET_SCHIRM = "gfx/invader/rocket-schirm.png";
	final static String LASER = "gfx/invader/laser-red.png";
	final static String LASER_GEGNER = "gfx/invader/laser-green.png";

	final static String UFO_1 = "gfx/invader/ufo-1.png";
	final static String UFO_2 = "gfx/invader/ufo-2.png";

	final static String STERN = "gfx/invader/stern.png";
	final static String SONNE = "gfx/invader/sun.png";
	final static String ERDE = "gfx/invader/earth.png";
	final static String MOND = "gfx/invader/moon.png";
	final static String SATURN = "gfx/invader/saturn.png";

	final static String[] EXPLOSION = { "gfx/invader/explosion-1.png", "gfx/invader/explosion-2.png",
			"gfx/invader/explosion-3.png", "gfx/invader/explosion-4.png" };

	// Pacman

	final static String PAC_BASHFUL_INKY_D0 = "gfx/pacman/bashful-inky_D0.png";
	final static String PAC_BASHFUL_INKY_D1 = "gfx/pacman/bashful-inky_D1.png";
	final static String PAC_BASHFUL_INKY_L0 = "gfx/pacman/bashful-inky_L0.png";
	final static String PAC_BASHFUL_INKY_L1 = "gfx/pacman/bashful-inky_L1.png";
	final static String PAC_BASHFUL_INKY_R0 = "gfx/pacman/bashful-inky_R0.png";
	final static String PAC_BASHFUL_INKY_R1 = "gfx/pacman/bashful-inky_R1.png";
	final static String PAC_BASHFUL_INKY_U0 = "gfx/pacman/bashful-inky_U0.png";
	final static String PAC_BASHFUL_INKY_U1 = "gfx/pacman/bashful-inky_U1.png";
	final static String PAC_DEADEYES_D = "gfx/pacman/deadeyes_D.png";
	final static String PAC_DEADEYES_L = "gfx/pacman/deadeyes_L.png";
	final static String PAC_DEADEYES_R = "gfx/pacman/deadeyes_R.png";
	final static String PAC_DEADEYES_U = "gfx/pacman/deadeyes_U.png";
	final static String PAC_FRUIT_ANANAS = "gfx/pacman/fruit_ananas.png";
	final static String PAC_FRUIT_APPLE = "gfx/pacman/fruit_apple.png";
	final static String PAC_FRUIT_CHERRY = "gfx/pacman/fruit_cherry.png";
	final static String PAC_FRUIT_ORANGE = "gfx/pacman/fruit_orange.png";
	final static String PAC_FRUIT_STRAWBERRY = "gfx/pacman/fruit_strawberry.png";
	final static String PAC_GHOST_BLUE0 = "gfx/pacman/ghost_blue0.png";
	final static String PAC_GHOST_BLUE1 = "gfx/pacman/ghost_blue1.png";
	final static String PAC_GHOST_WHITE0 = "gfx/pacman/ghost_white0.png";
	final static String PAC_GHOST_WHITE1 = "gfx/pacman/ghost_white1.png";
	final static String PAC_PACMAN_DIE_0 = "gfx/pacman/pacman_die_0.png";
	final static String PAC_PACMAN_DIE_1 = "gfx/pacman/pacman_die_1.png";
	final static String PAC_PACMAN_DIE_2 = "gfx/pacman/pacman_die_2.png";
	final static String PAC_PACMAN_DIE_3 = "gfx/pacman/pacman_die_3.png";
	final static String PAC_PACMAN_DIE_4 = "gfx/pacman/pacman_die_4.png";
	final static String PAC_PACMAN_L0 = "gfx/pacman/pacman_L0.png";
	final static String PAC_PACMAN_L1 = "gfx/pacman/pacman_L1.png";
	final static String PAC_PACMAN_L2 = "gfx/pacman/pacman_L2.png";
	final static String PAC_PACMAN_L3 = "gfx/pacman/pacman_L3.png";
	final static String PAC_PACMAN_LIFE = "gfx/pacman/pacman_life.png";
	final static String PAC_PILLE_GROSS = "gfx/pacman/pille_gross.png";
	final static String PAC_PILLE_KLEIN = "gfx/pacman/pille_klein.png";
	final static String PAC_POKEY_CLYDE_D0 = "gfx/pacman/pokey-clyde_D0.png";
	final static String PAC_POKEY_CLYDE_D1 = "gfx/pacman/pokey-clyde_D1.png";
	final static String PAC_POKEY_CLYDE_L0 = "gfx/pacman/pokey-clyde_L0.png";
	final static String PAC_POKEY_CLYDE_L1 = "gfx/pacman/pokey-clyde_L1.png";
	final static String PAC_POKEY_CLYDE_R0 = "gfx/pacman/pokey-clyde_R0.png";
	final static String PAC_POKEY_CLYDE_R1 = "gfx/pacman/pokey-clyde_R1.png";
	final static String PAC_POKEY_CLYDE_U0 = "gfx/pacman/pokey-clyde_U0.png";
	final static String PAC_POKEY_CLYDE_U1 = "gfx/pacman/pokey-clyde_U1.png";
	final static String PAC_SCORE_200 = "gfx/pacman/score_200.png";
	final static String PAC_SHADOW_BLINKY_D0 = "gfx/pacman/shadow-blinky_D0.png";
	final static String PAC_SHADOW_BLINKY_D1 = "gfx/pacman/shadow-blinky_D1.png";
	final static String PAC_SHADOW_BLINKY_L0 = "gfx/pacman/shadow-blinky_L0.png";
	final static String PAC_SHADOW_BLINKY_L1 = "gfx/pacman/shadow-blinky_L1.png";
	final static String PAC_SHADOW_BLINKY_R0 = "gfx/pacman/shadow-blinky_R0.png";
	final static String PAC_SHADOW_BLINKY_R1 = "gfx/pacman/shadow-blinky_R1.png";
	final static String PAC_SHADOW_BLINKY_U0 = "gfx/pacman/shadow-blinky_U0.png";
	final static String PAC_SHADOW_BLINKY_U1 = "gfx/pacman/shadow-blinky_U1.png";
	final static String PAC_SPEEDY_PINKY_D0 = "gfx/pacman/speedy-pinky_D0.png";
	final static String PAC_SPEEDY_PINKY_D1 = "gfx/pacman/speedy-pinky_D1.png";
	final static String PAC_SPEEDY_PINKY_L0 = "gfx/pacman/speedy-pinky_L0.png";
	final static String PAC_SPEEDY_PINKY_L1 = "gfx/pacman/speedy-pinky_L1.png";
	final static String PAC_SPEEDY_PINKY_R0 = "gfx/pacman/speedy-pinky_R0.png";
	final static String PAC_SPEEDY_PINKY_R1 = "gfx/pacman/speedy-pinky_R1.png";
	final static String PAC_SPEEDY_PINKY_U0 = "gfx/pacman/speedy-pinky_U0.png";
	final static String PAC_SPEEDY_PINKY_U1 = "gfx/pacman/speedy-pinky_U1.png";
	final static String PAC_WALL_GATE = "gfx/pacman/wall_gate.png";
	final static String PAC_WALL_H = "gfx/pacman/wall_h.png";
	final static String PAC_WALL_V = "gfx/pacman/wall_v.png";
	final static String PAC_WALL_LO = "gfx/pacman/wall_lo.png";
	final static String PAC_WALL_RO = "gfx/pacman/wall_ro.png";
	final static String PAC_WALL_LU = "gfx/pacman/wall_lu.png";
	final static String PAC_WALL_RU = "gfx/pacman/wall_ru.png";
	final static String PAC_WALL_ROUND_LO = "gfx/pacman/wall_round_lo.png";
	final static String PAC_WALL_ROUND_LU = "gfx/pacman/wall_round_lu.png";
	final static String PAC_WALL_ROUND_RO = "gfx/pacman/wall_round_ro.png";
	final static String PAC_WALL_ROUND_RU = "gfx/pacman/wall_round_ru.png";

	// Mondauto

	static final String MOON_FELS = "gfx/mondauto/bfels.png";
	static final String MOON_GOLD = "gfx/mondauto/bgold.png";
	static final String MOON_KOMPASS_DEF = "gfx/mondauto/bkompassDef.png";
	static final String MOON_KOMPASS_N = "gfx/mondauto/bkompassN.png";
	static final String MOON_KOMPASS_NO = "gfx/mondauto/bkompassNO.png";
	static final String MOON_KOMPASS_NW = "gfx/mondauto/bkompassNW.png";
	static final String MOON_KOMPASS_O = "gfx/mondauto/bkompassO.png";
	static final String MOON_KOMPASS_S = "gfx/mondauto/bkompassS.png";
	static final String MOON_KOMPASS_SO = "gfx/mondauto/bkompassSO.png";
	static final String MOON_KOMPASS_SW = "gfx/mondauto/bkompassSW.png";
	static final String MOON_KOMPASS_W = "gfx/mondauto/bkompassW.png";
	static final String MOON_KRATER_0000 = "gfx/mondauto/bkrater0000.png";
	static final String MOON_KRATER_0001 = "gfx/mondauto/bkrater0001.png";
	static final String MOON_KRATER_0010 = "gfx/mondauto/bkrater0010.png";
	static final String MOON_KRATER_0011 = "gfx/mondauto/bkrater0011.png";
	static final String MOON_KRATER_0100 = "gfx/mondauto/bkrater0100.png";
	static final String MOON_KRATER_0101 = "gfx/mondauto/bkrater0101.png";
	static final String MOON_KRATER_0110 = "gfx/mondauto/bkrater0110.png";
	static final String MOON_KRATER_0111 = "gfx/mondauto/bkrater0111.png";
	static final String MOON_KRATER_1000 = "gfx/mondauto/bkrater1000.png";
	static final String MOON_KRATER_1001 = "gfx/mondauto/bkrater1001.png";
	static final String MOON_KRATER_1010 = "gfx/mondauto/bkrater1010.png";
	static final String MOON_KRATER_1011 = "gfx/mondauto/bkrater1011.png";
	static final String MOON_KRATER_1100 = "gfx/mondauto/bkrater1100.png";
	static final String MOON_KRATER_1101 = "gfx/mondauto/bkrater1101.png";
	static final String MOON_KRATER_1110 = "gfx/mondauto/bkrater1110.png";
	static final String MOON_KRATER_1111 = "gfx/mondauto/bkrater1111.png";
	static final String MOON_MARSEI = "gfx/mondauto/bmarsei.png";
	static final String MOON_MARSEIERSCHALE = "gfx/mondauto/bmarseierschale.png";
	static final String MOON_MARSIPOLINCHEN = "gfx/mondauto/bmarsipolinchen.png";
	static final String MOON_MONDAUTO_L0 = "gfx/mondauto/bmondauto_l0.png";
	static final String MOON_MONDAUTO_L1 = "gfx/mondauto/bmondauto_l1.png";
	static final String MOON_MONDAUTO_L2 = "gfx/mondauto/bmondauto_l2.png";
	static final String MOON_MONDAUTO_L3 = "gfx/mondauto/bmondauto_l3.png";
	static final String MOON_MONDAUTO_R0 = "gfx/mondauto/bmondauto_r0.png";
	static final String MOON_MONDAUTO_R1 = "gfx/mondauto/bmondauto_r1.png";
	static final String MOON_MONDAUTO_R2 = "gfx/mondauto/bmondauto_r2.png";
	static final String MOON_MONDAUTO_R3 = "gfx/mondauto/bmondauto_r3.png";
	static final String MOON_QUECKSILBER = "gfx/mondauto/bquecksilber.png";
	static final String MOON_SCHLUPFLOCH = "gfx/mondauto/bschlupfloch.png";
	static final String MOON_STATION = "gfx/mondauto/bstation.png";
	static final String MOON_WAND = "gfx/mondauto/bwall.png";

	// Texturen

	static final String TEXTURE_RASEN = "gfx/texture/rasen.png";
}
