package de.dreierschach.daddel.listener;

import de.dreierschach.daddel.gfx.sprite.Sprite;
import de.dreierschach.daddel.gfx.tilemap.Entity;
import de.dreierschach.daddel.gfx.tilemap.TileMap;

/**
 * @author Christian
 *
 */
public interface SpriteMoveFinishedListener {
	/**
	 * wird aufgerufen wird, wenn eine Entity ihr Ziel erreicht
	 * 
	 * @param me
	 *            die Entity
	 * @param tilemap
	 *            das gekachtelte Spielfeld
	 */
	public void onDestinationReached(Sprite me);
}
