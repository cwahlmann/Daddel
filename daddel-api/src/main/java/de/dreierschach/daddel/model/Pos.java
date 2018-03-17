package de.dreierschach.daddel.model;

/**
 * Position in Spielraster-Punkten
 * 
 * @author Christian
 *
 */
public class Pos {
	private double x, y;

	/**
	 * @param x
	 *            X-Koordinate
	 * @param y
	 *            Y-Koordinate
	 */
	public Pos(double x, double y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * @return die X-Koordinate
	 */
	public double x() {
		return x;
	}

	/**
	 * @return die Y-Koordinate
	 */
	public double y() {
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
	public Pos mul(double a) {
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
	public Pos div(double a) {
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

	public double squareDistance(Pos other) {
		double dx = this.x - other.x;
		double dy = this.y - other.y;
		return dx * dx + dy * dy;
	}

}
