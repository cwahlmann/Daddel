package de.dreierschach.daddel.gfx.menu;

import de.dreierschach.daddel.gfx.text.TextSprite;
import de.dreierschach.daddel.listener.KeyListener;

public class MenuItem {
	private TextSprite text;
	private KeyListener action;
	public MenuItem(TextSprite text, KeyListener action) {
		super();
		this.text = text;
		this.action = action;
	}
	public TextSprite getText() {
		return text;
	}
	public KeyListener getAction() {
		return action;
	}
}
