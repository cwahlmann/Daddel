package de.dreierschach.engine.gfx.menu;

import java.util.ArrayList;
import java.util.List;

import de.dreierschach.engine.Screen;
import de.dreierschach.engine.gfx.text.TextSprite;
import de.dreierschach.engine.listener.KeyListener;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.FontWeight;

public class Menu {
	private List<MenuItem> items = new ArrayList<>();
	private int actualItem = 0;
	private Color colorOff = Color.WHITE; 
	private Color colorOn = Color.GREEN;
	private float sizeOn = 1;
	private float sizeOff = 1;
	private FontWeight fontWeightOn = FontWeight.BOLD;
	private FontWeight fontWeightOff = FontWeight.BOLD;
	private String familyOn = "sans-serif";
	private String familyOff = "sans-serif";
	
	public void color(Color colorOn, Color colorOff) {
		this.colorOn = colorOn;
		this.colorOff = colorOff;
	}
	
	public Menu addItem(TextSprite text, KeyListener action) {
		this.items.add(new MenuItem(text, action));
		return this;
	}

	public Menu registerKeyListeners(Screen screen) {
		screen.addKeyListener(KeyCode.UP, () -> up());
		screen.addKeyListener(KeyCode.DOWN, () -> down());
		screen.addKeyListener(KeyCode.ENTER, () -> action());
		return this;
	}

	public int size() {
		return items.size();
	}

	public void size(float sizeOn, float sizeOff) {
		this.sizeOn = sizeOn;
		this.sizeOff = sizeOff;
	}
	
	public void family(String familyOn, String familyOff) {
		this.familyOn = familyOn;
		this.familyOff = familyOff;
	}
	
	public void fontWeight(FontWeight fontWeightOn, FontWeight fontWeightOff) {
		this.fontWeightOn = fontWeightOn;
		this.fontWeightOff = fontWeightOff;
	}
	
	private void on() {
		items.get(actualItem).getText().color(colorOn);
		items.get(actualItem).getText().family(familyOn);
		items.get(actualItem).getText().weight(fontWeightOn);
		items.get(actualItem).getText().size(sizeOn);		
	}
	
	private void off() {
		items.get(actualItem).getText().color(colorOff);
		items.get(actualItem).getText().family(familyOff);
		items.get(actualItem).getText().weight(fontWeightOff);
		items.get(actualItem).getText().size(sizeOff);		
	}

	private void up() {
		off();
		actualItem--;
		if (actualItem < 0) {
			actualItem = items.size() - 1;
		}
		on();
		items.get(actualItem).getText().color(colorOn);
	}

	private void down() {
		off();
		actualItem++;
		if (actualItem >= items.size()) {
			actualItem = 0;
		}
		on();
	}

	private void action() {
		items.get(actualItem).getAction().onKey();
	}
	
}
