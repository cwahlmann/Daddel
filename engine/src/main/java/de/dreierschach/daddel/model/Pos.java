package de.dreierschach.daddel.model;

public class Pos {
	private float x,y;

	public Pos(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public float x() {
		return x;
	}

	public float y() {
		return y;
	}

	public Pos add(Pos p) {
		return new Pos(this.x + p.x, this.y + p.y);
	}

	public Pos sub(Pos p) {
		return new Pos(this.x - p.x, this.y - p.y);
	}

	public Pos mul(float a) {
		return new Pos(this.x * a, this.y * a);
	}

	public Pos mul(int a) {
		return new Pos(this.x * a, this.y * a);
	}

	public Pos div(float a) {
		return new Pos(this.x / a, this.y / a);
	}

	public Pos div(int a) {
		return new Pos(this.x / a, this.y / a);
	}

	public MapPos toMapPos() {
		return new MapPos((int)this.x, (int)this.y, 0);
	}

	public Scr toScr() {
		return new Scr((int)this.x, (int)this.y);
	}

}
