package de.dreierschach.daddel.gfx.tilemap;

import de.dreierschach.daddel.gfx.sprite.ImageSprite;
import de.dreierschach.daddel.gfx.sprite.Sprite;
import de.dreierschach.daddel.listener.CollisionListener;
import de.dreierschach.daddel.model.Pos;
import de.dreierschach.daddel.model.SpriteGameLoop;
import de.dreierschach.daddel.model.Transformation;
import javafx.scene.canvas.GraphicsContext;

/**
 * eine Kachel für ein gekacheltes Spielfeld
 * 
 * @author Christian
 *
 */
public class Tile extends ImageSprite {

	/**
	 * @param transformation
	 *            Informationen zur Umrechnung von Spielraster-Punkten in
	 *            Bildschirmpixel
	 * @param type
	 *            ein benutzerdefinierter Typ, Integer
	 * @param maxSize
	 *            die maximale Breite und Höhe der Kachel in Spielraster-Punkten
	 * @param imagefiles
	 *            die Bilder der Kachel
	 */
	public Tile(Transformation transformation, int type, float maxSize, String... imagefiles) {
		super(transformation, type, maxSize, imagefiles);
	}

	/**
	 * zeichnet die Kachel
	 * 
	 * @param g
	 *            Grafikkontext
	 * @param pos
	 *            Position in Spielraster-Punkten
	 */
	public void draw(GraphicsContext g, Pos pos) {
		this.pos(pos);
		super.draw(g);
	}

	// override methods to return correct type

	/* (non-Javadoc)
	 * @see de.dreierschach.daddel.gfx.sprite.ImageSprite#rotation(double)
	 */
	@Override
	public Tile rotation(double rotation) {
		super.rotation(rotation);
		return this;
	}

	/* (non-Javadoc)
	 * @see de.dreierschach.daddel.gfx.sprite.ImageSprite#rotate(double)
	 */
	@Override
	public Tile rotate(double angle) {
		super.rotate(angle);
		return this;
	}

	/* (non-Javadoc)
	 * @see de.dreierschach.daddel.gfx.sprite.ImageSprite#r(float)
	 */
	@Override
	public Tile r(float r) {
		super.r(r);
		return this;
	}

	/* (non-Javadoc)
	 * @see de.dreierschach.daddel.gfx.sprite.ImageSprite#type(int)
	 */
	@Override
	public Tile type(int type) {
		super.type(type);
		return this;
	}

	/* (non-Javadoc)
	 * @see de.dreierschach.daddel.gfx.sprite.ImageSprite#gameLoop(de.dreierschach.daddel.model.SpriteGameLoop[])
	 */
	@Override
	public Tile gameLoop(SpriteGameLoop... gameLoops) {
		super.gameLoop(gameLoops);
		return this;
	}

	/* (non-Javadoc)
	 * @see de.dreierschach.daddel.gfx.sprite.ImageSprite#parent(de.dreierschach.daddel.gfx.sprite.Sprite)
	 */
	@Override
	public Tile parent(Sprite parent) {
		super.parent(parent);
		return this;
	}

	/* (non-Javadoc)
	 * @see de.dreierschach.daddel.gfx.sprite.ImageSprite#move(de.dreierschach.daddel.model.Pos)
	 */
	@Override
	public Tile move(Pos direction) {
		super.move(direction);
		return this;
	}

	/* (non-Javadoc)
	 * @see de.dreierschach.daddel.gfx.sprite.ImageSprite#move(float)
	 */
	@Override
	public Tile move(float distance) {
		super.move(distance);
		return this;
	}

	/* (non-Javadoc)
	 * @see de.dreierschach.daddel.gfx.sprite.ImageSprite#actualImage(int)
	 */
	@Override
	public Tile actualImage(int actualImage) {
		super.actualImage(actualImage);
		return this;
	}

	/* (non-Javadoc)
	 * @see de.dreierschach.daddel.gfx.sprite.ImageSprite#collisionListener(de.dreierschach.daddel.listener.CollisionListener)
	 */
	@Override
	public Tile collision(CollisionListener collisionListener) {
		super.collision(collisionListener);
		return this;
	}

	/* (non-Javadoc)
	 * @see de.dreierschach.daddel.gfx.sprite.ImageSprite#onCollision(de.dreierschach.daddel.gfx.sprite.Sprite)
	 */
	@Override
	public Tile onCollision(Sprite other) {
		super.onCollision(other);
		return this;
	}
}
