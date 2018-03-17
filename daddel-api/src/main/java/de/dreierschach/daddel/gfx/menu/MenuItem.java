package de.dreierschach.daddel.gfx.menu;

import de.dreierschach.daddel.gfx.text.TextSprite;
import de.dreierschach.daddel.listener.KeyListener;

/**
 * ein einzelner Menupunkt
 * 
 * @author Christian
 *
 */
public class MenuItem {
	private TextSprite text;
	private KeyListener action;

	/**
	 * @param text
	 *            der Text-Sprite zur Anzeige des Menupunkts
	 * @param action
	 *            die auszuführende Aktion bei Auswahl der Menupunkts
	 */
	public MenuItem(TextSprite text, KeyListener action) {
		super();
		this.text = text;
		this.action = action;
	}

	/**
	 * @return der Text-Sprite zur Anzeige des Menupunkts
	 */
	public TextSprite getText() {
		return text;
	}

	/**
	 * @return die auszuführende Aktion bei Auswahl der Menupunkts
	 */
	public KeyListener getAction() {
		return action;
	}
}
