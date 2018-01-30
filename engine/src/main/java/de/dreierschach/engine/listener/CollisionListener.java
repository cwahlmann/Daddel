package de.dreierschach.engine.listener;

import de.dreierschach.engine.gfx.sprite.Sprite;

public interface CollisionListener {
	void onCollision(Sprite me, Sprite other);
}
