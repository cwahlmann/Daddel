package de.dreierschach.daddel.gfx.text;

import de.dreierschach.daddel.gfx.sprite.Sprite;
import de.dreierschach.daddel.model.Pos;
import de.dreierschach.daddel.model.Scr;
import de.dreierschach.daddel.model.SpriteGameLoop;
import de.dreierschach.daddel.model.Transformation;
import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

public class TextSprite extends Sprite {

	private Color color = Color.WHITE;
	private String text;
	private double size = 12;
	private String family = "sans-serif";
	private FontWeight fontWeight = FontWeight.NORMAL;
	private TextAlignment align = TextAlignment.CENTER;
	private VPos valign = VPos.CENTER;
	
	public TextSprite(Transformation transformation, String text) {
		super(transformation, 0);
		this.text = text;
	}

	public TextSprite align(TextAlignment align, VPos valign) {
		this.align = align;
		this.valign = valign;
		return this;
	}
	
	public TextSprite color(Color color) {
		this.color = color;
		return this;
	}
	
	public Color getColor() {
		return color;
	}
	
	public TextSprite text(String text) {
		this.text = text;
		return this;
	}
	
	public String getText() {
		return text;
	}
	
	public TextSprite size(double size) {
		this.size = size;
		return this;
	}
	
	public double getSize() {
		return size;
	}
	
	public TextSprite family(String family) {
		this.family = family;
		return this;
	}
	
	public String getFamily() {
		return family;
	}
	
	public TextSprite weight(FontWeight fontWeight) {
		this.fontWeight = fontWeight;
		return this;
	}
	
	public FontWeight getWeight() {
		return fontWeight;
	}
	
	@Override
	public void draw(GraphicsContext g) {
		double newSize = transformation().zoom((float)size); 
		Font font = Font.font(family, fontWeight, newSize);
		g.setTextAlign(align);
		g.setTextBaseline(valign);
		g.setFont(font);
		g.setFill(color);
		Scr scr = transformation().t(pos());
		g.fillText(text, scr.x(), scr.y());
	}

	@Override
	public TextSprite onCollision(Sprite other) {
		return this;
	}
	
	// overwrite methods for correct return type

	@Override
	public TextSprite relativePos(Pos pos) {
		super.relativePos(pos);
		return this;
	}

	@Override
	public TextSprite rotation(double rotation) {
		super.rotation(rotation);
		return this;
	}

	@Override
	public TextSprite rotate(double angle) {
		super.rotate(angle);
		return this;
	}

	@Override
	public TextSprite direction(double direction) {
		super.direction(direction);
		return this;
	}

	@Override
	public TextSprite r(float r) {
		super.r(r);
		return this;
	}

	@Override
	public TextSprite type(int type) {
		super.type(type);
		return this;
	}

	@Override
	public TextSprite gameLoop(SpriteGameLoop... gameLoops) {
		super.gameLoop(gameLoops);
		return this;
	}

	@Override
	public TextSprite move(Pos direction) {
		super.move(direction);
		return this;
	}

	@Override
	public TextSprite move(float distance) {
		super.move(distance);
		return this;
	}

	@Override
	public TextSprite parent(Sprite parent) {
		super.parent(parent);
		return this;
	}
}
