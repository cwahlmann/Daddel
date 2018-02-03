package de.dreierschach.daddel.model;

public class Scr {
	private int x,y;

	public Scr(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int x() {
		return x;
	}

	public int y() {
		return y;
	}
	
	public Scr add(Scr p) {
		return new Scr (this.x + p.x, this.y + p.y);
	}

	public Scr sub(Scr p) {
		return new Scr (this.x - p.x, this.y - p.y);
	}

	public Scr mul(int a) {
		return new Scr (this.x * a, this.y * a);
	}

	public Scr mul(float a) {
		return new Scr ((int) (this.x * a), (int) (this.y * a));
	}

	public Scr div(int a) {
		return new Scr (this.x / a, this.y / a);
	}

	public Scr div(float a) {
		return new Scr ((int) (this.x / a), (int) (this.y / a));
	}

	public Pos toPos() {
		return new Pos(this.x, this.y);
	}

	public MapPos toMapPos() {
		return new MapPos(this.x, this.y, 0);
	}

}
