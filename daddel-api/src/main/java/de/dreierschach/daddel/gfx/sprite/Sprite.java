package de.dreierschach.daddel.gfx.sprite;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.dreierschach.daddel.model.Pos;
import de.dreierschach.daddel.model.Scr;
import de.dreierschach.daddel.model.Transformation;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.transform.Rotate;

public abstract class Sprite {
	private Pos pos = new Pos(0, 0);
	private float r;
	private int type;
	private List<SpriteGameLoop> gameLoops = new ArrayList<>();
	private boolean alive = true;
	private long ticks = 0;
	private double rotation = 0;
	private double direction = 0;
	private Transformation transformation;
	private Sprite parent = null;

	public Sprite(Transformation transformation, int type, int r) {
		this.r = r;
		this.type = type;
		this.transformation = transformation;
	}

	public Sprite(Transformation transformation, int type) {
		this(transformation, type, 1);
	}

	public Pos pos() {
		return !hasParent() ? pos : new Pos(parent.pos().x() + pos.x(), parent.pos().y() + pos.y());
	}

	public Pos relativePos() {
		return pos;
	}
	
	public Sprite relativePos(Pos pos) {
		this.pos = pos;
		return this;
	}

	public double rotation() {
		return rotation;
	}

	public Sprite rotation(double rotation) {
		this.rotation = rotation;
		return this;
	}

	public Sprite rotate(double angle) {
		rotation += angle;
		return this;
	}

	public double direction() {
		return direction;
	}

	public Sprite direction(double direction) {
		this.direction = direction;
		return this;
	}

	public float r() {
		return r;
	}

	public Sprite r(float r) {
		this.r = r;
		return this;
	}

	public int type() {
		return type;
	}

	public Sprite type(int type) {
		this.type = type;
		return this;
	}

	public boolean collides(Sprite other) {
		if (!other.alive()) {
			return false;
		}
		float dx = other.pos().x() - this.pos().x();
		float dy = other.pos().y() - this.pos().y();
		float dd = dx * dx + dy * dy;
		float dr = other.r + this.r;
		float ddr = dr * dr;
		return dd < ddr;
	}

	public boolean alive() {
		return alive;
	}

	public void kill() {
		this.alive = false;
	}

	public Sprite gameLoop(SpriteGameLoop... gameLoops) {
		this.gameLoops.addAll(Arrays.asList(gameLoops));
		return this;
	}

	public void gameLoop(long deltatime) {
		for (SpriteGameLoop gameLoop : gameLoops) {
			gameLoop.run(this, ticks, deltatime);
		}
		ticks += deltatime;
	}

	public Transformation transformation() {
		return transformation;
	}

	protected static void rotate(GraphicsContext g, double angle, Scr middle) {
		Rotate r = new Rotate(angle, middle.x(), middle.y());
		g.setTransform(r.getMxx(), r.getMyx(), r.getMxy(), r.getMyy(), r.getTx(), r.getTy());
	}

	protected long getTicks() {
		return ticks;
	}

	protected void setTicks(long ticks) {
		this.ticks = ticks;
	}

	public Sprite parent() {
		return parent;
	}
	
	public Sprite parent(Sprite parent) {
		this.parent = parent;
		return this;
	}
	
	public boolean hasParent() {
		return parent != null;
	}
	
	public void drawSprite(GraphicsContext g) {
		rotate(g, rotation, transformation.t(pos()));
		draw(g);
	}

	public Sprite move(Pos direction) {
		this.pos = new Pos(pos.x() + direction.x(), pos.y() + direction.y());
		return this;
	}

	public Sprite move(float distance) {
		Rotate r = new Rotate(direction);
		Point2D v2d = r.transform(new Point2D(1, 0));
		Pos v = new Pos((float) (v2d.getX()), (float) (v2d.getY()));
		this.pos = new Pos(this.pos.x() + v.x() * distance, pos.y() + v.y() * distance);
		return this;
	}

	public abstract Sprite onCollision(Sprite other);

	public abstract void draw(GraphicsContext g);
}
