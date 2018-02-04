package de.dreierschach.daddel.gfx.tilemap;

/**
 * @author Christian
 *
 */
public interface MoveFinishedListener {
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
