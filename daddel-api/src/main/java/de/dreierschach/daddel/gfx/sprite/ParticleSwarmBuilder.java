package de.dreierschach.daddel.gfx.sprite;

import java.util.Random;

import de.dreierschach.daddel.listener.CollisionListener;
import de.dreierschach.daddel.listener.CreateSwarmHandler;
import de.dreierschach.daddel.listener.ParticleDiesListener;
import de.dreierschach.daddel.model.EndOfLifeStrategy;
import de.dreierschach.daddel.model.OutsideGridStrategy;
import de.dreierschach.daddel.model.Pos;
import de.dreierschach.daddel.model.Transformation;

/**
 * Ein Partikelschwarm besteht aus vielen Partikeln, die in gewissen Grenzen
 * zufällig erzeugt wurden
 * 
 * @author Christian
 *
 */
/**
 * @author Christian
 *
 */
public class ParticleSwarmBuilder {

	// values for random particle generation

	private Pos[] initialPosRange = { new Pos(0, 0), new Pos(0, 0) };
	private float[] speedAnimationRange = { 0, 0 };
	private long[] lifeSpanRange = { 0L, 0L };
	private float[] sizeRange = { 0, 0 };
	private int sizeSteps = 5;

	private float[] directionStartRange = { 0, 0 };
	private float[] rotationStartRange = { 0, 0 };
	private float[] speedStartRange = { 0, 0 };
	private float[] alphaStartRange = { 1, 1 };

	private float[] directionEndRange = null;
	private float[] rotationEndRange = null;
	private float[] speedEndRange = null;
	private float[] alphaEndRange = null;

	//

	private int count;
	private OutsideGridStrategy outsideGridStrategy;
	private EndOfLifeStrategy endOfLifeStrategy;
	private Transformation transformation;
	private int typ;
	private String[] images;
	private CollisionListener collisionListener = (me, other) -> {
	};
	private CreateSwarmHandler createSwarmHandler = swarm -> {
	};
	private ParticleDiesListener particleDiesListener = particle -> {
	};
	private Sprite parent = null;

	//

	/**
	 * erzeugt einen Builder, mit dem ein Partikelschwarm generiert werden kann
	 * 
	 * @param count
	 *            Anzahl Partikel
	 * @param transformation
	 *            Informationen zur Umrechnung von Spielraster-Punkten in
	 *            Bildschirmpixel
	 * @param typ
	 *            Benutzerdefinierter Typ, Integer
	 * @param images
	 *            Die Pfade der Bilder des Partikels
	 * @param createSwarmHandler
	 *            Dieser wird aufgerufen, wenn der Partikelschwarm erzeugt wird, und
	 *            bekommt diesen als Parameter mitgegeben
	 */
	public ParticleSwarmBuilder(int count, Transformation transformation, int typ, String[] images,
			CreateSwarmHandler createSwarmHandler) {
		this.count = count;
		this.transformation = transformation;
		this.typ = typ;
		this.images = images;
		this.createSwarmHandler = createSwarmHandler;
	}

	/**
	 * erzeugt den Partikelschwarm nach den vorher angegeben Parametern
	 * 
	 * @return den erzeugten Partikelschwarm
	 */
	public ParticleSwarm create() {
		ParticleSwarm swarm = new ParticleSwarm();
		for (int i = 0; i < count; i++) {

			float speedStart = random(speedStartRange);
			float speedEnd = speedEndRange != null ? random(speedEndRange) : speedStart;

			float directionStart = random(directionStartRange);
			float directionEnd = directionEndRange != null ? random(directionEndRange) : directionStart;

			float rotationStart = random(rotationStartRange);
			float rotationEnd = rotationEndRange != null ? random(rotationEndRange) : rotationStart;

			float alphaStart = random(alphaStartRange);
			float alphaEnd = alphaEndRange != null ? random(alphaEndRange) : alphaStart;

			Particle particle = new Particle(transformation, typ, random(sizeRange, sizeSteps), random(lifeSpanRange),
					images).speedAnimation(random(speedAnimationRange));
			particle.pos(random(initialPosRange)).speed(speedStart, speedEnd)
					.direction(directionStart, directionEnd).rotation(rotationStart, rotationEnd)
					.alpha(alphaStart, alphaEnd).endOfLifeStrategy(endOfLifeStrategy)
					.outsideRasterStrategy(outsideGridStrategy).collision(collisionListener).parent(parent)
					.onDeath(particleDiesListener);
			swarm.getParticles().add(particle);
		}
		createSwarmHandler.onCreate(swarm);
		return swarm;
	}

