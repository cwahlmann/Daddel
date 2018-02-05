package de.dreierschach.daddel.gfx.sprite;

import de.dreierschach.daddel.listener.CollisionListener;
import de.dreierschach.daddel.listener.ParticleDiesListener;
import de.dreierschach.daddel.model.EndOfLifeStrategy;
import de.dreierschach.daddel.model.OutsideGridStrategy;
import de.dreierschach.daddel.model.Pos;
import de.dreierschach.daddel.model.SpriteGameLoop;
import de.dreierschach.daddel.model.Transformation;

/**
 * ein Partikel ist ein Sprite, der eine definierte Lebensdauer und ein
 * vorgegebenes Verhalten hat
 * 
 * @author Christian
 *
 */
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
	private ParticleDiesListener particleDiesListener = particle -> {
	};

	/**
	 * @param transformation
	 *            Informationen zur Umrechnung von Spielraster-Punkten in
	 *            Bildschirmpixel
	 * @param typ
	 *            Benutzerdefinierter Typ, Integer
	 * @param groesse
	 *            maximale Breite und Höhe in Spielraster-Punkten
	 * @param lifespan
	 *            Lebensdauer des Patikel in ms
	 * @param bilder
	 *            Die Pfade der Bilder des Sprites
	 */
	public Particle(Transformation transformation, int typ, float groesse, long lifespan, String... bilder) {
		super(transformation, typ, groesse, bilder);
		this.lifespan = lifespan;
		this.rotationStart = rotation();
		this.rotationEnd = rotation();
		this.gameLoop(particleGameLoop);
	}

	/**
	 * Die Drehung des Partikel am Anfang und am Ende seiner Lebensdauer
	 * 
	 * @param rotationStart
	 *            Winkel am Anfang der Lebensdauer (0 ... 360)
	 * @param rotationEnde
	 *            Winkel am Ende der Lebensdauer (0 ... 360)
	 * @return this
	 */
	public Particle rotation(double rotationStart, double rotationEnde) {
		this.rotationStart = rotationStart;
		this.rotationEnd = rotationEnde;
		return this;
	}

	/**
	 * Die Ausrichtung der Bewegung des Partikel am Anfang und am Ende seiner
	 * Lebensdauer
	 * 
	 * @param directionStart
	 *            Winkel am Anfang der Lebensdauer (0 ... 360)
	 * @param directionEnd
	 *            Winkel am Ende der Lebensdauer (0 ... 360)
	 * @return this
	 */
	public Particle direction(double directionStart, double directionEnd) {
		this.directionStart = directionStart;
		this.directionEnd = directionEnd;
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.dreierschach.daddel.gfx.sprite.ImageSprite#direction(double)
	 */
	@Override
	public Particle direction(double direction) {
		direction(direction, direction);
		return this;
	}

	/**
	 * Die Undurchsichtigkeit des Partikel am Anfang und am Ende seiner Lebensdauer
	 * 
	 * @param alphaStart
	 *            Undurchsichtigkeit am Anfang der Lebensdauer (0 .. 1.0)
	 * @param alphaEnd
	 *            Undurchsichtigkeit am Ende der Lebensdauer (0 .. 1.0)
	 * @return this
	 */
	public Particle alpha(float alphaStart, float alphaEnd) {
		this.alphaStart = alphaStart;
		this.alphaEnd = alphaEnd;
		super.alpha(alphaStart);
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.dreierschach.daddel.gfx.sprite.ImageSprite#alpha(float)
	 */
	@Override
	public Particle alpha(float alpha) {
		this.alphaStart = alpha;
		this.alphaEnd = alpha;
		super.alpha(alpha);
		return this;
	}

	/**
	 * Die Geschwindigkeit des Partikel am Anfang und am Ende seiner Lebensdauer
	 * 
	 * @param speedStart
	 *            Anfangsgeschwindigkeit in Spielraster-Punkten / s (0 ...)
	 * @param speedEnd
	 *            Endgeschwindigkeit in Spielraster-Punkten / s (0 ...)
	 * @return this
	 */
	public Particle speed(float speedStart, float speedEnd) {
		this.speedStart = speedStart;
		this.speedEnd = speedEnd;
		this.speed = speedStart;
		return this;
	}

	/**
	 * Setzt die Geschwindigkeit des Partikel für seine gesamte Lebensdauer
	 * 
	 * @param speed
	 *            Geschwindigkeit in Spielraster-Punkten / s (0 ...)
	 * @return this
	 */
	public Particle speed(float speed) {
		this.speedStart = speed;
		this.speedEnd = speed;
		this.speed = speed;
		return this;
	}

	/**
	 * Setzt die Geschwindigkeit der Animation des Partikel
	 * 
	 * @param speedAnimation
	 *            Geschwindigkeit in Bildern / s
	 * @return this
	 */
	public Particle speedAnimation(float speedAnimation) {
		this.speedAnimation = speedAnimation;
		return this;
	}

	/**
	 * Legt fest, was mit dem Partikel geschehen soll, wenn er das Spielraster
	 * verlässt
	 * 
	 * @param outsideRasterStrategy
	 *            mögliche Strategien sind: ignorieren (ignore), Partikel töten
	 *            (kill), zurücktitschen (bounce), auf der gegenüberliegenden Seite
	 *            erscheinen (reappear), Partikel neu starten (restart)
	 * @return this
	 */
	public Particle outsideRasterStrategy(OutsideGridStrategy outsideRasterStrategy) {
		this.outsideGridStrategy = outsideRasterStrategy;
		return this;
	}

	/**
	 * Legt fest, was mit dem Partikel am Ende seiner Lebensdauer geschehen soll
	 * 
	 * @param endOfLifeStrategy
	 *            mögliche Strategien sind: ignorieren (ignore), töten (die),
	 *            einfrieren (stop), zurücktitschen (bounce), Partikel neu starten
	 *            (restart)
	 * @return this
	 */
	public Particle endOfLifeStrategy(EndOfLifeStrategy endOfLifeStrategy) {
		this.endOfLifeStrategy = endOfLifeStrategy;
		return this;
	}

	/**
	 * Aktion, die ausgeführt wird, wenn der Partikel stirbt
	 * 
	 * @param particleDiesListener
	 *            Aktion
	 * @return this
	 */
	public Particle onDeath(ParticleDiesListener particleDiesListener) {
		this.particleDiesListener = particleDiesListener;
		return this;
	}

	// ------------------ private methods

	private SpriteGameLoop particleGameLoop = (partikel, gesamtZeit, deltaZeit) -> {
		if (lifespan > 0 && gesamtZeit >= lifespan) {
			switch (endOfLifeStrategy) {
			case stop:
				return;
			case die:
				this.kill();
				particleDiesListener.onDeath(this);
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
		super.alpha((alphaEnd - alphaStart) * (float) factor + alphaStart);
		actualImage(((int) (gesamtZeit * speedAnimation / 1000)) % imageCount());
		move(deltaZeit * speed / 1000);
		Pos min = transformation().getRasterLeftUpper();
		Pos max = transformation().getRasterRightBottom();
		if (this.pos().x() < min.x() || this.pos().x() > max.x() || this.pos().y() < min.y()
				|| this.pos().y() > max.y()) {
			switch (outsideGridStrategy) {
			case kill:
				kill();
				particleDiesListener.onDeath(this);
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

	// ---------------- override methods to return correct type --

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.dreierschach.daddel.gfx.sprite.ImageSprite#relativePos(de.dreierschach.
	 * daddel.model.Pos)
	 */
	@Override
	public Particle relativePos(Pos pos) {
		this.initialPos = pos;
		super.relativePos(pos);
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.dreierschach.daddel.gfx.sprite.ImageSprite#rotation(double)
	 */
	@Override
	public Particle rotation(double rotation) {
		super.rotation(rotation);
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.dreierschach.daddel.gfx.sprite.ImageSprite#rotate(double)
	 */
	@Override
	public Particle rotate(double angle) {
		super.rotate(angle);
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.dreierschach.daddel.gfx.sprite.ImageSprite#r(float)
	 */
	@Override
	public Particle r(float r) {
		super.r(r);
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.dreierschach.daddel.gfx.sprite.ImageSprite#type(int)
	 */
	@Override
	public Particle type(int type) {
		super.type(type);
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.dreierschach.daddel.gfx.sprite.ImageSprite#gameLoop(de.dreierschach.daddel
	 * .gfx.sprite.SpriteGameLoop[])
	 */
	@Override
	public Particle gameLoop(SpriteGameLoop... gameLoops) {
		super.gameLoop(gameLoops);
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.dreierschach.daddel.gfx.sprite.ImageSprite#parent(de.dreierschach.daddel.
	 * gfx.sprite.Sprite)
	 */
	@Override
	public Particle parent(Sprite parent) {
		super.parent(parent);
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.dreierschach.daddel.gfx.sprite.ImageSprite#move(de.dreierschach.daddel.
	 * model.Pos)
	 */
	@Override
	public Particle move(Pos direction) {
		super.move(direction);
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.dreierschach.daddel.gfx.sprite.ImageSprite#move(float)
	 */
	@Override
	public Particle move(float distance) {
		super.move(distance);
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.dreierschach.daddel.gfx.sprite.ImageSprite#actualImage(int)
	 */
	@Override
	public Particle actualImage(int actualImage) {
		super.actualImage(actualImage);
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.dreierschach.daddel.gfx.sprite.ImageSprite#collisionListener(de.
	 * dreierschach.daddel.listener.CollisionListener)
	 */
	@Override
	public Particle collisionListener(CollisionListener collisionListener) {
		super.collisionListener(collisionListener);
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.dreierschach.daddel.gfx.sprite.ImageSprite#onCollision(de.dreierschach.
	 * daddel.gfx.sprite.Sprite)
	 */
	@Override
	public Particle onCollision(Sprite other) {
		super.onCollision(other);
		return this;
	}
	
	/* (non-Javadoc)
	 * @see de.dreierschach.daddel.gfx.sprite.ImageSprite#debug(boolean)
	 */
	@Override
	public Particle debug(boolean debug) {
		super.debug(debug);
		return this;
	}
}
