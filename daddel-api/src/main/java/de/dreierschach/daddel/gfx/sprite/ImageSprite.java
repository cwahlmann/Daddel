package de.dreierschach.daddel.gfx.sprite;

import java.util.ArrayList;
import java.util.List;

import de.dreierschach.daddel.Screen.Debug;
import de.dreierschach.daddel.gfx.ImageLib;
import de.dreierschach.daddel.listener.CollisionListener;
import de.dreierschach.daddel.model.Pos;
import de.dreierschach.daddel.model.Scr;
import de.dreierschach.daddel.model.SpriteGameLoop;
import de.dreierschach.daddel.model.Transformation;
import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

/**
 * ein Sprite in Form eines Bildes; kann animiert sein
 * 
 * @author Christian
 *
 */
public class ImageSprite extends Sprite {

	private List<Image> images = new ArrayList<>();
	private int witdh = 0;
	private int height = 0;
	private Pos size;
	private int actualImage;
	private CollisionListener collisionListener = (me, other) -> {
	};
	private double alpha = 1f;

	/**
	 * Constructor
	 * 
	 * @param transformation
	 *            Informationen zur Umrechnung von Spielraster-Punkten in
	 *            Bildschirmpixel
	 * @param type
	 *            Benutzerdefinierter Typ, Integer
	 * @param maxSize
	 *            maximale Breite und Höhe in Spielraster-Punkten
	 * @param imagefiles
	 *            Die Pfade der Bilder des Sprites
	 */
	public ImageSprite(Transformation transformation, int type, double maxSize, String... imagefiles) {
		super(transformation, type);
		Scr maxSizeScr = transformation().zoom(new Pos(maxSize, maxSize));
		maxSizeScr = new Scr(maxSizeScr.x() > 0 ? maxSizeScr.x() + 1 : 1, maxSizeScr.y() > 0 ? maxSizeScr.y() + 1 : 1);
		for (String file : imagefiles) {
			Image image = ImageLib.image(file, maxSizeScr.x());
			this.images.add(image);
			if (image.getWidth() > this.witdh) {
				this.witdh = (int) image.getWidth();
			}
			if (image.getHeight() > this.height) {
				this.height = (int) image.getHeight();
			}
		}
		this.actualImage = 0;
		this.size = transformation.zoom(new Scr(witdh, height));
		this.r((this.size.x() + this.size.y()) / 4f);
	}

	// ------------------------ API-Methoden --

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.dreierschach.daddel.gfx.sprite.Sprite#debug(boolean)
	 */
	public ImageSprite debug(Debug debug) {
		super.debug(debug);
		return this;
	}

	/**
	 * @return die Anzahl der Bilder des Sprite
	 */
	public int imageCount() {
		return images.size();
	}

	/**
	 * @return das aktuell anzuzeigende Bild
	 */
	public Image image() {
		return images.get(actualImage);
	}

	/**
	 * setzt das aktuell anzuzeigende Bild
	 * 
	 * @param actualImage
	 *            Index des Bilds
	 * @return this
	 */
	public ImageSprite actualImage(int actualImage) {
		if (actualImage < 0 || actualImage >= images.size()) {
			return this;
		}
		this.actualImage = actualImage;
		return this;
	}

	/**
	 * @return der Index des aktuell anzuzeigenden Images
	 */
	public int actualImage() {
		return actualImage;
	}

	/**
	 * @return die Größe des Sprites in Spielraster-Punkten
	 */
	public Pos size() {
		return size;
	}

	/**
	 * @return die Undurchsichtigkeit des Sprites (0 ... 1.0)
	 */
	public double alpha() {
		return alpha;
	}

	/**
	 * setzt die Undurchsichtigkeit des Sprites (0 ... 1.0)
	 * 
	 * @param alpha
	 *            die Undurchsichtigkeit des Sprites (0 ... 1.0)
	 * @return this
	 */
	public ImageSprite alpha(double alpha) {
		this.alpha = alpha;
		return this;
	}

	/**
	 * setzt die Aktion, die bei einer Kollision mit anderen Sprites ausgeführt
	 * werden soll
	 * 
	 * @param collisionListener
	 *            die Aktion bei Kollisionen
	 * @return this
	 */
	public ImageSprite collision(CollisionListener collisionListener) {
		this.collisionListener = collisionListener;
		return this;
	}

	// -------------- interne Methoden --

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.dreierschach.daddel.gfx.sprite.Sprite#draw(javafx.scene.canvas.
	 * GraphicsContext)
	 */
	@Override
	public void draw(GraphicsContext g) {
		if (!debug().wireframe()) {
			Scr scr = transformation().t(effektivePos());
			g.setGlobalAlpha(this.alpha);
			g.drawImage(image(), scr.x() - witdh / 2, scr.y() - height / 2);
		}
		if (debug().info()) {
			drawWireframe(g);
		}
		if (debug().wireframe()) {
			drawWireframe(g);
			drawInfo(g);
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
		g.setFill(Color.gray(0.5));
		g.setFont(Font.font(16));
		g.setTextAlign(TextAlignment.CENTER);
		g.setTextBaseline(VPos.CENTER);
		g.fillText(String.format("(%.3f / %.3f)", effektivePos().x(),
				effektivePos().y()), scr.x(), scr.y());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.dreierschach.daddel.gfx.sprite.Sprite#onCollision(de.dreierschach.daddel.
	 * gfx.sprite.Sprite)
	 */
	@Override
	public ImageSprite onCollision(Sprite other) {
		this.collisionListener.onCollision(this, other);
		return this;
	}

	// -------------- override methods to return correct type --

	/* (non-Javadoc)
	 * @see de.dreierschach.daddel.gfx.sprite.Sprite#pos(double, double)
	 */
	@Override
	public ImageSprite pos(double x, double y) {
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
	public ImageSprite pos(Pos pos) {
		super.pos(pos);
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.dreierschach.daddel.gfx.sprite.Sprite#rotation(double)
	 */
	@Override
	public ImageSprite rotation(double rotation) {
		super.rotation(rotation);
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.dreierschach.daddel.gfx.sprite.Sprite#rotate(double)
	 */
	@Override
	public ImageSprite rotate(double angle) {
		super.rotate(angle);
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.dreierschach.daddel.gfx.sprite.Sprite#direction(double)
	 */
	@Override
	public ImageSprite direction(double direction) {
		super.direction(direction);
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.dreierschach.daddel.gfx.sprite.Sprite#r(double)
	 */
	@Override
	public ImageSprite r(double r) {
		super.r(r);
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.dreierschach.daddel.gfx.sprite.Sprite#type(int)
	 */
	@Override
	public ImageSprite type(int type) {
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
	public ImageSprite gameLoop(SpriteGameLoop... gameLoops) {
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
	public ImageSprite parent(Sprite parent) {
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
	public ImageSprite move(Pos direction) {
		super.move(direction);
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.dreierschach.daddel.gfx.sprite.Sprite#move(double)
	 */
	@Override
	public ImageSprite move(double distance) {
		super.move(distance);
		return this;
	}
}
