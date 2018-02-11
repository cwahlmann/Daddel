package de.dreierschach.daddel.gfx.sprite;

import java.util.Random;

import de.dreierschach.daddel.listener.CollisionListener;
import de.dreierschach.daddel.listener.CreateSwarmHandler;
import de.dreierschach.daddel.listener.ParticleDiesListener;
import de.dreierschach.daddel.model.ParticleStrategy;
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
	private double[] speedAnimationRange = { 0, 0 };
	private long[] lifeSpanRange = { 0L, 0L };
	private double[] sizeRange = { 0, 0 };
	private int sizeSteps = 5;

	private double[] directionStartRange = { 0, 0 };
	private double[] rotationStartRange = { 0, 0 };
	private double[] speedStartRange = { 0, 0 };
	private double[] alphaStartRange = { 1, 1 };

	private double[] directionEndRange = null;
	private double[] rotationEndRange = null;
	private double[] speedEndRange = null;
	private double[] alphaEndRange = null;

	//

	private int count;
	private ParticleStrategy outsideGridStrategy;
	private ParticleStrategy endOfLifeStrategy;
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

			double speedStart = random(speedStartRange);
			double speedEnd = speedEndRange != null ? random(speedEndRange) : speedStart;

			double directionStart = random(directionStartRange);
			double directionEnd = directionEndRange != null ? random(directionEndRange) : directionStart;

			double rotationStart = random(rotationStartRange);
			double rotationEnd = rotationEndRange != null ? random(rotationEndRange) : rotationStart;

			double alphaStart = random(alphaStartRange);
			double alphaEnd = alphaEndRange != null ? random(alphaEndRange) : alphaStart;

			Particle particle = new Particle(transformation, typ, random(sizeRange, sizeSteps), random(lifeSpanRange),
					images).speedAnimation(random(speedAnimationRange));
			particle.pos(random(initialPosRange)).speed(speedStart, speedEnd)
					.direction(directionStart, directionEnd).rotation(rotationStart, rotationEnd)
					.alpha(alphaStart, alphaEnd).endOfLife(endOfLifeStrategy)
					.outsideGrid(outsideGridStrategy).collision(collisionListener).parent(parent)
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
	public ParticleSwarmBuilder speedAnimation(double value) {
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
	public ParticleSwarmBuilder speedAnimationRange(double start, double end) {
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
	public ParticleSwarmBuilder alpha(double alpha) {
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
	public ParticleSwarmBuilder alphaRange(double start, double end) {
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
	public ParticleSwarmBuilder alphaStart(double value) {
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
	public ParticleSwarmBuilder alphaStartRange(double start, double end) {
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
	public ParticleSwarmBuilder alphaEnd(double value) {
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
	public ParticleSwarmBuilder alphaEndRange(double start, double end) {
		this.alphaEndRange = new double[] { start, end };
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
	public ParticleSwarmBuilder size(double value) {
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
	public ParticleSwarmBuilder sizeRange(double start, double end, int sizeSteps) {
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
	public ParticleSwarmBuilder direction(double value) {
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
	public ParticleSwarmBuilder directionRange(double start, double end) {
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
	public ParticleSwarmBuilder directionStart(double value) {
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
	public ParticleSwarmBuilder directionStartRange(double start, double end) {
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
	public ParticleSwarmBuilder directionEnd(double value) {
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
	public ParticleSwarmBuilder directionEndRange(double start, double end) {
		this.directionEndRange = new double[] { start, end };
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
	public ParticleSwarmBuilder rotation(double value) {
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
	public ParticleSwarmBuilder rotationRange(double start, double end) {
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
	public ParticleSwarmBuilder rotationStart(double value) {
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
	public ParticleSwarmBuilder rotationStartRange(double start, double end) {
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
	public ParticleSwarmBuilder rotationEnd(double value) {
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
	public ParticleSwarmBuilder rotationEndRange(double start, double end) {
		this.rotationEndRange = new double[] { start, end };
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
	public ParticleSwarmBuilder speed(double value) {
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
	public ParticleSwarmBuilder speedRange(double start, double end) {
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
	public ParticleSwarmBuilder speedStart(double value) {
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
	public ParticleSwarmBuilder speedStartRange(double start, double end) {
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
	public ParticleSwarmBuilder speedEnd(double value) {
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
	public ParticleSwarmBuilder speedEndRange(double start, double end) {
		this.speedEndRange = new double[] { start, end };
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
	public ParticleSwarmBuilder outsideGrid(ParticleStrategy outsideGridStrategy) {
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
	public ParticleSwarmBuilder endOfLife(ParticleStrategy endOfLifeStrategy) {
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

	private double random(double[] range) {
		return (double) (random.nextDouble() * (range[1] - range[0])) + range[0];
	}

	private double random(double[] range, int steps) {
		return (double) (random.nextInt(steps)) / (double) (steps - 1) * (range[1] - range[0]) + range[0];
	}

	private long random(long[] range) {
		return (long) (random.nextDouble() * (range[1] - range[0])) + range[0];
	}

	private Pos random(Pos[] range) {
		return new Pos(random(new double[] { range[0].x(), range[1].x() }),
				random(new double[] { range[0].y(), range[1].y() }));
	}
}