	// ------------ builder methods --

	// position

	/**
	 * setze die Startposition der Partikel
	 * 
	 * @param value
	 *            Startposition in Spielraster-Punkten
	 * @return this
	 */
	public ParticleSwarmBuilder initialPos(Pos value) {
		return initialPosRange(value, value);
	}

	/**
	 * legt eine Fläche fest, in der die Startpositionen der Partikel zufälig
	 * ausgewählt werden
	 * 
	 * @param start
	 *            linke obere Ecke der möglichen Startposition in
	 *            Spielraster-Punkten
	 * @param end
	 *            rechte untere Ecke der möglichen Startposition in
	 *            Spielraster-Punkten
	 * @return this
	 */
	public ParticleSwarmBuilder initialPosRange(Pos start, Pos end) {
		this.initialPosRange[0] = start;
		this.initialPosRange[1] = end;
		return this;
	}

	// animation

	/**
	 * setzt die Animationsgeschwindigkeit
	 * 
	 * @param value
	 *            Geschwindigkeit in Spielraster-Punkten / s
	 * @return this
	 */
	public ParticleSwarmBuilder speedAnimation(float value) {
		return speedAnimationRange(value, value);
	}

	/**
	 * Legt ein Interval für die Animationsgeschwindigkeit fest, innerhalb dessen
	 * ein zufälliger Wert ermittelt wird
	 * 
	 * @param start
	 *            minimale Animationsgeschwindigkeit in Bilder / s
	 * @param end
	 *            maximale Animationsgeschwindigkeit in Bilder / s
	 * @return this
	 */
	public ParticleSwarmBuilder speedAnimationRange(float start, float end) {
		this.speedAnimationRange[0] = start;
		this.speedAnimationRange[1] = end;
		return this;
	}

	//

	/**
	 * Legt die Lebensdauer fest
	 * 
	 * @param value
	 *            Lebensdauer in ms
	 * @return this
	 */
	public ParticleSwarmBuilder lifeSpan(long value) {
		return lifeSpanRange(value, value);
	}

	/**
	 * Legt ein Interval für die Lebensdauer fest, innerhalb dessen ein zufälliger
	 * Wert ermittelt wird
	 * 
	 * @param start
	 *            minimale Lebensdauer in ms
	 * @param end
	 *            maximale Lebensdauer in ms
	 * @return this
	 */
	public ParticleSwarmBuilder lifeSpanRange(long start, long end) {
		this.lifeSpanRange[0] = start;
		this.lifeSpanRange[1] = end;
		return this;
	}

	//

	/**
	 * Legt die Undurchsichtigkeit fest
	 * 
	 * @param alpha
	 *            Undurchsichtigkeit (0 ... 1.0)
	 * @return this
	 */
	public ParticleSwarmBuilder alpha(float alpha) {
		alphaRange(alpha, alpha);
		return this;
	}

	/**
	 * Legt ein Interval für die Undurchsichtigkeit fest, innerhalb dessen ein
	 * zufälliger Wert ermittelt wird
	 * 
	 * @param start
	 *            minimale Undurchsichtigkeit (0 ... 1.0)
	 * @param end
	 *            maximale Undurchsichtigkeit (0 ... 1.0)
	 * @return this
	 */
	public ParticleSwarmBuilder alphaRange(float start, float end) {
		this.alphaStartRange[0] = start;
		this.alphaStartRange[1] = end;
		this.alphaEndRange = null;
		return this;
	}

	/**
	 * Legt die Undurchsichtigkeit zu Beginn der Lebensdauer fest
	 * 
	 * @param value
	 *            Undurchsichtigkeit (0 ... 1.0)
	 * @return this
	 */
	public ParticleSwarmBuilder alphaStart(float value) {
		alphaStartRange(value, value);
		return this;
	}

