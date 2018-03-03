package de.dreierschach.daddel.gfx.sprite;

import de.dreierschach.daddel.Screen.Debug;
import de.dreierschach.daddel.listener.CollisionListener;
import de.dreierschach.daddel.listener.ParticleDiesListener;
import de.dreierschach.daddel.model.ParticleStrategy;
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
	private double rotationStart = 0;
	private double rotationEnd = 0;
	private double directionStart = 0;
	private double directionEnd = 0;
	private double alphaStart = 1;
	private double alphaEnd = 1;
	private double speed = 0;
	private double speedStart = 0;
	private double speedEnd = 0;
	private double speedAnimation = 10; // bilder/s
	private ParticleStrategy outsideGridStrategy = ParticleStrategy.kill;
	private ParticleStrategy endOfLifeStrategy = ParticleStrategy.kill;
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
	public Particle(Transformation transformation, int typ, int layer, double groesse, long lifespan, String... bilder) {
		super(transformation, typ, layer, groesse, bilder);
		this.lifespan = lifespan;
		this.rotationStart = rotation();
		this.rotationEnd = rotation();
		this.gameLoop(particleGameLoop);
		showPosOnDebug(false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.dreierschach.daddel.gfx.sprite.ImageSprite#rotation(double)
	 */
	@Override
	public Particle rotation(double rotation) {
		this.rotation(rotation, rotation);
		return this;
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
		super.rotation(rotationStart);
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
		super.direction(directionStart);
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
	public Particle alpha(double alphaStart, double alphaEnd) {
		this.alphaStart = alphaStart;
		this.alphaEnd = alphaEnd;
		super.alpha(alphaStart);
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.dreierschach.daddel.gfx.sprite.ImageSprite#alpha(double)
	 */
	@Override
	public Particle alpha(double alpha) {
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
	public Particle speed(double speedStart, double speedEnd) {
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
	public Particle speed(double speed) {
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
	public Particle speedAnimation(double speedAnimation) {
		this.speedAnimation = speedAnimation;
		return this;
	}

	/**
	 * Legt fest, was mit dem Partikel geschehen soll, wenn er das Spielraster
	 * verlässt
	 * 
	 * @param outsideGridStrategy
	 *            mögliche Strategien sind: ignorieren (ignore), Partikel töten
	 *            (kill), zurücktitschen (bounce), auf der gegenüberliegenden Seite
	 *            erscheinen (reappear), Partikel neu starten (restart)
	 * @return this
	 */
	public Particle outsideGrid(ParticleStrategy outsideGridStrategy) {
		this.outsideGridStrategy = outsideGridStrategy;
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
	public Particle endOfLife(ParticleStrategy endOfLifeStrategy) {
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
			case kill:
				this.kill();
				particleDiesListener.onDeath(this);
				return;
			case reappear:
			case restart:
				setTicks(getTicks() - lifespan);
				super.pos(initialPos);
				break;
			case bounce:
				// TODO
				break;
			case ignore:
			}
		}
		double factor = lifespan == 0 ? 0 : ((double) gesamtZeit) / ((double) lifespan);
		this.speed = (double) ((speedEnd - speedStart) * factor + speedStart);
		super.rotation((rotationEnd - rotationStart) * factor + rotationStart);
		super.direction((directionEnd - directionStart) * factor + directionStart);
		super.alpha((alphaEnd - alphaStart) * (double) factor + alphaStart);
		actualImage(((int) (gesamtZeit * speedAnimation / 1000)) % imageCount());
		move(((double) deltaZeit) / 1000d * speed);
		Pos min = transformation().getRasterLeftUpper();
		Pos max = transformation().getRasterRightBottom();
		if (this.effektivePos().x() < min.x() || this.effektivePos().x() > max.x() || this.effektivePos().y() < min.y()
				|| this.effektivePos().y() > max.y()) {
			switch (outsideGridStrategy) {
			case stop:
			case kill:
				kill();
				particleDiesListener.onDeath(this);
				break;
			case bounce:
				// TODO
				break;
			case reappear:
				if (this.effektivePos().x() < min.x()) {
					super.pos(new Pos(effektivePos().x() - min.x() + max.x(), effektivePos().y()));
				} else if (this.effektivePos().x() > max.x()) {
					super.pos(new Pos(effektivePos().x() - max.x() + min.x(), effektivePos().y()));
				}

				if (this.effektivePos().y() < min.y()) {
					super.pos(new Pos(effektivePos().x(), effektivePos().y() - min.y() + max.y()));
				} else if (this.effektivePos().y() > max.y()) {
					super.pos(new Pos(effektivePos().x(), effektivePos().y() - max.y() + min.y()));
				}
				break;
			case restart:
				super.pos(initialPos);
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
	 * @see de.dreierschach.daddel.gfx.sprite.ImageSprite#pos(double, double)
	 */
	@Override
	public Particle pos(double x, double y) {
		return this.pos(new Pos(x, y));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.dreierschach.daddel.gfx.sprite.ImageSprite#relativePos(de.dreierschach.
	 * daddel.model.Pos)
	 */
	@Override
	public Particle pos(Pos pos) {
		this.initialPos = pos;
		super.pos(pos);
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
	 * @see de.dreierschach.daddel.gfx.sprite.ImageSprite#r(double)
	 */
	@Override
	public Particle r(double r) {
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
	 * @see de.dreierschach.daddel.gfx.sprite.ImageSprite#move(double)
	 */
	@Override
	public Particle move(double distance) {
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
	public Particle collision(CollisionListener collisionListener) {
		super.collision(collisionListener);
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.dreierschach.daddel.gfx.sprite.ImageSprite#debug(boolean)
	 */
	@Override
	public Particle debug(Debug debug) {
		super.debug(debug);
		return this;
	}
}
