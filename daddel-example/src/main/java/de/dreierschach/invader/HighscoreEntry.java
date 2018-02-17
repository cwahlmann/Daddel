package de.dreierschach.invader;

public class HighscoreEintrag implements Comparable<HighscoreEintrag> {
	public String name;
	public int score;

	public HighscoreEintrag(String name, int score) {
		super();
		this.name = name;
		this.score = score;
	}

	@Override
	public int compareTo(HighscoreEintrag highscoreEintrag) {
		if (highscoreEintrag == null) {
			return 1;
		}
		return highscoreEintrag.score - this.score;
	}
}
