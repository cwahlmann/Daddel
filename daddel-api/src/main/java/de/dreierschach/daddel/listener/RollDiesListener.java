package de.dreierschach.daddel.listener;

import de.dreierschach.daddel.gfx.roll.Roll;

/**
 * @author Christian
 *
 */
public interface RollDiesListener {
	/**
	/**
	 * Aktion, die ausgeführt wird, wenn eine Rolle stirbt
	 * 
	 * @param roll
	 *            der Rolle
	 */
	void onDeath(Roll roll);
}
