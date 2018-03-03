package de.dreierschach.daddel.gfx.tilemap;

import de.dreierschach.daddel.gfx.sprite.ImageSprite;
import de.dreierschach.daddel.gfx.sprite.Sprite;
import de.dreierschach.daddel.listener.CollisionListener;
import de.dreierschach.daddel.model.MapPos;
import de.dreierschach.daddel.model.Pos;
import de.dreierschach.daddel.model.SpriteGameLoop;
import de.dreierschach.daddel.model.Transformation;
import de.dreierschach.daddel.validator.Validator;

/**
 * eine Entity ist eine selbständige Einheit, die sich auf einem gekachelten
 * Spielfeld (TileMap) bewegt
 * 
 * @author Christian
 *
 */
public class Entity extends ImageSprite {

	/**
	 * die acht möglichen Richtungen auf einem gekachelten Spielfeld
	 * 
	 * @author Christian
	 *
	 */
	public enum Dir {//
		STOP(new MapPos(0, 0, 0), 0), //
		LEFT(new MapPos(-1, 0, 0), 0), //
		LEFT_UP(new MapPos(-1, -1, 0), 45), //
		UP(new MapPos(0, -1, 0), 90), //
		RIGHT_UP(new MapPos(1, -1, 0), 135), //
		RIGHT(new MapPos(1, 0, 0), 180), //
		RIGHT_DOWN(new MapPos(1, 1, 0), 225), //
		DOWN(new MapPos(0, 1, 0), 270), //
		LEFT_DOWN(new MapPos(-1, 1, 0), 315); //
		private MapPos p;
		private double rotation;

		Dir(MapPos p, double rotation) {
			this.p = p;
			this.rotation = rotation;
		}

		public MapPos p() {
			return p;
		}

		public double rotation() {
			return rotation;
		}

		public Dir left() {
			switch (this) {
			case LEFT:
				return DOWN;
			case LEFT_UP:
				return LEFT_DOWN;
			case UP:
				return LEFT;
			case RIGHT_UP:
				return LEFT_UP;
			case RIGHT:
				return UP;
			case RIGHT_DOWN:
				return RIGHT_UP;
			case DOWN:
				return RIGHT;
			case LEFT_DOWN:
				return RIGHT_DOWN;
			default:
			case STOP:
				return STOP;
			}
		}

		public Dir right() {
			switch (this) {
			case LEFT:
				return UP;
			case LEFT_UP:
				return RIGHT_UP;
			case UP:
				return RIGHT;
			case RIGHT_UP:
				return RIGHT_DOWN;
			case RIGHT:
				return DOWN;
			case RIGHT_DOWN:
				return LEFT_DOWN;
			case DOWN:
				return LEFT;
			case LEFT_DOWN:
				return LEFT_UP;
			default:
			case STOP:
				return STOP;
			}
		}
	}

	private TileMap tileMap;
	private MoveFinishedListener moveFinishedListener = (me, tilemap) -> {
	};
	private MapPos mapPos = new MapPos(0, 0, 0);
	private MapPos destMapPos = new MapPos(0, 0, 0);
	private long moveTimeDelta = 0;
	private long moveStartTime = 0;
	private double moveSpeed = 1f;
	private boolean startMove = false;
	private boolean neatlessMove = false;
	private boolean moving = false;
	private Dir lastMove = Dir.STOP;
	private Animation animation;

