package de.dreierschach.daddel.listener;

import de.dreierschach.daddel.gfx.sprite.ParticleSwarm;

/**
 * @author Christian
 *
 */
public interface CreateSwarmHandler {
	/**
	 * Callback, das für die Generierung von Schwärmen benötigt wird
	 * 
	 * @param swarm
	 *            der erzeugte Schwarm
	 */
	void onCreate(ParticleSwarm swarm);
}
