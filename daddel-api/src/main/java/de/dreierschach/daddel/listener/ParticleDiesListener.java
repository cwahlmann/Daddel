package de.dreierschach.daddel.listener;

import de.dreierschach.daddel.gfx.sprite.Particle;

/**
 * @author Christian
 *
 */
public interface ParticleDiesListener {
	/**
	 * Aktion, die ausgeführt wird, wenn ein Partikel stirbt
	 * 
	 * @param particle
	 *            der Partikel
	 */
	void onDeath(Particle particle);
}
