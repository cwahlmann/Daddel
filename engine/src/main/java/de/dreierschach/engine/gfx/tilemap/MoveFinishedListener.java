package de.dreierschach.engine.gfx.tilemap;

public interface MoveFinishedListener {
	public void onDestinationReached(Entity me, TileMap tilemap);
}
