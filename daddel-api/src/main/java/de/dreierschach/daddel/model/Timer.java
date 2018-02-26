package de.dreierschach.daddel.model;

import de.dreierschach.daddel.listener.TimeoutListener;

public class Timer {
	private long timeout;
	private long actualTime = 0;
	private boolean finished = false;
	private TimeoutListener timeoutListener;
	
	public Timer(long timeout, TimeoutListener timeoutListener) {
		this.timeout = timeout;
		this.timeoutListener = timeoutListener;
	}
	
	public boolean finished() {
		return finished;
	}
	
	public void gameLoop(long deltaTime) {
		if (finished) {
			return;
		}
		actualTime += deltaTime;
		if (actualTime >= timeout) {
			finished = true;
			timeoutListener.onTimeout();
		}
	}
}
