package de.dreierschach.daddel.gfx.menu;

import de.dreierschach.daddel.Screen;
import de.dreierschach.daddel.gfx.text.TextSprite;
import de.dreierschach.daddel.listener.KeyListener;
import de.dreierschach.daddel.model.Pos;
import de.dreierschach.daddel.model.Transformation;
import javafx.geometry.VPos;
import javafx.scene.paint.Color;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

/**
 * Builder Klasse zur Erzeugung eines Menus basierend auf Text-Sprites
 * 
 * @author Christian
 *
 */
public class MenuBuilder {
	private Menu menu;
	private Screen screen;
	private Transformation transformation;

	private Pos pos = new Pos(0, 0);
	private float lineHeight = 1f;
	private TextAlignment align = TextAlignment.CENTER;
	private VPos valign = VPos.CENTER;

	private Color colorOn = Color.GREEN;
	private float sizeOn = 1;
	private String familyOn = "sans-serif";
	private FontWeight fontWeightOn = FontWeight.NORMAL;

	private Color colorOff = Color.WHITE;
	private float sizeOff = 1;
	private String familyOff = "sans-serif";
	private FontWeight fontWeightOff = FontWeight.NORMAL;

	/**
	 * @param transformation
	 *            Informationen zur Umrechnung von Bildpunkten in
	 *            Spielraster-Punkten
	 * @param screen
	 *            Referenz auf die Bildschirmanzeige
	 */
	public MenuBuilder(Transformation transformation, Screen screen) {
		this.transformation = transformation;
		this.screen = screen;
		menu = new Menu();
	}

	/**
	 * @return das fertige und im Screen verankerte Menu-Objekt
	 */
	public Menu create() {
		menu.registerKeyListeners(screen);
		return menu;
	}

	/**
	 * Fügt einen neuen Menupunkt hinzu
	 * 
	 * @param text
	 *            der Text des Menupunkts
	 * @param action
	 *            die Aktion, die ausgeführt werden soll, wenn der Menupunkt
	 *            ausgewählt wird
	 * @return this
	 */
	public MenuBuilder item(String text, KeyListener action) {
		if (menu.size() == 0) {
			TextSprite textSprite = new TextSprite(transformation, text).align(align, valign).color(colorOn)
					.family(familyOn).size(sizeOn).weight(fontWeightOn).type(-1).pos(pos);
			menu.addItem(textSprite, action);
			screen.addSprite(textSprite);
			return this;
		}
		TextSprite textSprite = new TextSprite(transformation, text).align(align, valign).color(colorOff)
				.family(familyOff).size(sizeOff).weight(fontWeightOff).type(-1)
				.pos(new Pos(pos.x(), pos.y() + lineHeight * (float) (menu.size())));
		menu.addItem(textSprite, action);
		screen.addSprite(textSprite);
		return this;
	}

	/**
	 * Legt die Position des Menus in Spielraster-Punkten fest
	 * 
	 * @param pos
	 *            die Position in Spielrasterpunkten
	 * @return this
	 */
	public MenuBuilder pos(Pos pos) {
		this.pos = pos;
		return this;
	}

	/**
	 * legt den Zeilenabstand zwischen den einzelnen Menupunkten in
	 * Spielraster-Punkten fest
	 * 
	 * @param lineHeight
	 *            Zeilenhöhe in Spielraster-Punkten
	 * @return this
	 */
	public MenuBuilder lineHeight(float lineHeight) {
		this.lineHeight = lineHeight;
		return this;
	}

	/**
	 * Legt die horizontale und vertikale Position der einzelnen Menupunkte
	 * innerhalb ihrer Zeile fest
	 * 
	 * @param align
	 *            die horizontale Position, z.B. TextAlignment.RIGHT
	 * @param valign
	 *            die vertikale Position, VPos.BASELINE
	 * @return this
	 */
	public MenuBuilder align(TextAlignment align, VPos valign) {
		this.align = align;
		this.valign = valign;
		return this;
	}

	/**
	 * Setzt die Vordergrundfarbe für aktive und inaktive Menupunkte
	 * 
	 * @param colorOn
	 *            Vordergrundfarbe für aktive Menupunkte
	 * @param colorOff
	 *            Vordergrundfarbe für inaktive Menupunkte
	 * @return this
	 */
	public MenuBuilder color(Color colorOn, Color colorOff) {
		menu.color(colorOn, colorOff);
		this.colorOn = colorOn;
		this.colorOff = colorOff;
		return this;
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
	public MenuBuilder size(float sizeOn, float sizeOff) {
		menu.size(sizeOn, sizeOff);
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
	public MenuBuilder family(String familyOn, String familyOff) {
		menu.family(familyOn, familyOff);
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
	public MenuBuilder weight(FontWeight fontWeightOn, FontWeight fontWeightOff) {
		menu.fontWeight(fontWeightOn, fontWeightOff);
		this.fontWeightOn = fontWeightOn;
		this.fontWeightOff = fontWeightOff;
		return this;
	}
}
