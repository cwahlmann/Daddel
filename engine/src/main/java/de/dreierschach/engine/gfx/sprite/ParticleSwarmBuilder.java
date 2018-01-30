package de.dreierschach.engine.gfx.sprite;

import java.util.Random;

import de.dreierschach.engine.listener.CollisionListener;
import de.dreierschach.engine.listener.CreateSwarmHandler;
import de.dreierschach.engine.model.EndOfLifeStrategy;
import de.dreierschach.engine.model.OutsideGridStrategy;
import de.dreierschach.engine.model.Pos;
import de.dreierschach.engine.model.Transformation;

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
	private CollisionListener collisionListener = (me, other) -> {};
	private CreateSwarmHandler createSwarmHandler = swarm -> {};
	private Sprite parent = null;

	//
		
	public ParticleSwarmBuilder(int count, Transformation transformation, int typ, String[] images, CreateSwarmHandler createSwarmHandler) {
		this.count = count;
		this.transformation = transformation;
		this.typ = typ;
		this.images = images;
		this.createSwarmHandler = createSwarmHandler;
	}
	
	//
	
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
					images)
					.speedAnimation(random(speedAnimationRange));
			particle
					.relativePos(random(initialPosRange))
					.speed(speedStart, speedEnd)
					.direction(directionStart, directionEnd)
					.rotation(rotationStart, rotationEnd)
					.alpha(alphaStart, alphaEnd)
					.endOfLifeStrategy(endOfLifeStrategy)
					.outsideRasterStrategy(outsideGridStrategy)
					.collisionListener(collisionListener)
					.parent(parent)
					;
			swarm.getParticles().add(particle);
		}
		createSwarmHandler.onCreate(swarm);
		return swarm;
	}

	// setter
	
	public ParticleSwarmBuilder initialPos(Pos value) {
		return initialPosRange(value, value);
	}
	
	public ParticleSwarmBuilder initialPosRange(Pos start, Pos end) {
		this.initialPosRange[0] = start;
		this.initialPosRange[1] = end;	
		return this;
	}

	//
	
	public ParticleSwarmBuilder speedAnimation(float value) {
		return speedAnimationRange(value, value);
	}
	
	public ParticleSwarmBuilder speedAnimationRange(float start, float end) {
		this.speedAnimationRange[0] = start;
		this.speedAnimationRange[1] = end;
		return this;
	}

	//
	
	public ParticleSwarmBuilder lifeSpan(long value) {
		return lifeSpanRange(value, value);
	}
	
	public ParticleSwarmBuilder lifeSpanRange(long start, long end) {
		this.lifeSpanRange[0] = start;
		this.lifeSpanRange[1] = end;
		return this;
	}

	//
	
	public ParticleSwarmBuilder alpha(float alpha) {
		alphaRange(alpha, alpha);
		return this;
	}

	public ParticleSwarmBuilder alphaRange(float start, float end) {
		this.alphaStartRange[0] = start;
		this.alphaStartRange[1] = end;
		this.alphaEndRange = null;
		return this;
	}
	
	public ParticleSwarmBuilder alphaStart(float value) {
		alphaStartRange(value, value);
		return this;
	}
	
	public ParticleSwarmBuilder alphaStartRange(float start, float end) {
		this.alphaStartRange[0] = start;
		this.alphaStartRange[1] = end;
		return this;
	}
	
	public ParticleSwarmBuilder alphaEnd(float value) {
		alphaEndRange(value, value);
		return this;
	}
	
	public ParticleSwarmBuilder alphaEndRange(float start, float end) {
		this.alphaEndRange = new float[] {start, end};
		return this;
	}

	public ParticleSwarmBuilder size(float value) {
		return sizeRange(value, value, 1);
	}
		
	public ParticleSwarmBuilder sizeRange(float start, float end, int sizeSteps) {
		this.sizeRange[0] = start;
		this.sizeRange[1] = end;
		this.sizeSteps = sizeSteps;
		return this;
	}

	// direction
	
	public ParticleSwarmBuilder direction(float value) {
		return directionRange(value, value);
	}

	public ParticleSwarmBuilder directionRange(float start, float end) {
		this.directionStartRange[0] = start;
		this.directionStartRange[1] = end;
		this.directionEndRange = null;
		return this;
	}

	public ParticleSwarmBuilder directionStart(float value) {
		return directionStartRange(value, value);
	}
	
	public ParticleSwarmBuilder directionStartRange(float start, float end) {
		this.directionStartRange[0] = start;
		this.directionStartRange[1] = end;
		return this;
	}

	public ParticleSwarmBuilder directionEnd(float value) {
		return directionEndRange(value, value);		
	}
	
	public ParticleSwarmBuilder directionEndRange(float start, float end) {
		this.directionEndRange = new float[] {start, end};
		return this;
	}

	// rotation 
	
	public ParticleSwarmBuilder rotation(float value) {
		return rotationRange(value, value);
	}

	public ParticleSwarmBuilder rotationRange(float start, float end) {
		this.rotationStartRange[0] = start;
		this.rotationStartRange[1] = end;
		this.rotationEndRange = null;
		return this;
	}

	public ParticleSwarmBuilder rotationStart(float value) {
		return rotationStartRange(value, value);
	}
	
	public ParticleSwarmBuilder rotationStartRange(float start, float end) {
		this.rotationStartRange[0] = start;
		this.rotationStartRange[1] = end;
		return this;
	}

	public ParticleSwarmBuilder rotationEnd(float value) {
		return rotationEndRange(value, value);
	}
	
	public ParticleSwarmBuilder rotationEndRange(float start, float end) {
		this.rotationEndRange = new float[] {start, end};
		return this;
	}

	// speed
	
	public ParticleSwarmBuilder speed(float value) {
		return speedRange(value, value);
	}
	
	public ParticleSwarmBuilder speedRange(float start, float end) {
		this.speedStartRange[0] = start;
		this.speedStartRange[1] = end;
		this.speedEndRange = null;
		return this;
	}

	public ParticleSwarmBuilder speedStart(float value) {
		return speedStartRange(value, value);
	}
	
	public ParticleSwarmBuilder speedStartRange(float start, float end) {
		this.speedStartRange[0] = start;
		this.speedStartRange[1] = end;
		return this;
	}

	public ParticleSwarmBuilder speedEnd(float value) {
		return speedEndRange(value, value);
	}
	
	public ParticleSwarmBuilder speedEndRange(float start, float end) {
		this.speedEndRange = new float[] {start, end};
		return this;
	}

	//
	
	public ParticleSwarmBuilder count(int count) {
		this.count = count;
		return this;
	}

	public ParticleSwarmBuilder outsideGridStrategy(OutsideGridStrategy outsideGridStrategy) {
		this.outsideGridStrategy = outsideGridStrategy;
		return this;
	}

	public ParticleSwarmBuilder endOfLifeStrategy(EndOfLifeStrategy endOfLifeStrategy) {
		this.endOfLifeStrategy = endOfLifeStrategy;
		return this;
	}

	public ParticleSwarmBuilder transformation(Transformation transformation) {
		this.transformation = transformation;
		return this;
	}

	public ParticleSwarmBuilder typ(int typ) {
		this.typ = typ;
		return this;
	}

	public ParticleSwarmBuilder images(String[] images) {
		this.images = images;
		return this;
	}

	public ParticleSwarmBuilder collisionListener(CollisionListener collisionListener) {
		this.collisionListener = collisionListener;
		return this;
	}

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
		return (float)(random.nextInt(steps)) / (float) (steps-1) * (range[1]-range[0])+ range[0];
	}

	private long random(long[] range) {
		return (long) (random.nextDouble() * (range[1] - range[0])) + range[0];
	}

	private Pos random(Pos[] range) {
		return new Pos(random(new float[] { range[0].x(), range[1].x() }),
				random(new float[] { range[0].y(), range[1].y() }));
	}
}
