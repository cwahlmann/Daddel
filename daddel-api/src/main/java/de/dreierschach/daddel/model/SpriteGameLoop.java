package de.dreierschach.daddel.model;

import de.dreierschach.daddel.gfx.sprite.Sprite;

public interface SpriteGameLoop {
	void run(Sprite sprite, long ticks, long deltatime);
}
