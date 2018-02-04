package de.dreierschach.daddel.listener;

import de.dreierschach.daddel.gfx.text.TextParticle;

/**
 * @author Christian
 *
 */
public interface TextParticleDiesListener {
	/**
	/**
	 * Aktion, die ausgeführt wird, wenn ein Text-Partikel stirbt
	 * 
	 * @param textParticle
	 *            der Text-Partikel
	 */
	void onDeath(TextParticle textParticle);
}
