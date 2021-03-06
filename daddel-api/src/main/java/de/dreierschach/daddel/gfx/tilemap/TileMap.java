package de.dreierschach.daddel.gfx.tilemap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import de.dreierschach.daddel.Screen.Debug;
import de.dreierschach.daddel.gfx.sprite.Sprite;
import de.dreierschach.daddel.model.MapPos;
import de.dreierschach.daddel.model.Pos;
import de.dreierschach.daddel.model.Scr;
import de.dreierschach.daddel.model.Transformation;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * ein gekacheltes Spielfeld - TODO: mehrere Ebenen
 * 
 * @author Christian
 *
 */
public class TileMap extends Sprite {
	public static final Tile NO_TILE = new Tile(new Transformation(10, 10), -999, 1);
	public static final int NO_ID = -1;
	public static final int NO_TYPE = -1;

	private Map<Integer, Tile> tiles = new HashMap<>();
	private List<Entity> entities = new LinkedList<>();
	private MapPos size;
	private double tileSize;
	private int[][][] map;
	private Transformation transformation;
	private int defaultTile = NO_ID;
	private boolean endless = false;
	private Entity focus = null;

	/**
	 * @param transformation
	 *            Informationen zur Umrechnung von Spielraster-Punkten in
	 *            Bildschirmpixel
	 * @param tileSize
	 *            die maximale Breite und Höhe einer Kachel in Spielraster-Punkten
	 */
	public TileMap(Transformation transformation, double tileSize) {
		super(transformation, -1, 0);
		this.tileSize = tileSize;
		this.transformation = transformation;
		clear(new MapPos(8, 8, 1));
	}

	/**
	 * Löscht das Spielfeld
	 * 
	 * @return this
	 */
	public TileMap clear() {
		return clear(defaultTile);
	}

	/**
	 * Erzeugt das Spielfeld neu in der angegebenen Größe
	 * 
	 * @param size
	 *            Größe des Spielfelds (Breite x Höhe x Tiefe)
	 * @return this
	 */
	public TileMap clear(MapPos size) {
		return clear(size, defaultTile);
	}

	/**
	 * Erzeugt das Spielfeld neu in der angegebenen Größe und der angegebenen Kachel
	 * 
	 * @param size
	 *            Größe des Spielfelds (Breite x Höhe x Tiefe) *
	 * @param id
	 *            Id der Kachel
	 * @return this
	 */
	public TileMap clear(MapPos size, int id) {
		this.size = size;
		this.map = new int[size.x()][size.y()][size.z()];
		// this.entities.clear();
		return clear(id);
	}

	/**
	 * Löscht das Spielfeld mit der angegebenen Kachel
	 * 
	 * @param id
	 *            Id der Kachel
	 * @return this
	 */
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

	/**
	 * Initialisiert das Spielfeld mit dem angegebenen Array von Strings
	 * 
	 * @param levelData
	 *            z.B. {"oxo", "xox", "oxo"},["x.x", "x.x", "xxx"} definiert ein 3x3
	 *           
	 *            Spielfeld mit 2 Ebenen
	 * @return this
	 */
	public TileMap initMap(String[]... levelData) {
		this.size = new MapPos(levelData[0][0].length(), levelData[0].length, levelData.length);
		this.map = new int[size.x()][size.y()][size.z()];
		for (int z = 0; z < size.z(); z++) {
			for (int y = 0; y < size.y(); y++) {
				for (int x = 0; x < size.x(); x++) {
					int id = (int) (levelData[z][y].charAt(x));
					id(new MapPos(x, y, z), tiles.containsKey(id) ? id : NO_ID);
				}
			}
		}
		return this;
	}

	public TileMap entity(Entity entity) {
		this.entities.add(entity);
		return this;
	}

	public List<Entity> entities() {
		return entities;
	}

	/**
	 * @return Größe des Spielfelds
	 */
	public MapPos size() {
		return size;
	}

	/**
	 * Fügt eine neue Kachel hinzu
	 * 
	 * @param id
	 *            die Id der Kachel
	 * @param type
	 *            der Typ der Kachel
	 * @param imagefiles
	 *            die Dateipfade der Bilder der Kachel
	 * @return this
	 */
	public TileMap tile(int id, int type, String... imagefiles) {
		this.tiles.put(id, new Tile(transformation, type, tileSize, imagefiles));
		return this;
	}

	/**
	 * @param id
	 *            die Id der gewünschten Kachel
	 * @return die Kachel mit der angegebenen Id
	 */
	public Tile tile(int id) {
		return this.tiles.containsKey(id) ? this.tiles.get(id) : NO_TILE;
	}

	/**
	 * @param pos
	 *            die Position der gewünschten Kachel
	 * @return die Kachel an der angegebenen Position
	 */
	public Tile tile(MapPos pos) {
		return tile(id(pos));
	}

