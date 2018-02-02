package de.dreierschach.daddel.listener;

import de.dreierschach.daddel.gfx.sprite.Sprite;

public interface CollisionListener {
	void onCollision(Sprite me, Sprite other);
}
