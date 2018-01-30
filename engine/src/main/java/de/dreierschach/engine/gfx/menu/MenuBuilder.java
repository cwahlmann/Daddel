package de.dreierschach.engine.gfx.menu;

import de.dreierschach.engine.Screen;
import de.dreierschach.engine.gfx.text.TextSprite;
import de.dreierschach.engine.listener.KeyListener;
import de.dreierschach.engine.model.Pos;
import de.dreierschach.engine.model.Transformation;
import javafx.geometry.VPos;
import javafx.scene.paint.Color;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

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

	public MenuBuilder(Transformation transformation, Screen screen) {
		this.transformation = transformation;
		this.screen = screen;
		menu = new Menu();
	}

	
	public Menu create() {
		menu.registerKeyListeners(screen);
		return menu;
	}
	
	public MenuBuilder item(String text, KeyListener action) {
		if (menu.size() == 0) {
			TextSprite textSprite = new TextSprite(transformation, text)
					.align(align, valign)
					.color(colorOn)
					.family(familyOn)
					.size(sizeOn)
					.weight(fontWeightOn)
					.type(-1)
					.relativePos(pos);
			menu.addItem(textSprite, action);
			screen.addSprite(textSprite);
			return this;
		}
		TextSprite textSprite = new TextSprite(transformation, text)
				.align(align, valign)
				.color(colorOff)
				.family(familyOff)
				.size(sizeOff)
				.weight(fontWeightOff)
				.type(-1)
				.relativePos(new Pos(pos.x(), pos.y() + lineHeight * (float)(menu.size())));
		menu.addItem(textSprite, action);
		screen.addSprite(textSprite);
		return this;
	}

	public MenuBuilder pos(Pos pos) {
		this.pos = pos;
		return this;
	}

	public MenuBuilder lineHeight(float lineHeight) {
		this.lineHeight = lineHeight;
		return this;
	}

	public MenuBuilder align(TextAlignment align, VPos valign) {
		this.align = align;
		this.valign = valign;
		return this;
	}
	
	public MenuBuilder color(Color colorOn, Color colorOff) {
		menu.color(colorOn, colorOff);
		this.colorOn = colorOn;
		this.colorOff = colorOff;
		return this;
	}
	
	public MenuBuilder size(float sizeOn, float sizeOff) {
		menu.size(sizeOn, sizeOff);
		this.sizeOn = sizeOn;
		this.sizeOff = sizeOff;
		return this;
	}
	
	public MenuBuilder family(String familyOn, String familyOff) {
		menu.family(familyOn, familyOff);
		this.familyOn = familyOn;
		this.familyOff = familyOff;
		return this;
	}
	
	public MenuBuilder weight(FontWeight fontWeightOn, FontWeight fontWeightOff) {
		menu.fontWeight(fontWeightOn, fontWeightOff);
		this.fontWeightOn = fontWeightOn;
		this.fontWeightOff = fontWeightOff;
		return this;
	}
}