	/**
	 * @param pos
	 *            die Position der gewünschten Kachel-Id
	 * @return die Kachel-Id an der angegebenen Position
	 */
	public int id(MapPos pos) {
		if (endless) {
			return map[mod(pos.x(), size.x())][mod(pos.y(), size.y())][mod(pos.z(), size.z())];
		}
		return isValidPosition(pos) ? map[pos.x()][pos.y()][pos.z()] : defaultTile;
	}

	/**
	 * @param pos
	 *            die Position der gewünschten Kachel
	 * @return der Typ der Kachel an der angegebenen Position
	 */
	public int type(MapPos pos) {
		return tile(id(pos)).type();
	}

	/**
	 * @param pos
	 *            eine Position
	 * @return true, wenn die Position innerhalb der Grenzen des Spielfelds liegt
	 */
	public boolean isValidPosition(MapPos pos) {
		return isIn(pos.x(), 0, size.x()) && isIn(pos.y(), 0, size.y()) && isIn(pos.z(), 0, size.z());
	}

	/**
	 * @param p
	 *            eine Position
	 * @return true, wenn die Position links vom Spielfeld liegt
	 */
	public boolean leftOfMap(MapPos p) {
		return p.x() < 0;
	}

	/**
	 * @param p
	 *            eine Position
	 * @return true, wenn die Position rechts vom Spielfeld liegt
	 */
	public boolean rightOfMap(MapPos p) {
		return p.x() >= size.x();
	}

	/**
	 * @param p
	 *            eine Position
	 * @return true, wenn die Position oberhalb des Spielfelds liegt
	 */
	public boolean onTopOfMap(MapPos p) {
		return p.y() < 0;
	}

	/**
	 * @param p
	 *            eine Position
	 * @return true, wenn die Position unterhalb des Spielfelds liegt
	 */
	public boolean belowBottomOfMap(MapPos p) {
		return p.y() >= size.y();
	}

	/**
	 * Legt die default-Kachel fest
	 * 
	 * @param id
	 *            die Id der default-Kachel
	 * @return this
	 */
	public TileMap defaultTile(int id) {
		this.defaultTile = id;
		return this;
	}

	/**
	 * @return die Id der default-Kachel
	 */
	public int defaultTile() {
		return defaultTile;
	}

	/**
	 * Legt fest, ob das Spielfeld unendlich ist
	 * 
	 * @param endless
	 *            true, wenn das Spielfels unendlich ist
	 * @return this
	 */
	public TileMap endless(boolean endless) {
		this.endless = endless;
		return this;
	}

	/**
	 * @return true, wenn das Spielfels unendlich ist
	 */
	public boolean endless() {
		return endless;
	}

	/**
	 * Setzt die Id an einer bestimmten Position im Spielfeld
	 * 
	 * @param pos
	 *            die Position
	 * @param id
	 *            die Id
	 * @return this
	 */
	public TileMap id(MapPos pos, int id) {
		if (endless) {
			map[mod(pos.x(), size.x())][mod(pos.y(), size.y())][mod(pos.z(), size.z())] = id;
		} else if (isValidPosition(pos)) {
			map[pos.x()][pos.y()][pos.z()] = id;
		}
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.dreierschach.daddel.gfx.sprite.Sprite#parent(de.dreierschach.daddel.gfx.
	 * sprite.Sprite)
	 */
	@Override
	public TileMap parent(Sprite parent) {
		super.parent(parent);
		return this;
	}

	/**
	 * Legt die Entity fest, die im Fokus steht. Das Spielfeld wird so angezeigt,
	 * dass die angegebene Entity mittig auf dem Bildschirm zu sehen ist.
	 * 
	 * @param focus
	 *            die fokussierte Entity
	 * @return this
	 */
	public TileMap focus(Entity focus) {
		this.focus = focus;
		return this;
	}

	/**
	 * Legt fest, dass das Spielfeld nicht automatisch auf eine Entity fokussiert
	 * ist.
	 * 
	 * @return this
	 */
	public TileMap noFocus() {
		focus = null;
		return this;
	}

	/**
	 * @return die Entity, auf die das Spielfeld fokussiert ist
	 */
	public Entity focus() {
		return focus;
	}

