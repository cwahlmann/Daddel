package de.dreierschach.daddel.gfx.sprite;

import de.dreierschach.daddel.Screen.Debug;
import de.dreierschach.daddel.listener.CollisionListener;
import de.dreierschach.daddel.model.Pos;
import de.dreierschach.daddel.model.Scr;
import de.dreierschach.daddel.model.SpriteGameLoop;
import de.dreierschach.daddel.model.Transformation;
import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.scene.transform.Affine;

/**
 * ein Sprite in Form eines Bildes; kann animiert sein
 * 
 * @author Christian
 *
 */
public class InvisibleSprite extends Sprite {

	/**
	 * Constructor
	 * 
	 * @param transformation
	 *            Informationen zur Umrechnung von Spielraster-Punkten in
	 *            Bildschirmpixel
	 * @param type
	 *            Benutzerdefinierter Typ, Integer
	 * @param layer
	 *            Die Ebene, auf der der Sprite angezeigt wird
	 * @param r
	 *            Der Radius des Sprites für die Kollisionskontrolle
	 */
	public InvisibleSprite(Transformation transformation, int type, int layer, double r) {
		super(transformation, type, layer, r);
	}

	// ------------------------ API-Methoden --

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.dreierschach.daddel.gfx.sprite.Sprite#debug(boolean)
	 */
	public InvisibleSprite debug(Debug debug) {
		super.debug(debug);
		return this;
	}

	// -------------- interne Methoden --

	/* (non-Javadoc)
	 * @see de.dreierschach.daddel.gfx.sprite.Sprite#collision(de.dreierschach.daddel.listener.CollisionListener)
	 */
	public InvisibleSprite collision(CollisionListener collisionListener) {
		super.collision(collisionListener);
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.dreierschach.daddel.gfx.sprite.Sprite#draw(javafx.scene.canvas.
	 * GraphicsContext)
	 */
	@Override
	public void draw(GraphicsContext g) {
		if (debug().info()) {
			drawWireframe(g);
		} else if (debug().wireframe()) {
			drawWireframe(g);
			if (showPosOnDebug()) {
				drawInfo(g);
			}
		}
	}

	private void drawWireframe(GraphicsContext g) {
		Scr scr = transformation().t(effektivePos());
		g.setGlobalAlpha(1.0);
		g.setStroke(Color.gray(0.5));
		double d = transformation().zoom(r()) * 2;
		g.strokeOval(scr.x() - d / 2, scr.y() - d / 2, d, d);
	}

	private void drawInfo(GraphicsContext g) {
		Scr scr = transformation().t(effektivePos());
		g.setTransform(new Affine());
		g.setFill(Color.gray(0.5));
		g.setFont(Font.font(16));
		g.setTextAlign(TextAlignment.CENTER);
		g.setTextBaseline(VPos.CENTER);
		g.fillText(String.format("(%.3f / %.3f)", effektivePos().x(), effektivePos().y()), scr.x(), scr.y());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.dreierschach.daddel.gfx.sprite.Sprite#showPosOnDebug(boolean)
	 */
	@Override
	public InvisibleSprite showPosOnDebug(boolean showPosOnDebug) {
		super.showPosOnDebug(showPosOnDebug);
		return this;
	}
	// -------------- override methods to return correct type --

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.dreierschach.daddel.gfx.sprite.Sprite#pos(double, double)
	 */
	@Override
	public InvisibleSprite pos(double x, double y) {
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
	public InvisibleSprite pos(Pos pos) {
		super.pos(pos);
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.dreierschach.daddel.gfx.sprite.Sprite#rotation(double)
	 */
	@Override
	public InvisibleSprite rotation(double rotation) {
		super.rotation(rotation);
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.dreierschach.daddel.gfx.sprite.Sprite#rotate(double)
	 */
	@Override
	public InvisibleSprite rotate(double angle) {
		super.rotate(angle);
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.dreierschach.daddel.gfx.sprite.Sprite#direction(double)
	 */
	@Override
	public InvisibleSprite direction(double direction) {
		super.direction(direction);
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.dreierschach.daddel.gfx.sprite.Sprite#r(double)
	 */
	@Override
	public InvisibleSprite r(double r) {
		super.r(r);
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.dreierschach.daddel.gfx.sprite.Sprite#type(int)
	 */
	@Override
	public InvisibleSprite type(int type) {
		super.type(type);
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.dreierschach.daddel.gfx.sprite.Sprite#gameLoop(de.dreierschach.daddel.gfx.
	 * sprite.SpriteGameLoop[])
	 */
	@Override
	public InvisibleSprite gameLoop(SpriteGameLoop... gameLoops) {
		super.gameLoop(gameLoops);
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
	public InvisibleSprite parent(Sprite parent) {
		super.parent(parent);
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
	public InvisibleSprite move(Pos direction) {
		super.move(direction);
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.dreierschach.daddel.gfx.sprite.Sprite#move(double)
	 */
	@Override
	public InvisibleSprite move(double distance) {
		super.move(distance);
		return this;
	}
}