	/**
	 * Legt ein Interval für die Undurchsichtigkeit zu Beginn der Lebensdauer fest,
	 * innerhalb dessen ein zufälliger Wert ermittelt wird
	 * 
	 * @param start
	 *            minimale Undurchsichtigkeit (0 ... 1.0)
	 * @param end
	 *            maximale Undurchsichtigkeit (0 ... 1.0)
	 * @return this
	 */
	public ParticleSwarmBuilder alphaStartRange(float start, float end) {
		this.alphaStartRange[0] = start;
		this.alphaStartRange[1] = end;
		return this;
	}

	/**
	 * Legt die Undurchsichtigkeit zum Ende der Lebensdauer fest
	 * 
	 * @param value
	 *            Undurchsichtigkeit (0 ... 1.0)
	 * @return this
	 */
	public ParticleSwarmBuilder alphaEnd(float value) {
		alphaEndRange(value, value);
		return this;
	}

	/**
	 * Legt ein Interval für die Undurchsichtigkeit zum Ende der Lebensdauer fest,
	 * innerhalb dessen ein zufälliger Wert ermittelt wird
	 * 
	 * @param start
	 *            minimale Undurchsichtigkeit (0 ... 1.0)
	 * @param end
	 *            maximale Undurchsichtigkeit (0 ... 1.0)
	 * @return this
	 */
	public ParticleSwarmBuilder alphaEndRange(float start, float end) {
		this.alphaEndRange = new float[] { start, end };
		return this;
	}

	// size

	/**
	 * Legt die Größe fest
	 * 
	 * @param value
	 *            Größe in Spielraster-Punkten
	 * @return this
	 */
	public ParticleSwarmBuilder size(float value) {
		return sizeRange(value, value, 1);
	}

	/**
	 * Legt ein Interval für die Größe fest, innerhalb dessen ein zufälliger Wert
	 * ermittelt wird
	 * 
	 * @param start
	 *            minimale Größe in Spielraster-Punkten
	 * @param end
	 *            maximale Größe in Spielraster-Punkten
	 * @param sizeSteps
	 *            Anzahl verschiedener zugelassener Bildgrößen
	 * @return this
	 */
	public ParticleSwarmBuilder sizeRange(float start, float end, int sizeSteps) {
		this.sizeRange[0] = start;
		this.sizeRange[1] = end;
		this.sizeSteps = sizeSteps;
		return this;
	}

	// direction

	/**
	 * Legt Bewegungsrichtung fest
	 * 
	 * @param value
	 *            Bewegungsrichtung (0 .. 360)
	 * @return this
	 */
	public ParticleSwarmBuilder direction(float value) {
		return directionRange(value, value);
	}

	/**
	 * Legt ein Interval für Bewegungsrichtung fest, innerhalb dessen ein zufälliger
	 * Wert ermittelt wird
	 * 
	 * @param start
	 *            minimale Bewegungsrichtung (0 .. 360)
	 * @param end
	 *            maximale Bewegungsrichtung (0 .. 360)
	 * @return this
	 */
	public ParticleSwarmBuilder directionRange(float start, float end) {
		this.directionStartRange[0] = start;
		this.directionStartRange[1] = end;
		this.directionEndRange = null;
		return this;
	}

	/**
	 * Legt die Bewegungsrichtung fest
	 * 
	 * @param value
	 *            Bewegungsrichtung (0 .. 360)
	 * @return this
	 */
	public ParticleSwarmBuilder directionStart(float value) {
		return directionStartRange(value, value);
	}

	/**
	 * Legt ein Interval für die Bewegungsrichtung fest, innerhalb dessen ein
	 * zufälliger Wert ermittelt wird
	 * 
	 * @param start
	 *            minimale Bewegungsrichtung (0 .. 360)
	 * @param end
	 *            maximale Bewegungsrichtung (0 .. 360)
	 * @return this
	 */
	public ParticleSwarmBuilder directionStartRange(float start, float end) {
		this.directionStartRange[0] = start;
		this.directionStartRange[1] = end;
		return this;
	}

	/**
	 * Legt die Bewegungsrichtung fest
	 * 
	 * @param value
	 *            Bewegungsrichtung (0 .. 360)
	 * @return this
	 */
	public ParticleSwarmBuilder directionEnd(float value) {
		return directionEndRange(value, value);
	}

