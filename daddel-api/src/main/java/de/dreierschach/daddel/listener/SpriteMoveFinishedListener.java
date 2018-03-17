package de.dreierschach.daddel.listener;

import de.dreierschach.daddel.gfx.sprite.Sprite;

/**
 * @author Christian
 *
 */
public interface SpriteMoveFinishedListener {
	/**
	 * wird aufgerufen wird, wenn ein Sprite sein Ziel erreicht
	 * 
	 * @param me
	 *            der Sprite
	 */
	public void onDestinationReached(Sprite me);
}
