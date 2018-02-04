package de.dreierschach.daddel.model;

/**
 * Position in Spielraster-Punkten
 * 
 * @author Christian
 *
 */
public class Pos {
	private float x, y;

	/**
	 * @param x
	 *            X-Koordinate
	 * @param y
	 *            Y-Koordinate
	 */
	public Pos(float x, float y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * @return die X-Koordinate
	 */
	public float x() {
		return x;
	}

	/**
	 * @return die Y-Koordinate
	 */
	public float y() {
		return y;
	}

	/**
	 * Addition
	 * 
	 * @param p
	 *            die zu addierende Position
	 * @return das Ergebnis
	 */
	public Pos add(Pos p) {
		return new Pos(this.x + p.x, this.y + p.y);
	}

	/**
	 * Subtraktion
	 * 
	 * @param p
	 *            die zu subtrahierende Position
	 * @return das Ergebnis
	 */
	public Pos sub(Pos p) {
		return new Pos(this.x - p.x, this.y - p.y);
	}

	/**
	 * Float-Multiplikation
	 * 
	 * @param a
	 *            der Faktor
	 * @return das Ergebnis
	 */
	public Pos mul(float a) {
		return new Pos(this.x * a, this.y * a);
	}

	/**
	 * Integer-Multiplikation
	 * 
	 * @param a
	 *            der Faktor
	 * @return das Ergebnis
	 */
	public Pos mul(int a) {
		return new Pos(this.x * a, this.y * a);
	}

	/**
	 * Float-Division
	 * 
	 * @param a
	 *            der Divisor
	 * @return das Ergebnis
	 */
	public Pos div(float a) {
		return new Pos(this.x / a, this.y / a);
	}

	/**
	 * Integer-Division
	 * 
	 * @param a
	 *            der Divisor
	 * @return das Ergebnis
	 */
	public Pos div(int a) {
		return new Pos(this.x / a, this.y / a);
	}

	/**
	 * @return die Position in Kachel-Spielfeld-Koordinaten
	 */
	public MapPos toMapPos() {
		return new MapPos((int) this.x, (int) this.y, 0);
	}

	/**
	 * @return die Position in Bildschirmkoordinaten
	 */
	public Scr toScr() {
		return new Scr((int) this.x, (int) this.y);
	}

}
