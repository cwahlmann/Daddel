package de.dreierschach.daddel.gfx.text;

import de.dreierschach.daddel.gfx.sprite.Sprite;
import de.dreierschach.daddel.listener.TextParticleDiesListener;
import de.dreierschach.daddel.model.ParticleStrategy;
import de.dreierschach.daddel.model.Pos;
import de.dreierschach.daddel.model.SpriteGameLoop;
import de.dreierschach.daddel.model.Transformation;
import javafx.geometry.VPos;
import javafx.scene.paint.Color;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

/**
 * ein Text-Partikel ist ein Text-Sprite, der eine definierte Lebensdauer und
 * ein vorgegebenes Verhalten hat
 * 
 * @author Christian
 *
 */
public class TextParticle extends TextSprite {

	private long lifespan = 1000L;
	private Pos initialPos = new Pos(0,0);
	private double rotationStart = 0f;
	private double rotationEnd = 0f;
	private double directionStart = 0f;
	private double directionEnd = 0f;
	private double speed = 0f;
	private double speedStart = 0f;
	private double speedEnd = 0f;
	private double sizeStart = 2f;
	private double sizeEnd = 2f;
	private ParticleStrategy outsideGridStrategy = ParticleStrategy.kill;
	private ParticleStrategy endOfLifeStrategy = ParticleStrategy.kill;
	private TextParticleDiesListener textParticleDiesListener = particle -> {
	};

