package de.dreierschach.daddel.listener;

import de.dreierschach.daddel.model.Timeline;

/**
 * @author Christian
 *
 */
public interface TimelineDiesListener {
	/**
	 * Aktion, die ausgeführt wird, wenn ein Partikel stirbt
	 * 
	 * @param particle
	 *            der Partikel
	 */
	void onDeath(Timeline particle);
}
