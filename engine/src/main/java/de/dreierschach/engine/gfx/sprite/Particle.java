package de.dreierschach.engine.gfx.sprite;

import de.dreierschach.engine.listener.CollisionListener;
import de.dreierschach.engine.model.EndOfLifeStrategy;
import de.dreierschach.engine.model.OutsideGridStrategy;
import de.dreierschach.engine.model.Pos;
import de.dreierschach.engine.model.Transformation;

public class Particle extends ImageSprite {

	private Pos initialPos = new Pos(0, 0);
	private long lifespan = 1000L;
	private double rotationStart = 0f;
	private double rotationEnd = 0f;
	private double directionStart = 0f;
	private double directionEnd = 0f;
	private float alphaStart = 1f;
	private float alphaEnd = 1f;
	private float speed = 0f;
	private float speedStart = 0f;
	private float speedEnd = 0f;
	private float speedAnimation = 10f; // bilder/s
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
		super.alpha((alphaEnd - alphaStart) * (float)factor + alphaStart);
		actualImage(((int) (gesamtZeit * speedAnimation / 1000)) % imageCount());
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
					super.relativePos(new Pos(pos().x() - min.x() + max.x(), pos().y()));
				} else if (this.pos().x() > max.x()) {
					super.relativePos(new Pos(pos().x() - max.x() + min.x(), pos().y()));
				}

				if (this.pos().y() < min.y()) {
					super.relativePos(new Pos(pos().x(), pos().y() - min.y() + max.y()));
				} else if (this.pos().y() > max.y()) {
					super.relativePos(new Pos(pos().x(), pos().y() - max.y() + min.y()));
				}
				break;
			case restart:
				super.relativePos(initialPos);
				setTicks(0);
				break;
			case ignore:
			}
		}

	};

	public Particle(Transformation transformation, int typ, float groesse, long lifespan, String... bilder) {
		super(transformation, typ, groesse, bilder);
		this.lifespan = lifespan;
		this.rotationStart = rotation();
		this.rotationEnd = rotation();
		this.gameLoop(particleGameLoop);
	}

	public Particle rotation(double rotationStart, double rotationEnde) {
		this.rotationStart = rotationStart;
		this.rotationEnd = rotationEnde;
		return this;
	}

	public Particle direction(double directionStart, double directionEnd) {
		this.directionStart = directionStart;
		this.directionEnd = directionEnd;
		return this;
	}

	@Override
	public Particle direction(double direction) {
		direction(direction, direction);
		return this;
	}

	public Particle alpha(float alphaStart, float alphaEnd) {
		this.alphaStart = alphaStart;
		this.alphaEnd = alphaEnd;
		super.alpha(alphaStart);
		return this;
	}

	public Particle alpha(float alpha) {
		this.alphaStart = alpha;
		this.alphaEnd = alpha;
		super.alpha(alpha);
		return this;
	}

	public Particle speed(float speedStart, float speedEnd) {
		this.speedStart = speedStart;
		this.speedEnd = speedEnd;
		this.speed = speedStart;
		return this;
	}

	public Particle speed(float speed) {
		this.speedStart = speed;
		this.speedEnd = speed;
		this.speed = speed;
		return this;
	}

	public Particle speedAnimation(float speedAnimation) {
		this.speedAnimation = speedAnimation;
		return this;
	}

	public Particle outsideRasterStrategy(OutsideGridStrategy outsideRasterStrategy) {
		this.outsideGridStrategy = outsideRasterStrategy;
		return this;
	}

	public Particle endOfLifeStrategy(EndOfLifeStrategy endOfLifeStrategy) {
		this.endOfLifeStrategy = endOfLifeStrategy;
		return this;
	}

	@Override
	public Particle relativePos(Pos pos) {
		this.initialPos = pos;
		super.relativePos(pos);
		return this;
	}
	
	// override methods to return correct type

	@Override
	public Particle rotation(double rotation) {
		super.rotation(rotation);
		return this;
	}

	@Override
	public Particle rotate(double angle) {
		super.rotate(angle);
		return this;
	}

	@Override
	public Particle r(float r) {
		super.r(r);
		return this;
	}

	@Override
	public Particle type(int type) {
		super.type(type);
		return this;
	}

	@Override
	public Particle gameLoop(SpriteGameLoop... gameLoops) {
		super.gameLoop(gameLoops);
		return this;
	}

	@Override
	public Particle parent(Sprite parent) {
		super.parent(parent);
		return this;
	}

	@Override
	public Particle move(Pos direction) {
		super.move(direction);
		return this;
	}

	@Override
	public Particle move(float distance) {
		super.move(distance);
		return this;
	}

	@Override
	public Particle actualImage(int actualImage) {
		super.actualImage(actualImage);
		return this;
	}

	@Override
	public Particle collisionListener(CollisionListener collisionListener) {
		super.collisionListener(collisionListener);
		return this;
	}

	@Override
	public Particle onCollision(Sprite other) {
		super.onCollision(other);
		return this;
	}		
}
