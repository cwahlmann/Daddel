package de.dreierschach.daddel.gfx.text;

import com.sun.javafx.tk.FontMetrics;
import com.sun.javafx.tk.Toolkit;

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
import javafx.scene.transform.Affine;

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

	public String text() {
		return text;
	}

	public TextSprite size(double size) {
		this.size = size;
		return this;
	}

	public double size() {
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
		double newSize = transformation().zoom((double) size);
		Font font = Font.font(family, fontWeight, newSize);
		g.setTextAlign(align);
		g.setTextBaseline(valign);
		g.setFont(font);
		g.setFill(color);
		Scr scr = transformation().t(effektivePos());
		Scr textSize = textSize(g, text());
		switch (valign) {
		case TOP: break;
		case BASELINE:
		case BOTTOM:
			scr.add(new Scr(0, -textSize.y()));
			break;
		case CENTER:
			scr.add(new Scr(0, -textSize.y()/2));			
		}
		if (!debug().wireframe()) {
			g.fillText(text, scr.x(), scr.y());
		}
		if (debug().info()) {
			drawWireframe(g);
		} else if (debug().wireframe()) {
			drawWireframe(g);
			if (showPosOnDebug()) {
				drawInfo(g);
			}
		}
	}

	private void drawWireframe(GraphicsContext g) {
		Scr scr = transformation().t(effektivePos());
		g.setGlobalAlpha(1.0);
		g.setStroke(Color.gray(0.5));
		Scr size = textSize(g, text);
		int offsetX;
		switch (this.align) {
		case LEFT:
			offsetX = 0;
			break;
		case RIGHT:
			offsetX = -size.x();
			break;
		case CENTER:
		default:
			offsetX = -size.x() / 2;
		}
		int offsetY;
		switch (this.valign) {
		case CENTER:
			offsetY = -size.y() / 2;
			break;
		case BOTTOM:
			offsetY = -size.y();
			break;
		case TOP:
		default:
			offsetY = 0;
		}
		g.strokeRect(scr.x() + offsetX, scr.y() + offsetY, size.x(), size.y());
	}

	private void drawInfo(GraphicsContext g) {
		Scr scr = transformation().t(effektivePos());
		g.setTransform(new Affine());
		g.setFill(Color.gray(0.5));
		g.setFont(Font.font(16));
		g.setTextAlign(TextAlignment.CENTER);
		g.setTextBaseline(VPos.CENTER);
		g.fillText(String.format("(%.3f / %.3f)", effektivePos().x(), effektivePos().y()), scr.x(), scr.y());
	}

	public int lines() {
		return text.split("\n").length;
	}
	
	private Scr textSize(GraphicsContext g, String text) {
		FontMetrics fm = Toolkit.getToolkit().getFontLoader().getFontMetrics(g.getFont());
		String[] lines = text.split("\n");
		double textWidth = 0;
		for (String line : lines) {
			double w = fm.computeStringWidth(line);
			if (w > textWidth) {
				textWidth = w;
			}
		}
		double textHeight = fm.getLineHeight() * lines.length;
		return new Scr((int) textWidth, (int) textHeight);
	}

	@Override
	public TextSprite onCollision(Sprite other) {
		return this;
	}

	// overwrite methods for correct return type

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.dreierschach.daddel.gfx.sprite.Sprite#pos(double, double)
	 */
	@Override
	public TextSprite pos(double x, double y) {
		// TODO Auto-generated method stub
		super.pos(x, y);
		return this;
	}

	@Override
	public TextSprite pos(Pos pos) {
		super.pos(pos);
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
	public TextSprite r(double r) {
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
	public TextSprite move(double distance) {
		super.move(distance);
		return this;
	}

	@Override
	public TextSprite parent(Sprite parent) {
		super.parent(parent);
		return this;
	}
}