	/**
	 * @param transformation
	 *            Informationen zur Umrechnung von Spielraster-Punkten in
	 *            Bildschirmpixel
	 * @param tileMap
	 *            das gekachelte Spielfeld, auf dem sich die Entity bewegt
	 * @param type
	 *            ein benutzerdefinierter Typ, Integer
	 * @param maxSize
	 *            die maximale Breite und Höhe der Entity in Spielraster-Punkten
	 * @param imagefiles
	 *            die Bilder der Entity
	 */
	public Entity(Transformation transformation, TileMap tileMap, int type, double maxSize, String... imagefiles) {
		super(transformation, type, 0, maxSize, imagefiles);
		this.animation = new Animation();
		gameLoop((me, total, deltatime) -> {

			if (startMove) {
				startMove = false;
				moving = true;
				if (neatlessMove) {
					moveStartTime += moveTimeDelta;
				} else {
					moveStartTime = total;
				}
				moveTimeDelta = (long) (1000f / moveSpeed);
			}
			if (moving) {
				if (total >= moveStartTime + moveTimeDelta) {
					moving = false;
					mapPos(destMapPos);
					moveFinishedListener.onDestinationReached(this, tileMap);
				} else {
					Pos start = tileMap.toPos(mapPos.x(), mapPos.y());
					Pos dest = tileMap.toPos(destMapPos.x(), destMapPos.y());
					double d = ((double) (total - moveStartTime)) / (double) moveTimeDelta;
					Pos p = new Pos(d * (dest.x() - start.x()) + start.x(), d * (dest.y() - start.y()) + start.y());
					me.pos(p);
				}
			}
			if (!moving) {
				me.pos(tileMap.toPos(mapPos.x(), mapPos.y()));
			}
		}, animation);
		this.tileMap = tileMap;
//		tileMap.entity(this);
//		parent(tileMap);
	}

	/**
	 * prüft, ob auf dem Spielfeld in der angegebenen Richtung eine bestimmte Kachel
	 * liegt
	 * 
	 * @param dir
	 *            die Richtung, in der geprüft wird
	 * @param isId
	 *            eine Funktion, die die gefundene Id prüft
	 * @return true, wenn das Ergebnis der Funktion isId true ist
	 */
	public boolean checkId(Dir dir, Validator<Integer> isId) {
		return isId.validate(tileMap.id(this.mapPos().add(dir.p())));
	}

	/**
	 * prüft, ob auf dem Spielfeld unter der Entity eine bestimmte Kachel liegt
	 * 
	 * @param isId
	 *            eine Funktion, die die gefundene Id prüft
	 * @return true, wenn das ergebnis der Funktion isId true ist
	 */
	public boolean checkId(Validator<Integer> isId) {
		return isId.validate(tileMap.id(this.mapPos()));
	}

	/**
	 * prüft, ob auf dem Spielfeld in der angegebenen Richtung eine bestimmte Kachel
	 * liegt
	 * 
	 * @param dir
	 *            die Richtung, in der geprüft wird
	 * @param isType
	 *            eine Funktion, die den gefundene Typ prüft
	 * @return true, wenn das Ergebnis der Funktion isTyp true ist
	 */
	public boolean checkType(Dir dir, Validator<Integer> isType) {
		return isType.validate(tileMap.type(this.mapPos().add(dir.p())));
	}

	/**
	 * prüft, ob auf dem Spielfeld unter der Entity eine bestimmte Kachel liegt
	 * 
	 * @param isType
	 *            eine Funktion, die den gefundene Typ prüft
	 * @return true, wenn das Ergebnis der Funktion isTyp true ist
	 */
	public boolean checkType(Validator<Integer> isType) {
		return isType.validate(tileMap.type(this.mapPos()));
	}

	/**
	 * nimmt die Kachel in der angegebenen Richtung auf
	 * 
	 * @param dir
	 *            die Richtung
	 * @return die Id der aufgenommenen Kachel
	 */
	public int take(Dir dir) {
		MapPos p = mapPos.add(dir.p());
		int id = tileMap.id(p);
		tileMap.id(p, TileMap.NO_ID);
		return id;
	}

	/**
	 * nimmt die Kachel unter der Entity auf
	 * 
	 * @return die Id der aufgenommenen Kachel
	 */
	public int take() {
		int id = tileMap.id(mapPos);
		tileMap.id(mapPos, TileMap.NO_ID);
		return id;
	}

	/**
	 * Legt in der angegebenen Richtung eine Kachel ab
	 * 
	 * @param dir
	 *            die Richtung
	 * @param id
	 *            die Id der abzulegeneden Kachel
	 * @return this
	 */
	public Entity drop(Dir dir, int id) {
		MapPos p = mapPos.add(dir.p());
		tileMap.id(p, id);
		return this;
	}

	/**
	 * Legt unter der Entity eine Kachel ab
	 * 
	 * @param id
	 *            die Id der abzulegeneden Kachel
	 * @return this
	 */
	public Entity drop(int id) {
		tileMap.id(mapPos, id);
		return this;
	}

