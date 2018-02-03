package de.dreierschach.daddel.gfx.text;

import de.dreierschach.daddel.gfx.sprite.Sprite;
import de.dreierschach.daddel.gfx.sprite.SpriteGameLoop;
import de.dreierschach.daddel.model.EndOfLifeStrategy;
import de.dreierschach.daddel.model.OutsideGridStrategy;
import de.dreierschach.daddel.model.Pos;
import de.dreierschach.daddel.model.Transformation;
import javafx.geometry.VPos;
import javafx.scene.paint.Color;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

public class TextParticle extends TextSprite {

	private long lifespan = 1000L;
	private double rotationStart = 0f;
	private double rotationEnd = 0f;
	private double directionStart = 0f;
	private double directionEnd = 0f;
	private float speed = 0f;
	private float speedStart = 0f;
	private float speedEnd = 0f;
	private float sizeStart = 2f;
	private float sizeEnd = 2f;
	private OutsideGridStrategy outsideGridStrategy = OutsideGridStrategy.kill;
	private EndOfLifeStrategy endOfLifeStrategy = EndOfLifeStrategy.die;

	private SpriteGameLoop particleGameLoop = (partikel, gesamtZeit, deltaZeit) -> {
		if (lifespan > 0 && gesamtZeit >= lifespan) {
			switch (endOfLifeStrategy) {
			case stop:
				return;
			case die:
				this.kill();
				return;
			case restart:
				setTicks(getTicks() - lifespan);
				break;
			case bounce:
				// TODO
				break;
			case ignore:
			}
		}
		double factor = lifespan == 0 ? 0 : (double) (gesamtZeit) / (double) (lifespan);
		this.speed = (float) ((speedEnd - speedStart) * factor + speedStart);
		super.rotation((rotationEnd - rotationStart) * factor + rotationStart);
		super.direction((directionEnd - directionStart) * factor + directionStart);
		super.size((sizeEnd - sizeStart) * factor + sizeStart);
		move(deltaZeit * speed / 1000);
		Pos min = transformation().getRasterLeftUpper();
		Pos max = transformation().getRasterRightBottom();
		if (this.pos().x() < min.x() || this.pos().x() > max.x() || this.pos().y() < min.y()
				|| this.pos().y() > max.y()) {
			switch (outsideGridStrategy) {
			case kill:
				kill();
				break;
			case bounce:
				// TODO
				break;
			case reappear:
				if (this.pos().x() < min.x()) {
					this.relativePos(new Pos(pos().x() - min.x() + max.x(), pos().y()));
				} else if (this.pos().x() > max.x()) {
					this.relativePos(new Pos(pos().x() - max.x() + min.x(), pos().y()));
				}

				if (this.pos().y() < min.y()) {
					this.relativePos(new Pos(pos().x(), pos().y() - min.y() + max.y()));
				} else if (this.pos().y() > max.y()) {
					this.relativePos(new Pos(pos().x(), pos().y() - max.y() + min.y()));
				}
				break;
			case restart:
				setTicks(0);
			case ignore:
			}
		}

	};

	public TextParticle(Transformation transformation, long lifespan, String text) {
		super(transformation, text);
		this.lifespan = lifespan;
		this.rotationStart = rotation();
		this.rotationEnd = rotation();
		this.gameLoop(particleGameLoop);
	}

	public TextParticle rotation(double rotationStart, double rotationEnde) {
		this.rotationStart = rotationStart;
		this.rotationEnd = rotationEnde;
		return this;
	}

	@Override
	public TextParticle rotation(double rotation) {
		rotation(rotation, rotation);
		return this;
	}

	public TextParticle direction(double directionStart, double directionEnd) {
		this.directionStart = directionStart;
		this.directionEnd = directionEnd;
		return this;
	}

	@Override
	public TextParticle direction(double direction) {
		direction(direction, direction);
		return this;
	}
	
	public TextParticle speed(float speedStart, float speedEnd) {
		this.speedStart = speedStart;
		this.speedEnd = speedEnd;
		this.speed = speedStart;
		return this;
	}

	public TextParticle speed(float speed) {
		this.speedStart = speed;
		this.speedEnd = speed;
		this.speed = speed;
		return this;
	}

	public TextParticle size(float sizeStart, float sizeEnd) {
		this.sizeStart = sizeStart;
		this.sizeEnd = sizeEnd;
		return this;
	}

	@Override
	public TextParticle size(double size) {
		size((float)size, (float)size);
		return this;
	}
	
	public TextParticle outsideRasterStrategy(OutsideGridStrategy outsideRasterStrategy) {
		this.outsideGridStrategy = outsideRasterStrategy;
		return this;
	}
	
	public TextParticle endOfLifeStrategy(EndOfLifeStrategy endOfLifeStrategy) {
		this.endOfLifeStrategy = endOfLifeStrategy;
		return this;
	}
	
	// overwrite methods for correct return type

	@Override
	public TextParticle relativePos(Pos pos) {
		super.relativePos(pos);
		return this;
	}

	@Override
	public TextParticle rotate(double angle) {
		super.rotate(angle);
		return this;
	}

	@Override
	public TextParticle r(float r) {
		super.r(r);
		return this;
	}

	@Override
	public TextParticle type(int type) {
		super.type(type);
		return this;
	}

	@Override
	public TextParticle gameLoop(SpriteGameLoop... gameLoops) {
		super.gameLoop(gameLoops);
		return this;
	}

	@Override
	public TextParticle move(Pos direction) {
		super.move(direction);
		return this;
	}

	@Override
	public TextParticle move(float distance) {
		super.move(distance);
		return this;
	}

	@Override
	public TextParticle align(TextAlignment align, VPos valign) {
		super.align(align, valign);
		return this;
	}

	@Override
	public TextParticle color(Color color) {
		super.color(color);
		return this;
	}

	@Override
	public TextParticle text(String text) {
		super.text(text);
		return this;
	}

	@Override
	public TextParticle family(String family) {
		super.family(family);
		return this;
	}

	@Override
	public TextParticle weight(FontWeight fontWeight) {
		super.weight(fontWeight);
		return this;
	}

	@Override
	public TextParticle onCollision(Sprite other) {
		super.onCollision(other);
		return this;
	}

	@Override
	public TextParticle parent(Sprite parent) {
		super.parent(parent);
		return this;
	}	
}