	/**
	 * @return true, wenn das Spielfeld auf eine Entity fokussiert ist
	 */
	public boolean hasFocus() {
		return focus != null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.dreierschach.daddel.gfx.sprite.Sprite#gameLoop(long)
	 */
	public void gameLoop(long delta) {
		runEntities(delta);
		tiles.values().forEach(tile -> tile.gameLoop(delta));
		if (hasFocus()) {
			pos(new Pos(-focus.pos().x(), -focus.pos().y()));
		}
	}

	private void runEntities(long delta) {
		List<Entity> entitiesCopy = new ArrayList<>(entities);
		Iterator<Entity> it = entitiesCopy.iterator();
		while (it.hasNext()) {
			Entity entity = it.next();
			if (entity.alive()) {
				entity.gameLoop(delta);
			}
		}

		it = entities.iterator();
		while (it.hasNext()) {
			Entity entity = it.next();
			if (!entity.alive()) {
				it.remove();
			}
		}
	}

	/**
	 * @param x
	 *            die X-Koordinate der Spielfeld-Position
	 * @param y
	 *            die Y-Koordinate der Spielfeld-Position
	 * @return die Position der Kachel in Spielraster-Punkten
	 */
	public Pos toPos(int x, int y) {
		return new Pos(((double) x) * tileSize, ((double) y) * tileSize);
	}

	// private methods

	private boolean isIn(int value, int min, int max) {
		return value >= min && value < max;
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

	public void draw(GraphicsContext g) {
		double dx = transformation.getRasterRightBottom().x() - transformation.getRasterLeftUpper().x();
		double dy = transformation.getRasterRightBottom().y() - transformation.getRasterLeftUpper().y();
		int x0 = -1 + (int) (-effektivePos().x() - dx * 0.5f / tileSize);
		int y0 = -1 + (int) (-effektivePos().y() - dy * 0.5f / tileSize);
		int x1 = 2 + x0 + (int) (dx / tileSize);
		int y1 = 2 + y0 + (int) (dy / tileSize);

		if (!debug().wireframe()) {
			drawEntities(g, entity -> entity.mapPos().z() < 0);
			for (int d = 0; d < size.z(); d++) {
				for (int x = x0; x <= x1; x++) {
					for (int y = y0; y <= y1; y++) {
						Pos prel = toPos(x, y);
						Pos p = new Pos(prel.x() + effektivePos().x(), prel.y() + effektivePos().y());
						int id = id(new MapPos(x, y, d));
						Tile tile = tile(id);
						if (tile != NO_TILE) {
							tile.draw(g, p);
						}
					}
				}
				final int depth = d;

				drawEntities(g, entity -> entity.mapPos().z() == depth);
			}
			drawEntities(g, entity -> entity.mapPos().z() >= size.z());
		}
		if (debug().info() || debug().wireframe()) {
			Pos halfTileSize = new Pos(tileSize / 2, tileSize / 2);
			for (int x = x0; x <= x1; x++) {
				Pos p0 = toPos(x, y0).add(effektivePos()).add(halfTileSize);
				Pos p1 = toPos(x, y1).add(effektivePos()).add(halfTileSize);
				line(g, p0, p1);
			}
			for (int y = y0; y <= y1; y++) {
				Pos p0 = toPos(x0, y).add(effektivePos()).add(halfTileSize);
				Pos p1 = toPos(x1, y).add(effektivePos()).add(halfTileSize);
				line(g, p0, p1);
			}
		}
	}

	private void drawEntities(GraphicsContext g, Predicate<Entity> predicate) {
		entities.stream().filter(predicate).forEach(entity -> {
			g.save();
			entity.debug(debug());
			entity.drawSprite(g);
			g.restore();
		});
	}

	private void line(GraphicsContext g, Pos p0, Pos p1) {
		Scr scr0 = transformation().t(p0);
		Scr scr1 = transformation().t(p1);
		g.setFill(Color.gray(0.5));
		g.fillRect(scr0.x(), scr0.y(), scr1.x() - scr0.x() + 1, scr1.y() - scr0.y() + 1);
	}
	// overwrite methods for correct return type

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.dreierschach.daddel.gfx.sprite.Sprite#debug(boolean)
	 */
	@Override
	public Sprite debug(Debug debug) {
		super.debug(debug);
		return this;
	}

	@Override
	public TileMap pos(double x, double y) {
		super.pos(x, y);
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.dreierschach.daddel.gfx.sprite.Sprite#relativePos(de.dreierschach.daddel.
	 * model.Pos)
	 */
	@Override
	public TileMap pos(Pos pos) {
		super.pos(pos);
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.dreierschach.daddel.gfx.sprite.Sprite#rotation(double)
	 */
	@Override
	public TileMap rotation(double rotation) {
		super.rotation(rotation);
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.dreierschach.daddel.gfx.sprite.Sprite#rotate(double)
	 */
	@Override
	public TileMap rotate(double angle) {
		super.rotate(angle);
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.dreierschach.daddel.gfx.sprite.Sprite#direction(double)
	 */
	@Override
	public TileMap direction(double direction) {
		super.direction(direction);
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.dreierschach.daddel.gfx.sprite.Sprite#r(double)
	 */
	@Override
	public TileMap r(double r) {
		super.r(r);
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.dreierschach.daddel.gfx.sprite.Sprite#type(int)
	 */
	@Override
	public TileMap type(int type) {
		super.type(type);
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.dreierschach.daddel.gfx.sprite.Sprite#move(de.dreierschach.daddel.model.
	 * Pos)
	 */
	@Override
	public TileMap move(Pos direction) {
		super.move(direction);
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.dreierschach.daddel.gfx.sprite.Sprite#move(double)
	 */
	@Override
	public TileMap move(double distance) {
		super.move(distance);
		return this;
	}
}
