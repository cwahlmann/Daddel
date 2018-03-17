package de.dreierschach.daddel.listener;

import de.dreierschach.daddel.gfx.sprite.Sprite;

/**
 * @author Christian
 *
 */
public interface CollisionListener {
	/**
	 * wird aufgerufen, wenn eine Kollision festgestellt wird
	 * 
	 * @param me
	 *            das Subjekt der Kollision
	 * @param other
	 *            das Objekt der Kollision
	 */
	void onCollision(Sprite me, Sprite other);
}