	/**
	 * Legt ein Interval für die Bewegungsrichtung fest, innerhalb dessen ein
	 * zufälliger Wert ermittelt wird
	 * 
	 * @param start
	 *            minimale Bewegungsrichtung (0 .. 360)
	 * @param end
	 *            maximale Bewegungsrichtung (0 .. 360)
	 * @return this
	 */
	public ParticleSwarmBuilder directionEndRange(float start, float end) {
		this.directionEndRange = new float[] { start, end };
		return this;
	}

	// rotation

	/**
	 * Legt die Rotation fest
	 * 
	 * @param value
	 *            Rotation (0 .. 360)
	 * @return this
	 */
	public ParticleSwarmBuilder rotation(float value) {
		return rotationRange(value, value);
	}

	/**
	 * Legt ein Interval für die Rotation fest, innerhalb dessen ein zufälliger Wert
	 * ermittelt wird
	 * 
	 * @param start
	 *            minimale Rotation (0 ... 360)
	 * @param end
	 *            maximale Rotation (0 ... 360)
	 * @return this
	 */
	public ParticleSwarmBuilder rotationRange(float start, float end) {
		this.rotationStartRange[0] = start;
		this.rotationStartRange[1] = end;
		this.rotationEndRange = null;
		return this;
	}

	/**
	 * Legt die Rotation zu Beginn der Lebensdauer fest
	 * 
	 * @param value
	 *            Rotation (0 ... 360)
	 * @return this
	 */
	public ParticleSwarmBuilder rotationStart(float value) {
		return rotationStartRange(value, value);
	}

	/**
	 * Legt ein Interval für die Rotation zu Beginn der Lebensdauer fest, innerhalb
	 * dessen ein zufälliger Wert ermittelt wird
	 * 
	 * @param start
	 *            minimale Rotation (0 ... 360)]
	 * @param end
	 *            maximale Rotation (0 ... 360)
	 * @return this
	 */
	public ParticleSwarmBuilder rotationStartRange(float start, float end) {
		this.rotationStartRange[0] = start;
		this.rotationStartRange[1] = end;
		return this;
	}

	/**
	 * Legt Rotation am Ende der Lebensdauer fest
	 * 
	 * @param value
	 *            Rotation (0 ... 360)
	 * @return this
	 */
	public ParticleSwarmBuilder rotationEnd(float value) {
		return rotationEndRange(value, value);
	}

	/**
	 * Legt ein Interval für Rotation am Ende der Lebensdauer fest, innerhalb dessen
	 * ein zufälliger Wert ermittelt wird
	 * 
	 * @param start
	 *            minimale Rotation (0 ... 360)
	 * @param end
	 *            maximale Rotation (0 ... 360)
	 * @return this
	 */
	public ParticleSwarmBuilder rotationEndRange(float start, float end) {
		this.rotationEndRange = new float[] { start, end };
		return this;
	}

	// speed

	/**
	 * Legt die Geschwindigkeit fest
	 * 
	 * @param value
	 *            Geschwindigkeit in Spielraster-Punkten / s
	 * @return this
	 */
	public ParticleSwarmBuilder speed(float value) {
		return speedRange(value, value);
	}

	/**
	 * Legt ein Interval für die Geschwindigkeit fest, innerhalb dessen ein
	 * zufälliger Wert ermittelt wird
	 * 
	 * @param start
	 *            minimale Geschwindigkeit in Spielraster-Punkten / s
	 * @param end
	 *            maximale Geschwindigkeit in Spielraster-Punkten / s
	 * @return this
	 */
	public ParticleSwarmBuilder speedRange(float start, float end) {
		this.speedStartRange[0] = start;
		this.speedStartRange[1] = end;
		this.speedEndRange = null;
		return this;
	}

	/**
	 * Legt die Geschwindigkeit zu Beginn der Lebensdauer fest
	 * 
	 * @param value
	 *            Geschwindigkeit in Spielraster-Punkten / s
	 * @return this
	 */
	public ParticleSwarmBuilder speedStart(float value) {
		return speedStartRange(value, value);
	}