	/**
	 * prüft, ob die angegebene Position innerhalb des Spielfelds liegt
	 * 
	 * @param p
	 *            die Position vom Typ MapPos
	 * @return true, wenn die Position innnerhalb des Spielfelds liegt
	 */
	public boolean insideMap(MapPos p) {
		return tileMap.isValidPosition(p);
	}

	/**
	 * prüft, ob die angegebene Position links vom Spielfeld liegt
	 * 
	 * @param p
	 *            die Position vom Typ MapPos
	 * @return true, wenn die Position links vom Spielfeld liegt
	 */
	public boolean leftOfMap(MapPos p) {
		return tileMap.leftOfMap(p);
	}

	/**
	 * prüft, ob die angegebene Position rechts vom Spielfeld liegt
	 * 
	 * @param p
	 *            die Position vom Typ MapPos
	 * @return true, wenn die Position rechts vom Spielfeld liegt
	 */
	public boolean rightOfMap(MapPos p) {
		return tileMap.rightOfMap(p);
	}

	/**
	 * prüft, ob die angegebene Position oberhalb des Spielfelds liegt
	 * 
	 * @param p
	 *            die Position vom Typ MapPos
	 * @return true, wenn die Position oberhalb des Spielfelds liegt
	 */
	public boolean onTopOfMap(MapPos p) {
		return tileMap.onTopOfMap(p);
	}

	/**
	 * prüft, ob die angegebene Position unterhalb des Spielfelds liegt
	 * 
	 * @param p
	 *            die Position vom Typ MapPos
	 * @return true, wenn die Position unterhalb des Spielfelds liegt
	 */
	public boolean belowBottomOfMap(MapPos p) {
		return tileMap.belowBottomOfMap(p);
	}

	/**
	 * @return die zuletzt eingeschlagene Richtung der Entity
	 */
	public Dir lastMove() {
		return this.lastMove;
	}

	/**
	 * Bewegt die Entity in die zuletzt eingeschlagene Richtung
	 *
	 * @return this
	 */
	public Entity move() {
		return move(lastMove);
	}

	/**
	 * Bewegt die Entity in die angegebene Richtung
	 *
	 * @param dir
	 *            die Richtung
	 * @return this
	 */
	public Entity move(Dir dir) {
		destMapPos(mapPos.add(dir.p()));
		startMove(lastMove != Dir.STOP);
		this.lastMove = dir;
		return this;
	}

	/**
	 * @return die Bewegungsgeschwindigkeit in Spielraster-Punkten / s
	 */
	public double moveSpeed() {
		return moveSpeed;
	}

	/**
	 * Legt die Bewegungsgeschwindigkeit fest
	 * 
	 * @param moveSpeed
	 *            die Bewegungsgeschwindigkeit in Spielraster-Punkten / s
	 * @return this
	 */
	public Entity moveSpeed(double moveSpeed) {
		this.moveSpeed = moveSpeed;
		return this;
	}

	/**
	 * @return die Position auf dem gekachelten Spielfeld
	 */
	public MapPos mapPos() {
		return mapPos;
	}

	/**
	 * Setzt die Position auf dem gekachelten Spielfeld
	 * 
	 * @param mapPos
	 *            die Position auf dem gekachelten Spielfeld
	 * @return this
	 */
	public Entity mapPos(MapPos mapPos) {
		this.mapPos = mapPos;
		// this.destMapPos = mapPos;
		return this;
	}

	/**
	 * @return die Zielposition auf dem gekachelten Spielfeld
	 */
	public MapPos destMapPos() {
		return destMapPos;
	}

	/**
	 * setzt die Zielposition auf dem gekachelten Spielfeld
	 * 
	 * @param destMapPos
	 *            die Zielposition auf dem gekachelten Spielfeld
	 * @return this
	 */
	public Entity destMapPos(MapPos destMapPos) {
		this.destMapPos = destMapPos;
		return this;
	}