	/**
	 * @param transformation
	 *            Informationen zur Umrechnung von Spielraster-Punkten in
	 *            Bildschirmpixel
	 * @param lifespan
	 *            Lebensdauer des Patikel in ms
	 * @param text
	 *            der anzuzeigende Text
	 * @param layer
	 *            Die Ebene, auf der der TextPartikel angezeigt wird
	 */
	public TextParticle(Transformation transformation, long lifespan, String text, int layer) {
		super(transformation, text, layer);
		this.lifespan = lifespan;
		this.rotationStart = rotation();
		this.rotationEnd = rotation();
		this.gameLoop(particleGameLoop);
		showPosOnDebug(false);
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
	public TextParticle rotation(double rotationStart, double rotationEnde) {
		this.rotationStart = rotationStart;
		this.rotationEnd = rotationEnde;
		return this;
	}

	/* (non-Javadoc)
	 * @see de.dreierschach.daddel.gfx.text.TextSprite#rotation(double)
	 */
	@Override
	public TextParticle rotation(double rotation) {
		rotation(rotation, rotation);
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
	public TextParticle direction(double directionStart, double directionEnd) {
		this.directionStart = directionStart;
		this.directionEnd = directionEnd;
		return this;
	}

	/* (non-Javadoc)
	 * @see de.dreierschach.daddel.gfx.text.TextSprite#direction(double)
	 */
	@Override
	public TextParticle direction(double direction) {
		direction(direction, direction);
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
	public TextParticle speed(double speedStart, double speedEnd) {
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
	public TextParticle speed(double speed) {
		this.speedStart = speed;
		this.speedEnd = speed;
		this.speed = speed;
		return this;
	}

	/**
	 * Die Größe (Höhe) des Partikel am Anfang und am Ende seiner Lebensdauer
	 * 
	 * @param sizeStart
	 *            Größe (Höhe) in Spielraster-Punkten / s (0 ...)
	 * @param sizeEnd
	 *            Größe (Höhe) in Spielraster-Punkten / s (0 ...)
	 * @return this
	 */
	public TextParticle size(double sizeStart, double sizeEnd) {
		this.sizeStart = sizeStart;
		this.sizeEnd = sizeEnd;
		return this;
	}

	/* (non-Javadoc)
	 * @see de.dreierschach.daddel.gfx.text.TextSprite#size(double)
	 */
	@Override
	public TextParticle size(double size) {
		size((double) size, (double) size);
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
	public TextParticle outsideRasterStrategy(ParticleStrategy outsideRasterStrategy) {
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
	public TextParticle endOfLifeStrategy(ParticleStrategy endOfLifeStrategy) {
		this.endOfLifeStrategy = endOfLifeStrategy;
		return this;
	}

	/**
	 * Aktion, die ausgeführt wird, wenn der TextPartikel stirbt
	 * 
	 * @param textParticleDiesListener
	 *            Aktion
	 * @return this
	 */
	public TextParticle onDeath(TextParticleDiesListener textParticleDiesListener) {
		this.textParticleDiesListener = textParticleDiesListener;
		return this;
	}

	// private methoden

	private SpriteGameLoop particleGameLoop = (partikel, gesamtZeit, deltaZeit) -> {
		if (lifespan > 0 && gesamtZeit >= lifespan) {
			switch (endOfLifeStrategy) {
			case stop:
				return;
			case kill:
				this.kill();
				textParticleDiesListener.onDeath(this);
				return;
			case reappear:
			case restart:
				setTicks(getTicks() - lifespan);
				pos(initialPos);
				break;
			case bounce:
				// TODO
				break;
			case ignore:
			}
		}
		double factor = lifespan == 0 ? 0 : (double) (gesamtZeit) / (double) (lifespan);
		this.speed = (double) ((speedEnd - speedStart) * factor + speedStart);
		super.rotation((rotationEnd - rotationStart) * factor + rotationStart);
		super.direction((directionEnd - directionStart) * factor + directionStart);
		super.size((sizeEnd - sizeStart) * factor + sizeStart);
		move(deltaZeit * speed / 1000);
		Pos min = transformation().getRasterLeftUpper();
		Pos max = transformation().getRasterRightBottom();
		if (this.effektivePos().x() < min.x() || this.effektivePos().x() > max.x() || this.effektivePos().y() < min.y()
				|| this.effektivePos().y() > max.y()) {
			switch (outsideGridStrategy) {
			case stop:
			case kill:
				kill();
				textParticleDiesListener.onDeath(this);
				break;
			case bounce:
				// TODO
				break;
			case reappear:
				if (this.effektivePos().x() < min.x()) {
					this.pos(new Pos(effektivePos().x() - min.x() + max.x(), effektivePos().y()));
				} else if (this.effektivePos().x() > max.x()) {
					this.pos(new Pos(effektivePos().x() - max.x() + min.x(), effektivePos().y()));
				}

				if (this.effektivePos().y() < min.y()) {
					this.pos(new Pos(effektivePos().x(), effektivePos().y() - min.y() + max.y()));
				} else if (this.effektivePos().y() > max.y()) {
					this.pos(new Pos(effektivePos().x(), effektivePos().y() - max.y() + min.y()));
				}
				break;
			case restart:
				setTicks(0);
				pos(initialPos);
			case ignore:
			}
		}

	};

	// overwrite methods for correct return type
	
	/* (non-Javadoc)
	 * @see de.dreierschach.daddel.gfx.text.TextSprite#pos(double, double)
	 */
	@Override
	public TextParticle pos(double x, double y) {
		super.pos(x, y);
		return this;
	}
	
	/* (non-Javadoc)
	 * @see de.dreierschach.daddel.gfx.text.TextSprite#relativePos(de.dreierschach.daddel.model.Pos)
	 */
	@Override
	public TextParticle pos(Pos pos) {
		super.pos(pos);
		this.initialPos = pos;
		return this;
	}

	/* (non-Javadoc)
	 * @see de.dreierschach.daddel.gfx.text.TextSprite#rotate(double)
	 */
	@Override
	public TextParticle rotate(double angle) {
		super.rotate(angle);
		return this;
	}

	/* (non-Javadoc)
	 * @see de.dreierschach.daddel.gfx.text.TextSprite#r(double)
	 */
	@Override
	public TextParticle r(double r) {
		super.r(r);
		return this;
	}

	/* (non-Javadoc)
	 * @see de.dreierschach.daddel.gfx.text.TextSprite#type(int)
	 */
	@Override
	public TextParticle type(int type) {
		super.type(type);
		return this;
	}

	/* (non-Javadoc)
	 * @see de.dreierschach.daddel.gfx.text.TextSprite#gameLoop(de.dreierschach.daddel.model.SpriteGameLoop[])
	 */
	@Override
	public TextParticle gameLoop(SpriteGameLoop... gameLoops) {
		super.gameLoop(gameLoops);
		return this;
	}

	/* (non-Javadoc)
	 * @see de.dreierschach.daddel.gfx.text.TextSprite#move(de.dreierschach.daddel.model.Pos)
	 */
	@Override
	public TextParticle move(Pos direction) {
		super.move(direction);
		return this;
	}

	/* (non-Javadoc)
	 * @see de.dreierschach.daddel.gfx.text.TextSprite#move(double)
	 */
	@Override
	public TextParticle move(double distance) {
		super.move(distance);
		return this;
	}

	/* (non-Javadoc)
	 * @see de.dreierschach.daddel.gfx.text.TextSprite#align(javafx.scene.text.TextAlignment, javafx.geometry.VPos)
	 */
	@Override
	public TextParticle align(TextAlignment align, VPos valign) {
		super.align(align, valign);
		return this;
	}

	/* (non-Javadoc)
	 * @see de.dreierschach.daddel.gfx.text.TextSprite#color(javafx.scene.paint.Color)
	 */
	@Override
	public TextParticle color(Color color) {
		super.color(color);
		return this;
	}

	/* (non-Javadoc)
	 * @see de.dreierschach.daddel.gfx.text.TextSprite#text(java.lang.String)
	 */
	/* (non-Javadoc)
	 * @see de.dreierschach.daddel.gfx.text.TextSprite#text(java.lang.String)
	 */
	@Override
	public TextParticle text(String text) {
		super.text(text);
		return this;
	}

	/* (non-Javadoc)
	 * @see de.dreierschach.daddel.gfx.text.TextSprite#family(java.lang.String)
	 */
	@Override
	public TextParticle family(String family) {
		super.family(family);
		return this;
	}

	/* (non-Javadoc)
	 * @see de.dreierschach.daddel.gfx.text.TextSprite#weight(javafx.scene.text.FontWeight)
	 */
	@Override
	public TextParticle weight(FontWeight fontWeight) {
		super.weight(fontWeight);
		return this;
	}

	/* (non-Javadoc)
	 * @see de.dreierschach.daddel.gfx.text.TextSprite#parent(de.dreierschach.daddel.gfx.sprite.Sprite)
	 */
	@Override
	public TextParticle parent(Sprite parent) {
		super.parent(parent);
		return this;
	}
}
