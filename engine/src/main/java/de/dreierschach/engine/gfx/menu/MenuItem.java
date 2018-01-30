package de.dreierschach.engine.gfx.menu;

import de.dreierschach.engine.gfx.text.TextSprite;
import de.dreierschach.engine.listener.KeyListener;

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
