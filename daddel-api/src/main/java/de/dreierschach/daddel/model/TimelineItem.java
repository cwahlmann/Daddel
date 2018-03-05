package de.dreierschach.daddel.model;

public class TimelineItem {
	private SpriteGameLoop spriteGameLoop;
	private long duration;

	/**
	 * ein Eintrag in einer Timeline
	 * 
	 * @param spriteGameLoop
	 *            eine Spielschleife
	 * @param duration
	 *            die Dauer der Spielschleife
	 */
	public TimelineItem(SpriteGameLoop spriteGameLoop, long duration) {
		this.spriteGameLoop = spriteGameLoop;
		this.duration = duration;
	}

	/**
	 * @return die Dauer der Spielschleife
	 */
	public long duration() {
		return duration;
	}

	/**
	 * @return die Spielschleife
	 */
	public SpriteGameLoop spriteGameLoop() {
		return spriteGameLoop;
	}

}