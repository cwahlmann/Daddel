package de.dreierschach.daddel.model;

import java.util.ArrayList;
import java.util.List;

import de.dreierschach.daddel.gfx.sprite.Sprite;
import de.dreierschach.daddel.listener.TimelineDiesListener;

/**
 * Eine Timeline ist Spielschleife für Sprites, die wiederum aus mehreren
 * Sprite-Spielschleifen besteht. Letztere sind nacheinander für eine jeweils
 * angegebene Dauer aktiv. Die Timeline kann einmalig laufen oder immer wieder
 * von vorne bis hinten wiederholt werden.
 * 
 * @author Christian
 *
 */
public class Timeline implements SpriteGameLoop {
	public static long DEFAULT_DURATION = 1000;

	private List<TimelineItem> items = new ArrayList<>();
	private long offset = 0;
	private int actualItem = 0;
	private boolean cycle = false;
	private boolean running = true;
	private TimelineDiesListener timelineDiesListener = timeline -> {
	};

	/**
	 * FÜgt der Timeline eine weitere Spielschleife hinzu.
	 * 
	 * @param spriteGameLoop
	 *            die Spielschleife
	 * @param duration
	 *            die Dauer der Spielschleife in ms
	 * @return this
	 */
	public Timeline with(SpriteGameLoop spriteGameLoop, long duration) {
		items.add(new TimelineItem(spriteGameLoop, duration));
		return this;
	}

	/**
	 * FÜgt der Timeline eine weitere Spielschleife hinzu. Diese läuft 1000 ms.
	 * 
	 * @param spriteGameLoop
	 *            die Spielschleife
	 * @return this
	 */
	public Timeline with(SpriteGameLoop spriteGameLoop) {
		return with(spriteGameLoop, DEFAULT_DURATION);
	}

	/**
	 * der timelineDiesListener wird ausgeführt, wenn die Timeline endet
	 * 
	 * @param timelineDiesListener
	 *            der Listener
	 * @return this
	 */
	public Timeline onDeath(TimelineDiesListener timelineDiesListener) {
		this.timelineDiesListener = timelineDiesListener;
		return this;
	}

	/**
	 * Legt fest, dass die Timeline von vorne bis hinten wiederholt läuft. Default
	 * ist, dass endet, wenn alle Spielschleifen gelaufen sind.
	 * 
	 * @return this
	 */
	public Timeline cycle() {
		this.cycle = true;
		return this;
	}

	/**
	 * Startet die Timeline erneut.
	 * 
	 * @return this
	 */
	public Timeline restart() {
		this.actualItem = 0;
		this.offset = 0;
		this.running = true;
		return this;
	}

	@Override
	public void run(Sprite sprite, long ticks, long deltatime) {
		if (running && ticks - offset >= items.get(actualItem).duration()) {
			actualItem++;
			if (actualItem >= items.size()) {
				if (cycle) {
					actualItem = 0;
				} else {
					running = false;
					timelineDiesListener.onDeath(this);
				}
			}
			offset = ticks;
		}
		if (running) {
			items.get(actualItem).spriteGameLoop().run(sprite, ticks - offset, deltatime);
		}
	}
}
