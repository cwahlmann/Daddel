package de.dreierschach.daddel.gfx.menu;

import java.util.ArrayList;
import java.util.List;

import de.dreierschach.daddel.Screen;
import de.dreierschach.daddel.gfx.text.TextSprite;
import de.dreierschach.daddel.listener.KeyListener;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.FontWeight;

/**
 * Ein Menu, das mit Text-Sprites realisiert wird.
 * 
 * @author Christian
 *
 */
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

	/**
	 * Setzt die Vordergrundfarbe für aktive und inaktive Menupunkte
	 * 
	 * @param colorOn
	 *            Vordergrundfarbe für aktive Menupunkte
	 * @param colorOff
	 *            Vordergrundfarbe für inaktive Menupunkte
	 */
	public void color(Color colorOn, Color colorOff) {
		this.colorOn = colorOn;
		this.colorOff = colorOff;
	}

	/**
	 * Fügt einen weiteren Menupunkt hinzu
	 * 
	 * @param text
	 *            der Text-Sprite für diesen Menupunkt
	 * @param action
	 *            dir Aktion, die ausgeführt wird, wenn der Menupunkt ausgewählt
	 *            wird
	 * @return this
	 */
	public Menu addItem(TextSprite text, KeyListener action) {
		this.items.add(new MenuItem(text, action));
		return this;
	}

	/**
	 * installiert die Aktionen für die Tastenbefehle im Screen-Objekt
	 * 
	 * @param screen
	 *            das Screen-Objekt
	 * @return this
	 */
	public Menu registerKeyListeners(Screen screen) {
		screen.addKeyListener(KeyCode.UP, (keyCode) -> up());
		screen.addKeyListener(KeyCode.DOWN, (keyCode) -> down());
		screen.addKeyListener(KeyCode.ENTER, (keyCode) -> action(keyCode));
		return this;
	}

	/**
	 * @return die Anzahl Menupunkte
	 */
	public int size() {
		return items.size();
	}

	/**
	 * Legt die Größe (Höhe) der aktiven und inaktiven Menupunkte in
	 * Spielraster-Punkten fest
	 * 
	 * @param sizeOn
	 *            Größe des aktiven (Höhe) Menupunkts in Spielraster-Punkten
	 * @param sizeOff
	 *            Größe des inaktiven (Höhe) Menupunkts in Spielraster-Punkten
	 * @return this
	 */
	public Menu size(float sizeOn, float sizeOff) {
		this.sizeOn = sizeOn;
		this.sizeOff = sizeOff;
		return this;
	}

	/**
	 * Legt die Zeichensatz-Familie der aktiven und inaktiven Menupunkte fest
	 * 
	 * @param familyOn
	 *            Zeichensatz-Familie der aktiven Menupunkte, z.B. sans-serif
	 * @param familyOff
	 *            Zeichensatz-Familie der inaktiven Menupunkte, z.B. monospace
	 * @return this
	 */
	public Menu family(String familyOn, String familyOff) {
		this.familyOn = familyOn;
		this.familyOff = familyOff;
		return this;
	}

	/**
	 * Legt die Zeichensatz-Dicke der aktiven und inaktiven Menupunkte fest
	 * 
	 * @param fontWeightOn
	 *            Zeichensatz-Dicke der aktiven Menupunkte, z.B. FontWeight.BOLD
	 * @param fontWeightOff
	 *            Zeichensatz-Dicke der aktiven Menupunkte, z.B. FontWeight.THIN
	 * @return this
	 */
	public Menu fontWeight(FontWeight fontWeightOn, FontWeight fontWeightOff) {
		this.fontWeightOn = fontWeightOn;
		this.fontWeightOff = fontWeightOff;
		return this;
	}

	// private methoden

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

	private void action(KeyCode keyCode) {
		items.get(actualItem).getAction().onKey(keyCode);
	}

}
