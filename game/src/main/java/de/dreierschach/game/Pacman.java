package de.dreierschach.game;

import java.util.Random;

import de.dreierschach.engine.JavaGame;
import de.dreierschach.engine.gfx.tilemap.Entity;
import de.dreierschach.engine.gfx.tilemap.Entity.Dir;
import de.dreierschach.engine.gfx.tilemap.MoveFinishedListener;
import de.dreierschach.engine.gfx.tilemap.TileMap;
import de.dreierschach.engine.model.MapPos;
import de.dreierschach.engine.model.Pos;
import javafx.scene.input.KeyCode;

public class Pacman extends JavaGame {

	@Override
	public void titleScreen() {
		TO_LEVEL.run();
	}

	@Override
	public void menuScreen() {
		// TODO Auto-generated method stub

	}

	@Override
	public void setupScreen() {
		// TODO Auto-generated method stub

	}

	@Override
	public void introScreen() {
		// TODO Auto-generated method stub

	}

	@Override
	public void levelIntroScreen(int level) {
		// TODO Auto-generated method stub

	}

	private static int TYPE_WALL = 1;
	private static int TYPE_GATE = 2;
	private static int TYPE_PACMAN = 3;
	private static int TYPE_PILLE = 4;
	private static int TYPE_PILLE_GROSS = 5;
	private static int TYPE_GHOST = 6;

	private TileMap map;
	private static final int ID_WALL_ROUND_RU = (int) 'a';
	private static final int ID_WALL_ROUND_LU = (int) 'b';
	private static final int ID_WALL_ROUND_RO = (int) 'c';
	private static final int ID_WALL_ROUND_LO = (int) 'd';
	private static final int ID_WALL_RU = (int) 'A';
	private static final int ID_WALL_LU = (int) 'B';
	private static final int ID_WALL_RO = (int) 'C';
	private static final int ID_WALL_LO = (int) 'D';
	private static final int ID_WALL_H = (int) '-';
	private static final int ID_WALL_V = (int) '|';
	private static final int ID_WALL_GATE = (int) '=';
	private static final int ID_PILLE = (int) '.';
	private static final int ID_GROSSE_PILLE = (int) 'o';
	private static final int ID_DEATEYES_D = (int) ':';

	private static final MapPos HOME_BASHFUL_INKY = new MapPos(12, 14, 0);
	private static final MapPos HOME_POKEY_CLYDE = new MapPos(17, 14, 0);
	private static final MapPos HOME_SHADOW_BLINKY = new MapPos(12, 16, 0);
	private static final MapPos HOME_SPEEDY_PINKY = new MapPos(17, 16, 0);

	private Random random = new Random();

	private static final String[][] LEVEL_0 = { {

			"A----------------------------B", //
			"|a------------ba------------b|", //
			"||............||............||", //
			"||.a--b.a---b.||.a---b.a--b.||", //
			"||o|  |.|   |.||.|   |.|  |o||", //
			"||.c--d.c---d.cd.c---d.c--d.||", //
			"||..........................||", //
			"||.a--b.ab.a------b.ab.a--b.||", //
			"||.c--d.||.c--ba--d.||.c--d.||", //
			"||......||....||....||......||", //
			"|c----b.|c--b.||.a--d|.a----d|", //
			"|     |.|a--d.cd.c--b|.|     |", //
			"|     |.||..........||.|     |", //
			"|     |.||.A--==--B.||.|     |", //
			"C-----d.cd.|      |.cd.c-----D", //
			" ..........|      |.......... ", //
			"A-----b.ab.|      |.ab.a-----B", //
			"|     |.||.C------D.||.|     |", //
			"|     |.||..........||.|     |", //
			"|     |.||.a------b.||.|     |", //
			"|a----d.cd.c--ba--d.cd.c----b|", //
			"||............||............||", //
			"||.a--b.a---b.||.a---b.a--b.||", //
			"||.c-b|.c---d.cd.c---d.|a-d.||", //
			"||o..||................||..o||", //
			"|c-b.||.ab.a------b.ab.||.a-d|", //
			"|a-d.cd.||.c--ba--d.||.cd.c-b|", //
			"||......||....||....||......||", //
			"||.a----dc--b.||.a--dc----b.||", //
			"||.c--------d.cd.c--------d.||", //
			"||..........................||", //
			"|c--------------------------d|", //
			"C----------------------------D", } };

