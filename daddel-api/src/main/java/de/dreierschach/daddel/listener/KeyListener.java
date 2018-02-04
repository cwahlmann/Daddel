package de.dreierschach.daddel.listener;

import javafx.scene.input.KeyCode;

/**
 * @author Christian
 *
 */
public interface KeyListener {
	/**
	 * wird aufgerufen, wenn eine vorher festgelegte Taste gedrückt wird
	 * 
	 * @param keyCode
	 *            der KeyCode der Taste
	 */
	public void onKey(KeyCode keyCode);
}
