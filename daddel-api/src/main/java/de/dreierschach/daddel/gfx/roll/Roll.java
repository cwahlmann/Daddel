package de.dreierschach.daddel.gfx.roll;

import java.util.LinkedList;
import java.util.List;

import de.dreierschach.daddel.Screen;
import de.dreierschach.daddel.gfx.sprite.ImageSprite;
import de.dreierschach.daddel.gfx.sprite.Sprite;
import de.dreierschach.daddel.gfx.text.TextSprite;
import de.dreierschach.daddel.listener.RollDiesListener;
import de.dreierschach.daddel.model.Pos;
import de.dreierschach.daddel.model.Transformation;

public class Roll {

	private double speed = 1;
	private List<Sprite> sprites = new LinkedList<>();
	private Screen screen;
	private Transformation transformation;
	private long ticksNext = 0;
	private long ticks;
	private RollDiesListener rollDiesListener = roll -> {};
	private int activeSprites = 0;
	private int layer;
	
	public Roll(Screen screen, Transformation transformation, int layer) {
		this.layer = layer;
		this.screen = screen;
		this.transformation = transformation;
	}

	public double speed() {
		return speed;
	}
	
	public Roll speed(double speed) {
		this.speed = speed;
		return this;
	}
	
	public Roll onFinished(RollDiesListener rollDiesListener) {
		this.rollDiesListener = rollDiesListener;
		return this;
	}
	
	public void gameloop(long delta) {
		ticks += delta;
		if (!sprites.isEmpty()) {
			if (ticks >= ticksNext) {
				Sprite sprite = sprites.get(0);
				activeSprites++;
				double height = getHeight(sprite);
				ticksNext = ticks + (long) ((height / speed) * 1000);
				Pos startPos = new Pos(
						(transformation.getRasterRightBottom().x() - transformation.getRasterLeftUpper().x()) / 2d
								+ transformation.getRasterLeftUpper().x(),
						transformation.getRasterRightBottom().y() + height/2);
				sprite.gameLoop((sp, t, d) -> {
					double dy = speed * (double) d / 1000d;
					sp.move(new Pos(0, -dy));
					if (sp.effektivePos().y() < transformation.getRasterLeftUpper().y() - height) {
						sp.kill();
						activeSprites--;
						if (activeSprites == 0) {
							rollDiesListener.onDeath(this);
						}
					}
				}).pos(startPos);
				screen.addSprite(sprite);
				sprites.remove(0);
			}
		}
	}

	private double getHeight(Sprite sprite) {
		if (sprite instanceof TextSprite) {
			TextSprite textSprite = (TextSprite) sprite;
			return ((double) textSprite.lineCount()) * textSprite.size();
		}
		if (sprite instanceof ImageSprite) {
			ImageSprite imageSprite = (ImageSprite) sprite;
			return imageSprite.size().y();
		}
		return sprite.r() * 2;
	}

	public ImageSprite sprite(int maxSize, String...imagefiles) {
		ImageSprite sprite = new ImageSprite(transformation, -1, layer, maxSize, imagefiles);
		sprites.add(sprite);
		return sprite;
	}

	public TextSprite text(String text) {
		TextSprite textSprite = new TextSprite(transformation, text, layer);
		sprites.add(textSprite);
		return textSprite;
	}
}
