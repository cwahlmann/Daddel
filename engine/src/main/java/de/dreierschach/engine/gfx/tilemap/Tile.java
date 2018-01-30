package de.dreierschach.engine.gfx.tilemap;

import de.dreierschach.engine.gfx.sprite.ImageSprite;
import de.dreierschach.engine.gfx.sprite.Sprite;
import de.dreierschach.engine.gfx.sprite.SpriteGameLoop;
import de.dreierschach.engine.listener.CollisionListener;
import de.dreierschach.engine.model.Pos;
import de.dreierschach.engine.model.Transformation;
import javafx.scene.canvas.GraphicsContext;

public class Tile extends ImageSprite {

	public Tile(Transformation transformation, int type, float maxSize, String... imagefiles) {
		super(transformation, type, maxSize, imagefiles);
	}
	
	public void draw(GraphicsContext g, Pos pos) {
		this.relativePos(pos);
		super.draw(g);
	}
	
	// override methods to return correct type

	@Override
	public Tile rotation(double rotation) {
		super.rotation(rotation);
		return this;
	}

	@Override
	public Tile rotate(double angle) {
		super.rotate(angle);
		return this;
	}

	@Override
	public Tile r(float r) {
		super.r(r);
		return this;
	}

	@Override
	public Tile type(int type) {
		super.type(type);
		return this;
	}

	@Override
	public Tile gameLoop(SpriteGameLoop... gameLoops) {
		super.gameLoop(gameLoops);
		return this;
	}

	@Override
	public Tile parent(Sprite parent) {
		super.parent(parent);
		return this;
	}

	@Override
	public Tile move(Pos direction) {
		super.move(direction);
		return this;
	}

	@Override
	public Tile move(float distance) {
		super.move(distance);
		return this;
	}

	@Override
	public Tile actualImage(int actualImage) {
		super.actualImage(actualImage);
		return this;
	}

	@Override
	public Tile collisionListener(CollisionListener collisionListener) {
		super.collisionListener(collisionListener);
		return this;
	}

	@Override
	public Tile onCollision(Sprite other) {
		super.onCollision(other);
		return this;
	}	
}
