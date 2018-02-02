package de.dreierschach.daddel.gfx.tilemap;

public interface MoveFinishedListener {
	public void onDestinationReached(Entity me, TileMap tilemap);
}