	class MoveState {
		Dir dir = Dir.STOP;
		Dir nextDir = Dir.STOP;
	}

	private MoveState pacmanState = new MoveState();
	private Entity pacman;
	private Entity[] ghosts = new Entity[4];

	@Override
	public void startLevel(int level) {
		clear();
		grid(-16, 16, -9f, 9f);
		map = tilemap(1f).tile(ID_WALL_ROUND_LO, TYPE_WALL, GFX_PAC_WALL_ROUND_LO) //
				.tile(ID_WALL_ROUND_RO, TYPE_WALL, GFX_PAC_WALL_ROUND_RO) //
				.tile(ID_WALL_ROUND_LU, TYPE_WALL, GFX_PAC_WALL_ROUND_LU) //
				.tile(ID_WALL_ROUND_RU, TYPE_WALL, GFX_PAC_WALL_ROUND_RU)//
				.tile(ID_WALL_LO, TYPE_WALL, GFX_PAC_WALL_LO) //
				.tile(ID_WALL_RO, TYPE_WALL, GFX_PAC_WALL_RO) //
				.tile(ID_WALL_LU, TYPE_WALL, GFX_PAC_WALL_LU) //
				.tile(ID_WALL_RU, TYPE_WALL, GFX_PAC_WALL_RU) //
				.tile(ID_WALL_H, TYPE_WALL, GFX_PAC_WALL_H) //
				.tile(ID_WALL_V, TYPE_WALL, GFX_PAC_WALL_V) //
				.tile(ID_WALL_GATE, TYPE_GATE, GFX_PAC_WALL_GATE) //
				.tile(ID_PILLE, TYPE_PILLE, GFX_PAC_PILLE_KLEIN) //
				.tile(ID_GROSSE_PILLE, TYPE_PILLE_GROSS, GFX_PAC_PILLE_GROSS) //
				.tile(ID_DEATEYES_D, TYPE_WALL, GFX_PAC_DEADEYES_D, GFX_PAC_DEADEYES_R, GFX_PAC_DEADEYES_U,
						GFX_PAC_DEADEYES_L)
				.relativePos(new Pos(0, 0)) //
				.defaultTile(ID_DEATEYES_D).initMap(LEVEL_0);

		map.tile(map.defaultTile()).gameLoop(animation(0, 3, false, 2f));

		pacman = entity(TYPE_PACMAN, 2f, GFX_PAC_PACMAN_L3, GFX_PAC_PACMAN_L0, GFX_PAC_PACMAN_L1, GFX_PAC_PACMAN_L2)
				.mapPos(new MapPos(2, 2, 0)).rotate(180).moveSpeed(8f);
		pacman.animation().imageStart(0).imageEnd(0).speed(3f).bounce(false);
		map.focus(pacman);
		pacman.onFinishMove((me, map) -> pacmanGo());

		MoveFinishedListener ghostMoveFinischedListener = (ghost, map) -> {
			if (ghost.checkType(ghost.lastMove(), type -> type == TYPE_WALL || ( //
			(ghost.checkType(ghost.lastMove().left(), type2 -> type2 != TYPE_WALL)
					|| ghost.checkType(ghost.lastMove().right(), type3 -> type3 != TYPE_WALL)
							&& random.nextBoolean())))) {
				int count = 0;
				Dir dir = ghost.lastMove() == Dir.STOP ? Dir.UP : ghost.lastMove();
				if (random.nextBoolean()) {
					dir = dir.left();
				} else {
					dir = dir.right();
				}
				while (count < 4 && ghost.checkType(dir, type -> type == TYPE_WALL)) {
					count++;
					dir = dir.left();
				}
				int o = dir.ordinal();
				ghost.move(dir);
				ghost.animation().imageStart(o-1).imageEnd(o).bounce(false).speed(1f);
			} else {
				ghost.move();
			}
		};

		ghosts[0] = entity(TYPE_GHOST, 2f, //
				GFX_PAC_BASHFUL_INKY_L0, GFX_PAC_BASHFUL_INKY_L1, //
				GFX_PAC_BASHFUL_INKY_U0, GFX_PAC_BASHFUL_INKY_U1, //
				GFX_PAC_BASHFUL_INKY_R0, GFX_PAC_BASHFUL_INKY_R1, //
				GFX_PAC_BASHFUL_INKY_D0, GFX_PAC_BASHFUL_INKY_D1, //
				GFX_PAC_GHOST_BLUE0, GFX_PAC_GHOST_BLUE1, //
				GFX_PAC_GHOST_BLUE0, GFX_PAC_GHOST_BLUE1, //
				GFX_PAC_DEADEYES_L, //
				GFX_PAC_DEADEYES_U, //
				GFX_PAC_DEADEYES_R, //
				GFX_PAC_DEADEYES_D //
		).mapPos(HOME_BASHFUL_INKY).moveSpeed(8f).onFinishMove(ghostMoveFinischedListener);

		ghosts[1] = entity(TYPE_GHOST, 2f, //
				GFX_PAC_POKEY_CLYDE_L0, GFX_PAC_POKEY_CLYDE_L1, //
				GFX_PAC_POKEY_CLYDE_U0, GFX_PAC_POKEY_CLYDE_U1, //
				GFX_PAC_POKEY_CLYDE_R0, GFX_PAC_POKEY_CLYDE_R1, //
				GFX_PAC_POKEY_CLYDE_D0, GFX_PAC_POKEY_CLYDE_D1, //
				GFX_PAC_GHOST_BLUE0, GFX_PAC_GHOST_BLUE1, //
				GFX_PAC_GHOST_BLUE0, GFX_PAC_GHOST_BLUE1, //
				GFX_PAC_DEADEYES_L, //
				GFX_PAC_DEADEYES_U, //
				GFX_PAC_DEADEYES_R, //
				GFX_PAC_DEADEYES_D //
		).mapPos(HOME_POKEY_CLYDE).moveSpeed(8f).onFinishMove(ghostMoveFinischedListener);

		ghosts[2] = entity(TYPE_GHOST, 2f, //
				GFX_PAC_SHADOW_BLINKY_L0, GFX_PAC_SHADOW_BLINKY_L1, //
				GFX_PAC_SHADOW_BLINKY_U0, GFX_PAC_SHADOW_BLINKY_U1, //
				GFX_PAC_SHADOW_BLINKY_R0, GFX_PAC_SHADOW_BLINKY_R1, //
				GFX_PAC_SHADOW_BLINKY_D0, GFX_PAC_SHADOW_BLINKY_D1, //
				GFX_PAC_GHOST_BLUE0, GFX_PAC_GHOST_BLUE1, //
				GFX_PAC_GHOST_BLUE0, GFX_PAC_GHOST_BLUE1, //
				GFX_PAC_DEADEYES_L, //
				GFX_PAC_DEADEYES_U, //
				GFX_PAC_DEADEYES_R, //
				GFX_PAC_DEADEYES_D //
		).mapPos(HOME_SHADOW_BLINKY).moveSpeed(8f).onFinishMove(ghostMoveFinischedListener);

		ghosts[3] = entity(TYPE_GHOST, 2f, //
				GFX_PAC_SPEEDY_PINKY_L0, GFX_PAC_SPEEDY_PINKY_L1, //
				GFX_PAC_SPEEDY_PINKY_U0, GFX_PAC_SPEEDY_PINKY_U1, //
				GFX_PAC_SPEEDY_PINKY_R0, GFX_PAC_SPEEDY_PINKY_R1, //
				GFX_PAC_SPEEDY_PINKY_D0, GFX_PAC_SPEEDY_PINKY_D1, //
				GFX_PAC_GHOST_BLUE0, GFX_PAC_GHOST_BLUE1, //
				GFX_PAC_GHOST_BLUE0, GFX_PAC_GHOST_BLUE1, //
				GFX_PAC_DEADEYES_L, //
				GFX_PAC_DEADEYES_U, //
				GFX_PAC_DEADEYES_R, //
				GFX_PAC_DEADEYES_D //
		).mapPos(HOME_SPEEDY_PINKY).moveSpeed(8f).onFinishMove(ghostMoveFinischedListener);

		for (Entity ghost : ghosts) {
			ghostMoveFinischedListener.onDestinationReached(ghost, map);
		}

		// keys

		key(KeyCode.ESCAPE, () -> exit());

		key(KeyCode.LEFT, () -> packmanGo(Dir.LEFT));
		key(KeyCode.UP, () -> packmanGo(Dir.UP));
		key(KeyCode.DOWN, () -> packmanGo(Dir.DOWN));
		key(KeyCode.RIGHT, () -> packmanGo(Dir.RIGHT));
		key(KeyCode.SPACE, () -> packmanGo(Dir.STOP));

		// debug

		debug(true);
	}