	/**
	 * Startet die Bewegung zur Zielposition
	 * 
	 * @param neatless
	 *            true: Legt fest, ob das Timing der Bewegungen nahtlos an die
	 *            vorherige anschließen soll
	 * @return this
	 */
	public Entity startMove(boolean neatless) {
		this.startMove = true;
		this.neatlessMove = neatless;
		return this;
	}

	//

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.dreierschach.daddel.gfx.sprite.ImageSprite#relativePos(de.dreierschach.
	 * daddel.model.Pos)
	 */
	@Override
	public Entity pos(Pos pos) {
		super.pos(pos);
		return this;
	}

	/**
	 * Legt die Aktion fest, die ausgeführt wird, wenn die Zielposition erreicht ist
	 * 
	 * @param moveFinishedListener
	 *            die Aktion
	 * @return this
	 */
	public Entity onFinishMove(MoveFinishedListener moveFinishedListener) {
		this.moveFinishedListener = moveFinishedListener;
		this.moveFinishedListener.onDestinationReached(this, tileMap);
		return this;
	}

	/**
	 * @return die Bild-Animationsparameter der Entity
	 */
	public Animation animation() {
		return animation;
	}

	// -------------- override methods to return correct type --

	/* (non-Javadoc)
	 * @see de.dreierschach.daddel.gfx.sprite.ImageSprite#actualImage(int)
	 */
	@Override
	public Entity actualImage(int actualImage) {
		super.actualImage(actualImage);
		return this;
	}

	/* (non-Javadoc)
	 * @see de.dreierschach.daddel.gfx.sprite.ImageSprite#alpha(double)
	 */
	@Override
	public Entity alpha(double alpha) {
		super.alpha(alpha);
		return this;
	}

	/* (non-Javadoc)
	 * @see de.dreierschach.daddel.gfx.sprite.ImageSprite#collisionListener(de.dreierschach.daddel.listener.CollisionListener)
	 */
	@Override
	public Entity collision(CollisionListener collisionListener) {
		super.collision(collisionListener);
		return this;
	}

	/* (non-Javadoc)
	 * @see de.dreierschach.daddel.gfx.sprite.ImageSprite#rotation(double)
	 */
	@Override
	public Entity rotation(double rotation) {
		super.rotation(rotation);
		return this;
	}

	/* (non-Javadoc)
	 * @see de.dreierschach.daddel.gfx.sprite.ImageSprite#rotate(double)
	 */
	@Override
	public Entity rotate(double angle) {
		super.rotate(angle);
		return this;
	}

	/* (non-Javadoc)
	 * @see de.dreierschach.daddel.gfx.sprite.ImageSprite#direction(double)
	 */
	@Override
	public Entity direction(double direction) {
		super.direction(direction);
		return this;
	}

	/* (non-Javadoc)
	 * @see de.dreierschach.daddel.gfx.sprite.ImageSprite#r(double)
	 */
	@Override
	public Entity r(double r) {
		super.r(r);
		return this;
	}

	/* (non-Javadoc)
	 * @see de.dreierschach.daddel.gfx.sprite.ImageSprite#type(int)
	 */
	@Override
	public Entity type(int type) {
		super.type(type);
		return this;
	}

	/* (non-Javadoc)
	 * @see de.dreierschach.daddel.gfx.sprite.ImageSprite#gameLoop(de.dreierschach.daddel.model.SpriteGameLoop[])
	 */
	@Override
	public Entity gameLoop(SpriteGameLoop... gameLoops) {
		super.gameLoop(gameLoops);
		return this;
	}

	/* (non-Javadoc)
	 * @see de.dreierschach.daddel.gfx.sprite.ImageSprite#parent(de.dreierschach.daddel.gfx.sprite.Sprite)
	 */
	@Override
	public Entity parent(Sprite parent) {
		super.parent(parent);
		return this;
	}

	/* (non-Javadoc)
	 * @see de.dreierschach.daddel.gfx.sprite.ImageSprite#move(de.dreierschach.daddel.model.Pos)
	 */
	@Override
	public Entity move(Pos direction) {
		super.move(direction);
		return this;
	}

	/* (non-Javadoc)
	 * @see de.dreierschach.daddel.gfx.sprite.ImageSprite#move(double)
	 */
	@Override
	public Entity move(double distance) {
		super.move(distance);
		return this;
	}
}