	/**
	 * Legt ein Interval für die Geschwindigkeit zu Beginn der Lebensdauer fest,
	 * innerhalb dessen ein zufälliger Wert ermittelt wird
	 * 
	 * @param start
	 *            minimale Geschwindigkeit in Spielraster-Punkten / s
	 * @param end
	 *            maximale Geschwindigkeit in Spielraster-Punkten / s
	 * @return this
	 */
	public ParticleSwarmBuilder speedStartRange(float start, float end) {
		this.speedStartRange[0] = start;
		this.speedStartRange[1] = end;
		return this;
	}

	/**
	 * Legt die Geschwindigkeit am Ende der Lebensdauer fest
	 * 
	 * @param value
	 *            Geschwindigkeit in Spielraster-Punkten / s
	 * @return this
	 */
	public ParticleSwarmBuilder speedEnd(float value) {
		return speedEndRange(value, value);
	}

	/**
	 * Legt ein Interval für die Geschwindigkeit am Ende der Lebensdauer fest,
	 * innerhalb dessen ein zufälliger Wert ermittelt wird
	 * 
	 * @param start
	 *            minimale Geschwindigkeit in Spielraster-Punkten / s
	 * @param end
	 *            maximale Geschwindigkeit in Spielraster-Punkten / s
	 * @return this
	 */
	public ParticleSwarmBuilder speedEndRange(float start, float end) {
		this.speedEndRange = new float[] { start, end };
		return this;
	}

	//

	/**
	 * setzt die Anzahl zu erzeugender Partikel
	 * 
	 * @param count
	 *            Anzahl
	 * @return this
	 */
	public ParticleSwarmBuilder count(int count) {
		this.count = count;
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
	public ParticleSwarmBuilder outsideGridStrategy(OutsideGridStrategy outsideGridStrategy) {
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
	public ParticleSwarmBuilder endOfLifeStrategy(EndOfLifeStrategy endOfLifeStrategy) {
		this.endOfLifeStrategy = endOfLifeStrategy;
		return this;
	}

	/**
	 * Aktion, die ausgeführt wird, wenn ein Partikel stirbt
	 * 
	 * @param particleDiesListener
	 *            Aktion
	 * @return this
	 */
	public ParticleSwarmBuilder onDeath(ParticleDiesListener particleDiesListener) {
		this.particleDiesListener = particleDiesListener;
		return this;
	}

	/**
	 * Informationen zur Umrechnung von Spielraster-Punkten in Bildschirmpixel
	 * 
	 * @param transformation
	 *            Informationen zur Umrechnung von Spielraster-Punkten in
	 *            Bildschirmpixel
	 * @return this
	 */
	public ParticleSwarmBuilder transformation(Transformation transformation) {
		this.transformation = transformation;
		return this;
	}

	/**
	 * Benutzerdefinierter Typ für Partikel
	 * 
	 * @param typ
	 *            Integer
	 * @return this
	 */
	public ParticleSwarmBuilder typ(int typ) {
		this.typ = typ;
		return this;
	}

	/**
	 * legt die Bilder der Partikel fest
	 * 
	 * @param images
	 *            Pfade der Bilder
	 * @return this
	 */
	public ParticleSwarmBuilder images(String[] images) {
		this.images = images;
		return this;
	}

	/**
	 * legt die Aktion fest, die im Falle einer Kollision ausgeführt wird
	 * 
	 * @param collisionListener
	 *            Aktion
	 * @return this
	 */
	public ParticleSwarmBuilder collisionListener(CollisionListener collisionListener) {
		this.collisionListener = collisionListener;
		return this;
	}

	/**
	 * Legt das Elternsprite für die Partikel fest
	 * 
	 * @param parent
	 *            ein Sprite
	 * @return this
	 */
	public ParticleSwarmBuilder parent(Sprite parent) {
		this.parent = parent;
		return this;
	}

	// random

	private static Random random = new Random();

	private float random(float[] range) {
		return (float) (random.nextDouble() * (range[1] - range[0])) + range[0];
	}

	private float random(float[] range, int steps) {
		return (float) (random.nextInt(steps)) / (float) (steps - 1) * (range[1] - range[0]) + range[0];
	}

	private long random(long[] range) {
		return (long) (random.nextDouble() * (range[1] - range[0])) + range[0];
	}

	private Pos random(Pos[] range) {
		return new Pos(random(new float[] { range[0].x(), range[1].x() }),
				random(new float[] { range[0].y(), range[1].y() }));
	}
}
