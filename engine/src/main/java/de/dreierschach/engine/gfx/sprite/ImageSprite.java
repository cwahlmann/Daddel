package de.dreierschach.engine.gfx.sprite;

import java.util.ArrayList;
import java.util.List;

import de.dreierschach.engine.gfx.ImageLib;
import de.dreierschach.engine.listener.CollisionListener;
import de.dreierschach.engine.model.Pos;
import de.dreierschach.engine.model.Scr;
import de.dreierschach.engine.model.Transformation;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class ImageSprite extends Sprite  {

	private List<Image> images = new ArrayList<>();
	private int witdh = 0;
	private int height = 0;
	private Pos size;
	private int actualImage;
	private CollisionListener collisionListener = (me, other) -> {};
	private float alpha = 1f;
	
	public ImageSprite(Transformation transformation, int type, float maxSize, String... imagefiles) {
		super(transformation, type);
		Scr maxSizeScr = transformation().zoom(new Pos(maxSize, maxSize));
		maxSizeScr = new Scr(
				maxSizeScr.x() > 0 ? maxSizeScr.x()+1 : 1, 
				maxSizeScr.y() > 0 ? maxSizeScr.y()+1 : 1
				);
		for (String file: imagefiles) {
			Image image = ImageLib.image(file, maxSizeScr.x());
			this.images.add(image);
			if (image.getWidth() > this.witdh) {
				this.witdh = (int)image.getWidth();				
			}
			if (image.getHeight() > this.height) {
				this.height = (int)image.getHeight();				
			}
		}
		this.actualImage = 0;
		this.size = transformation.zoom(new Scr(witdh, height));
		this.r((this.size.x() + this.size.y())/4f);
	}

	public int imageCount() {
		return images.size();
	}
	
	public Image image() {
		return images.get(actualImage);
	}

	public ImageSprite actualImage(int actualImage) {
		if (actualImage < 0 || actualImage >= images.size()) {
			return this;
		}
		this.actualImage = actualImage;
		return this;
	}
	
	public int actualImage() {
		return actualImage;
	}
	
	public Pos size() {
		return size;
	}
	
	public float alpha() {
		return alpha;
	}
	
	public ImageSprite alpha(float alpha) {
		this.alpha = alpha;
		return this;
	}
	
	@Override
	public void draw(GraphicsContext g) {
		Scr scr = transformation().t(pos());
		g.setGlobalAlpha(this.alpha);
		g.drawImage(image(), scr.x()-witdh/2, scr.y()-height/2);
	}

	public ImageSprite collisionListener(CollisionListener collisionListener) {
		this.collisionListener = collisionListener;
		return this;
	}

	@Override
	public ImageSprite onCollision(Sprite other) {
		this.collisionListener.onCollision(this, other);
		return this;
	}

	// -------------- override methods to return correct type --

	@Override
	public ImageSprite relativePos(Pos pos) {
		super.relativePos(pos);
		return this;
	}

	@Override
	public ImageSprite rotation(double rotation) {
		super.rotation(rotation);
		return this;
	}

	@Override
	public ImageSprite rotate(double angle) {
		super.rotate(angle);
		return this;
	}

	@Override
	public ImageSprite direction(double direction) {
		super.direction(direction);
		return this;
	}

	@Override
	public ImageSprite r(float r) {
		super.r(r);
		return this;
	}

	@Override
	public ImageSprite type(int type) {
		super.type(type);
		return this;
	}

	@Override
	public ImageSprite gameLoop(SpriteGameLoop... gameLoops) {
		super.gameLoop(gameLoops);
		return this;
	}

	@Override
	public ImageSprite parent(Sprite parent) {
		super.parent(parent);
		return this;
	}

	@Override
	public ImageSprite move(Pos direction) {
		super.move(direction);
		return this;
	}

	@Override
	public ImageSprite move(float distance) {
		super.move(distance);
		return this;
	}	
}
