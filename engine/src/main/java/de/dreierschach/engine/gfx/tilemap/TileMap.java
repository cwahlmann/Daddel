package de.dreierschach.engine.gfx.tilemap;

import java.util.HashMap;
import java.util.Map;

import de.dreierschach.engine.gfx.sprite.Sprite;
import de.dreierschach.engine.model.MapPos;
import de.dreierschach.engine.model.Pos;
import de.dreierschach.engine.model.Transformation;
import javafx.scene.canvas.GraphicsContext;

public class TileMap extends Sprite {
	public static final Tile NO_TILE = new Tile(new Transformation(10, 10), -999, 1);
	public static final int NO_ID = -1;
	public static final int NO_TYPE = -1;

	private Map<Integer, Tile> tiles = new HashMap<>();
	private MapPos size;
	private float tileSize;
	private int[][][] map;
	private Transformation transformation;
	private int defaultTile = NO_ID;
	private boolean endless = false;
	private Entity focus = null;

	public TileMap(Transformation transformation, float tileSize) {
		super(transformation, -1);
		this.tileSize = tileSize;
		this.transformation = transformation;
		clear(new MapPos(8, 8, 1));
	}

	public TileMap clear() {
		return clear(0);
	}
	
	public TileMap clear(MapPos size) {
		return clear(size, defaultTile);
	}

	public TileMap clear(MapPos size, int id) {
		this.size = size;
		this.map = new int[size.x()][size.y()][size.z()];
		return clear(id);
	}
	
	public TileMap clear(int id) {
		for (int x = 0; x < size.x(); x++) {
			for (int y = 0; y < size.y(); y++) {
				for (int z = 0; z < size.z(); z++) {
					this.map[x][y][z] = id;
				}
			}
		}
		return this;
	}

	public TileMap initMap(String[][] levelData) {
		this.size = new MapPos(levelData[0][0].length(), levelData[0].length, levelData.length);
		this.map = new int[size.x()][size.y()][size.z()];
		for (int z = 0; z < size.z(); z++) {
			for (int y = 0; y < size.y(); y++) {
				for (int x = 0; x < size.x(); x++) {
					id(new MapPos(x, y, 0), (int) (levelData[z][y].charAt(x)));
				}
			}
		}
		return this;
	}
	
	public MapPos size() {
		return size;
	}

	public TileMap tile(int id, int type, String... imagefiles) {
		this.tiles.put(id, new Tile(transformation, type, tileSize, imagefiles));
		return this;
	}

	public Tile tile(int id) {
		return this.tiles.containsKey(id) ? this.tiles.get(id) : NO_TILE;
	}

	public Tile tile(MapPos pos) {
		return tile(id(pos));
	}

	public int id(MapPos pos) {
		if (endless) {
			return map[mod(pos.x(), size.x())][mod(pos.y(), size.y())][mod(pos.z(), size.z())];
		}
		return isValidPosition(pos) ? map[pos.x()][pos.y()][pos.z()] : defaultTile;
	}

	public int type(MapPos pos) {
		return tile(id(pos)).type();
	}

	public boolean isValidPosition(MapPos pos) {
		return isIn(pos.x(), 0, size.x()) && isIn(pos.y(), 0, size.y()) && isIn(pos.z(), 0, size.z());
	}

	public boolean leftOfMap(MapPos p) {
		return p.x() < 0;
	}

	public boolean rightOfMap(MapPos p) {
		return p.x() >= size.x();
	}

	public boolean onTopOfMap(MapPos p) {
		return p.y() < 0;
	}

	public boolean belowBottomOfMap(MapPos p) {
		return p.y() >= size.y();
	}

	private boolean isIn(int value, int min, int max) {
		return value >= min && value < max;
	}

	public TileMap defaultTile(int id) {
		this.defaultTile = id;
		return this;
	}

	public int defaultTile() {
		return defaultTile;
	}

	public TileMap endless(boolean endless) {
		this.endless = endless;
		return this;
	}

	public boolean endless() {
		return endless;
	}

	private int mod(int v, int d) {
		if (v >= 0) {
			return v % d;
		}
		int result = v;
		while (result < 0) {
			result += d;
		}
		return result;
	}

	public TileMap id(MapPos pos, int id) {
		if (endless) {
			map[mod(pos.x(), size.x())][mod(pos.y(), size.y())][mod(pos.z(), size.z())] = id;
		} else if (isValidPosition(pos)) {
			map[pos.x()][pos.y()][pos.z()] = id;
		}
		return this;
	}

	@Override
	public TileMap parent(Sprite parent) {
		super.parent(parent);
		return this;
	}
	
	public TileMap focus(Entity focus) {
		this.focus = focus;
		return this;
	}
	
	public TileMap noFocus() {
		focus = null;
		return this;
	}

	public Entity focus() {
		return focus;
	}

	public boolean hasFocus() {
		return focus != null;
	}
	
	public void gameLoop(long delta) {
		tiles.values().forEach(tile -> tile.gameLoop(delta));
		if (hasFocus()) {
			relativePos(new Pos(-focus.relativePos().x(), -focus.relativePos().y()));
		}
	}

	public Pos pos(int x, int y) {
		return new Pos((float) x * tileSize, (float) y * tileSize);
	}

	@Override
	public TileMap onCollision(Sprite other) {
		return this;
	}

	public void draw(GraphicsContext g) {
		double dx = transformation.getRasterRightBottom().x() - transformation.getRasterLeftUpper().x();
		double dy = transformation.getRasterRightBottom().y() - transformation.getRasterLeftUpper().y();
		int x0 = -1 + (int) (-pos().x() - dx * 0.5f / tileSize);
		int y0 = -1 + (int) (-pos().y() - dy * 0.5f / tileSize);
		int x1 = 2 + x0 + (int) (dx / tileSize);
		int y1 = 2 + y0 + (int) (dy / tileSize);

		for (int x = x0; x <= x1; x++) {
			for (int y = y0; y <= y1; y++) {
				Pos prel = pos(x, y);
				Pos p = new Pos(prel.x() + pos().x(), prel.y() + pos().y());
				for (int d = 0; d < size.z(); d++) {
					int id = id(new MapPos(x, y, d));
					Tile tile = tile(id);
					if (tile != NO_TILE) {
						tile.draw(g, p);
					}
				}
			}
		}
	}

	// overwrite methods for correct return type

	@Override
	public TileMap relativePos(Pos pos) {
		super.relativePos(pos);
		return this;
	}

	@Override
	public TileMap rotation(double rotation) {
		super.rotation(rotation);
		return this;
	}

	@Override
	public TileMap rotate(double angle) {
		super.rotate(angle);
		return this;
	}

	@Override
	public TileMap direction(double direction) {
		super.direction(direction);
		return this;
	}

	@Override
	public TileMap r(float r) {
		super.r(r);
		return this;
	}

	@Override
	public TileMap type(int type) {
		super.type(type);
		return this;
	}

	@Override
	public TileMap move(Pos direction) {
		super.move(direction);
		return this;
	}

	@Override
	public TileMap move(float distance) {
		super.move(distance);
		return this;
	}
}
