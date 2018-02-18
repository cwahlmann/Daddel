package de.dreierschach.daddel.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class Highscore {

	public static final String SETUP_HIGHSCORE = "setup_highscore";

	private List<HighscoreEntry> highscores = new ArrayList<>();

	public Highscore() {
	}

	public Highscore init() {
		return init("FR3DVOMJUPITER", new int[] { 1000, 2000, 3000, 4000, 5000, 6000, 7000, 8000, 9000, 10000 });
	}
	
	public Highscore init(String name, int... scores) {
		for (int score : scores) {
			highscores.add(new HighscoreEntry(name, score));
		}
		Collections.sort(highscores);
		return this;
	}

	public Highscore insert(String name, int score) {
		highscores.add(new HighscoreEntry(name, score));
		Collections.sort(highscores);
		highscores.remove(highscores.size() - 1);
		return this;
	}

	public boolean isNewHighscore(int score) {
		return score > highscores.get(highscores.size() - 1).score;
	}

	public List<HighscoreEntry> highscores() {
		return new ArrayList<>(highscores);
	}

	public Highscore highscores(List<HighscoreEntry> highscores) {
		this.highscores = new ArrayList<>(highscores);
		Collections.sort(highscores);
		return this;
	}

	@Override
	public String toString() {
		AtomicInteger rank = new AtomicInteger(1);
		return highscores.stream()
				.map(entry -> String.format("%2d. %20s %08d", rank.getAndIncrement(), entry.name, entry.score))
				.collect(Collectors.joining("\n"));
	}
	
	public static void main(String[] args) {
		System.out.println(new Highscore());
	}
}
