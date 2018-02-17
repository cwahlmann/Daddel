package de.dreierschach.daddel.model;

public class HighscoreEntry implements Comparable<HighscoreEntry> {
	public String name;
	public int score;

	public HighscoreEntry(String name, int score) {
		super();
		this.name = name;
		this.score = score;
	}

	@Override
	public int compareTo(HighscoreEntry highscoreEintrag) {
		if (highscoreEintrag == null) {
			return 1;
		}
		return highscoreEintrag.score - this.score;
	}
}
