package de.dreierschach.engine.gfx.tilemap;

import de.dreierschach.engine.gfx.sprite.ImageSprite;
import de.dreierschach.engine.gfx.sprite.Sprite;
import de.dreierschach.engine.gfx.sprite.SpriteGameLoop;
import de.dreierschach.engine.listener.CollisionListener;
import de.dreierschach.engine.model.MapPos;
import de.dreierschach.engine.model.Pos;
import de.dreierschach.engine.model.Transformation;

public class Entity extends ImageSprite {

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
			case LEFT: return DOWN;
			case LEFT_UP: return LEFT_DOWN;
			case UP: return LEFT;
			case RIGHT_UP: return LEFT_UP;
			case RIGHT: return UP;
			case RIGHT_DOWN: return RIGHT_UP;
			case DOWN: return RIGHT;
			case LEFT_DOWN: return RIGHT_DOWN;
			default:
			case STOP: return STOP; 
			}
		}

		public Dir right() {
			switch (this) {
			case LEFT: return UP;
			case LEFT_UP: return RIGHT_UP;
			case UP: return RIGHT;
			case RIGHT_UP: return RIGHT_DOWN;
			case RIGHT: return DOWN;
			case RIGHT_DOWN: return LEFT_DOWN;
			case DOWN: return LEFT;
			case LEFT_DOWN: return LEFT_UP;
			default:
			case STOP: return STOP; 
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
	private float moveSpeed = 1f;
	private boolean startMove = false;
	private boolean moving = false;
	private Dir lastMove = Dir.STOP; 
	private Animation animation;

	public Entity(Transformation transformation, TileMap tileMap, int type, float maxSize, String... imagefiles) {
		super(transformation, type, maxSize, imagefiles);
		this.animation = new Animation();
		gameLoop((me, total, deltatime) -> {

			if (startMove) {
				startMove = false;
				moving = true;
				moveStartTime = total;
				moveTimeDelta = (int) (1000f / moveSpeed);
			}
			if (moving) {
				if (total > moveStartTime + moveTimeDelta) {
					moving = false;
					mapPos(destMapPos);
					moveFinishedListener.onDestinationReached(this, tileMap);
				} else {
					Pos start = tileMap.pos(mapPos.x(), mapPos.y());
					Pos dest = tileMap.pos(destMapPos.x(), destMapPos.y());
					float d = (float) (total - moveStartTime) / (float) moveTimeDelta;
					Pos p = new Pos(d * (dest.x() - start.x()) + start.x(), d * (dest.y() - start.y()) + start.y());
					me.relativePos(p);
				}
			}
			if (!moving) {
				me.relativePos(tileMap.pos(mapPos.x(), mapPos.y()));
			}
		}, animation);
		this.tileMap = tileMap;
	}

	public boolean checkId(Dir dir, Validator<Integer> isId) {
		return isId.validate(tileMap.id(this.mapPos().add(dir.p())));
	}

	public boolean checkId(Validator<Integer> isId) {
		return isId.validate(tileMap.id(this.mapPos()));
	}

	public boolean checkType(Dir dir, Validator<Integer> isType) {
		return isType.validate(tileMap.type(this.mapPos().add(dir.p())));
	}

	public boolean checkType(Validator<Integer> isType) {
		return isType.validate(tileMap.type(this.mapPos()));
	}

	public int take(Dir dir) {
		MapPos p = mapPos.add(dir.p());
		int id = tileMap.id(p);
		tileMap.id(p, TileMap.NO_ID);
		return id;
	}

	public int take() {
		int id = tileMap.id(mapPos);
		tileMap.id(mapPos, TileMap.NO_ID);
		return id;
	}

	public Entity drop(Dir dir, int id) {
		MapPos p = mapPos.add(dir.p());
		tileMap.id(p, id);
		return this;
	}

	public Entity drop(int id) {
		tileMap.id(mapPos, id);
		return this;
	}

	public boolean insideMap(MapPos p) {
		return tileMap.isValidPosition(p);
	}
	
	public boolean leftOfMap(MapPos p) {
		return tileMap.leftOfMap(p);
	}

	public boolean rightOfMap(MapPos p) {
		return tileMap.rightOfMap(p);
	}

	public boolean onTopOfMap(MapPos p) {
		return tileMap.onTopOfMap(p);
	}

	public boolean belowBottomOfMap(MapPos p) {
		return tileMap.belowBottomOfMap(p);
	}

	public Dir lastMove() {
		return this.lastMove;
	}
	
	public Entity move() {
		return move(lastMove);
	}

	public Entity move(Dir dir) {
		destMapPos(mapPos.add(dir.p()));
		this.lastMove = dir;
		startMove();
		return this;
	}

	public float moveSpeed() {
		return moveSpeed;
	}

	public Entity moveSpeed(float moveSpeed) {
		this.moveSpeed = moveSpeed;
		return this;
	}

	public MapPos mapPos() {
		return mapPos;
	}

	public Entity mapPos(MapPos mapPos) {
		this.mapPos = mapPos;
		// this.destMapPos = mapPos;
		return this;
	}

	public MapPos destMapPos() {
		return destMapPos;
	}

	public Entity destMapPos(MapPos destMapPos) {
		this.destMapPos = destMapPos;
		return this;
	}

	public Entity startMove() {
		this.startMove = true;
		return this;
	}

	//

	@Override
	public Entity relativePos(Pos pos) {
		super.relativePos(pos);
		return this;
	}

	public Entity onFinishMove(MoveFinishedListener moveFinishedListener) {
		this.moveFinishedListener = moveFinishedListener;
		return this;
	}

	public Animation animation() {
		return animation;
	}

	// -------------- override methods to return correct type --

	@Override
	public Entity actualImage(int actualImage) {
		super.actualImage(actualImage);
		return this;
	}

	@Override
	public Entity alpha(float alpha) {
		super.alpha(alpha);
		return this;
	}

	@Override
	public Entity collisionListener(CollisionListener collisionListener) {
		super.collisionListener(collisionListener);
		return this;
	}

	@Override
	public Entity rotation(double rotation) {
		super.rotation(rotation);
		return this;
	}

	@Override
	public Entity rotate(double angle) {
		super.rotate(angle);
		return this;
	}

	@Override
	public Entity direction(double direction) {
		super.direction(direction);
		return this;
	}

	@Override
	public Entity r(float r) {
		super.r(r);
		return this;
	}

	@Override
	public Entity type(int type) {
		super.type(type);
		return this;
	}

	@Override
	public Entity gameLoop(SpriteGameLoop... gameLoops) {
		super.gameLoop(gameLoops);
		return this;
	}

	@Override
	public Entity parent(Sprite parent) {
		super.parent(parent);
		return this;
	}

	@Override
	public Entity move(Pos direction) {
		super.move(direction);
		return this;
	}

	@Override
	public Entity move(float distance) {
		super.move(distance);
		return this;
	}
}
