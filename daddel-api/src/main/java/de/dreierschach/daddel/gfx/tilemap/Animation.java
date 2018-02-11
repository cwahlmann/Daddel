package de.dreierschach.daddel.gfx.tilemap;

import de.dreierschach.daddel.gfx.sprite.ImageSprite;
import de.dreierschach.daddel.gfx.sprite.Sprite;
import de.dreierschach.daddel.model.SpriteGameLoop;

/**
 * Definiert die Bildanimation einer Entity
 * 
 * @author Christian
 *
 */
public class Animation implements SpriteGameLoop {
	private int imageStart;
	private int imageEnd;
	private boolean bounce;
	private double speed;

	public Animation() {
		super();
		this.imageStart = 0;
		this.imageEnd = 0;
		this.bounce = false;
		this.speed = 1f;
	}

	/**
	 * @return Index des ersten Bilds der Animation
	 */
	public int imageStart() {
		return imageStart;
	}

	/**
	 * Legt das erste Bild der Animation fest
	 * 
	 * @param imageStart
	 *            Index
	 * @return this
	 */
	public Animation imageStart(int imageStart) {
		this.imageStart = imageStart;
		return this;
	}

	/**
	 * @return Index des letzten Bilds der Animation
	 */
	public int imageEnd() {
		return imageEnd;
	}

	/**
	 * Legt das letzte Bild der Animation fest
	 * 
	 * @param imageEnd
	 *            Index
	 * @return this
	 */
	public Animation imageEnd(int imageEnd) {
		this.imageEnd = imageEnd;
		return this;
	}

	/**
	 * @return true: die Animation läuft in beiden Richtungen
	 */
	public boolean bounce() {
		return bounce;
	}

	/**
	 * Legt fest, ob die Animation in beider Richtungen laufen soll
	 * 
	 * @param bounce
	 *            true: Animation in beide Richtungen
	 * @return this
	 */
	public Animation bounce(boolean bounce) {
		this.bounce = bounce;
		return this;
	}

	/**
	 * @return die Geschwindigkeit der Animation in Bildern / s
	 */
	public double speed() {
		return speed;
	}

	/**
	 * Legt die Geschwindigkeit der Animation fest
	 * 
	 * @param speed
	 *            die Geschwindigkeit der Animation in Bildern / s
	 * @return this
	 */
	public Animation speed(double speed) {
		this.speed = speed;
		return this;
	}

	/* (non-Javadoc)
	 * @see de.dreierschach.daddel.model.SpriteGameLoop#run(de.dreierschach.daddel.gfx.sprite.Sprite, long, long)
	 */
	@Override
	public void run(Sprite sprite, long total, long deltatime) {
		if (imageEnd == imageStart) {
			((ImageSprite) sprite).actualImage(imageStart);
			return;
		}
		if (bounce) {
			int d = imageEnd - imageStart;
			int d2 = d * 2;
			int actual = imageStart + ((int) ((double) total / 1000f * speed * (double) d) % d);
			if (actual < d) {
				((ImageSprite) sprite).actualImage(actual);
			} else {
				((ImageSprite) sprite).actualImage(d2 - actual + 1);
			}
			return;
		}
		int d = imageEnd - imageStart + 1;
		((ImageSprite) sprite).actualImage(((int) (total / 1000f * speed * (double) d)) % d + imageStart);
	}
}
