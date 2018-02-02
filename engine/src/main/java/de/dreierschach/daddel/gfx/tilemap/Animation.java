package de.dreierschach.daddel.gfx.tilemap;

import de.dreierschach.daddel.gfx.sprite.ImageSprite;
import de.dreierschach.daddel.gfx.sprite.Sprite;
import de.dreierschach.daddel.gfx.sprite.SpriteGameLoop;

public class Animation implements SpriteGameLoop {
	private int imageStart;
	private int imageEnd;
	private boolean bounce;
	private float speed;

	public Animation() {
		super();
		this.imageStart = 0;
		this.imageEnd = 0;
		this.bounce = false;
		this.speed = 1f;
	}

	public int imageStart() {
		return imageStart;
	}

	public Animation imageStart(int imageStart) {
		this.imageStart = imageStart;
		return this;
	}

	public int imageEnd() {
		return imageEnd;
	}

	public Animation imageEnd(int imageEnd) {
		this.imageEnd = imageEnd;
		return this;
	}

	public boolean bounce() {
		return bounce;
	}

	public Animation bounce(boolean bounce) {
		this.bounce = bounce;
		return this;
	}

	public float speed() {
		return speed;
	}

	public Animation speed(float speed) {
		this.speed = speed;
		return this;
	}

	@Override
	public void run(Sprite sprite, long total, long deltatime) {
		if (imageEnd == imageStart) {
			((ImageSprite) sprite).actualImage(imageStart);
			return;
		}
		if (bounce) {
			int d = imageEnd - imageStart;
			int d2 = d * 2;
			int actual = imageStart + ((int) ((float) total / 1000f * speed * (float) d) % d);
			if (actual < d) {
				((ImageSprite) sprite).actualImage(actual);
			} else {
				((ImageSprite) sprite).actualImage(d2 - actual + 1);
			}
			return;
		}
		int d = imageEnd - imageStart + 1;
		((ImageSprite) sprite).actualImage(((int) (total / 1000f * speed * (float) d)) % d + imageStart);
	}
}
