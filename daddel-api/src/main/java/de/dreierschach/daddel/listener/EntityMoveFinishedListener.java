package de.dreierschach.daddel.listener;

import de.dreierschach.daddel.gfx.tilemap.Entity;
import de.dreierschach.daddel.gfx.tilemap.TileMap;

/**
 * @author Christian
 *
 */
public interface EntityMoveFinishedListener {
	/**
	 * wird aufgerufen wird, wenn eine Entity ihr Ziel erreicht
	 * 
	 * @param me
	 *            die Entity
	 * @param tilemap
	 *            das gekachtelte Spielfeld
	 */
	public void onDestinationReached(Entity me, TileMap tilemap);
}