	private void packmanGo(Dir nextDir) {
		pacmanState.nextDir = nextDir;
		if (pacmanState.dir == Dir.STOP) {
			pacmanGo();
		}
	}

	private void pacmanGo() {

		// eat

		if (pacman.checkType(type -> type == TYPE_PILLE || type == TYPE_PILLE_GROSS)) {
			pacman.take();
		}

		// check if it is outside, then return opposite of it

		if (pacman.mapPos().x() == 0) {
			pacman.mapPos(new MapPos(map.size().x() - 1, pacman.mapPos().y(), pacman.mapPos().z()));
		} else if (pacman.mapPos().x() == map.size().x() - 1) {
			pacman.mapPos(new MapPos(0, pacman.mapPos().y(), pacman.mapPos().z()));
		}

		if (pacman.mapPos().y() == 0) {
			pacman.mapPos(new MapPos(map.size().x(), pacman.mapPos().y() - 2, pacman.mapPos().z()));
		} else if (pacman.mapPos().y() == map.size().y() - 1) {
			pacman.mapPos(new MapPos(pacman.mapPos().x(), 0, pacman.mapPos().z()));
		}

		// check move:

		if (pacman.checkType(pacmanState.nextDir, type -> type == TYPE_WALL || type == TYPE_GATE)) {
			if (pacman.checkType(pacmanState.dir, type -> type == TYPE_WALL || type == TYPE_GATE)) {
				pacmanState.dir = Dir.STOP;
				pacmanState.nextDir = Dir.STOP;
			}
		} else {
			pacmanState.dir = pacmanState.nextDir;
		}

		// animation:

		if (pacmanState.dir == Dir.STOP) {
			pacman.animation().imageEnd(0);
		} else {
			pacman.rotation(pacmanState.dir.rotation()).animation().imageEnd(3);
		}

		// move:

		pacman.move(pacmanState.dir);
	}

	@Override
	public void gameLoop(long gesamtZeit, long deltaZeit) {
	}

	@Override
	public void win() {
		// TODO Auto-generated method stub

	}

	@Override
	public void gameOver() {
		// TODO Auto-generated method stub

	}

	@Override
	public void highscore() {
		// TODO Auto-generated method stub

	}

	@Override
	public void credits() {
		// TODO Auto-generated method stub

	}

	// ===================== main-Methode, um das Programm zu starten =========

	public static void main(String[] args) {
		launch(args);
	}

}
