package de.dreierschach.daddel.model;

/**
 * Bildschirmkoordinaten
 * 
 * @author Christian
 *
 */
public class Scr {
	private int x = 0;
	private int y = 0;

	public Scr() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param x
	 *            die X-Koordinate
	 * @param y
	 *            die Y-Koordinate
	 */
	public Scr(int x, int y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * @return die X-Koordinate
	 */
	public int x() {
		return x;
	}

	/**
	 * @return die Y-Koordinate
	 */
	public int y() {
		return y;
	}

	/**
	 * Addition
	 * 
	 * @param p
	 *            die zu addierende Position
	 * @return das Ergebnis
	 */
	public Scr add(Scr p) {
		return new Scr(this.x + p.x, this.y + p.y);
	}

	/**
	 * Subtraktion
	 * 
	 * @param p
	 *            die zu subtrahierende Position
	 * @return das Ergebnis
	 */
	public Scr sub(Scr p) {
		return new Scr(this.x - p.x, this.y - p.y);
	}

	/**
	 * Integer-Multiplikation
	 * 
	 * @param a
	 *            der Faktor
	 * @return das Ergebnis
	 */
	public Scr mul(int a) {
		return new Scr(this.x * a, this.y * a);
	}

	/**
	 * Float-Multiplikation
	 * 
	 * @param a
	 *            der Faktor
	 * @return das Ergebnis
	 */
	public Scr mul(double a) {
		return new Scr((int) (this.x * a), (int) (this.y * a));
	}

	/**
	 * Integer-Division
	 * 
	 * @param a
	 *            der Divisor
	 * @return das Ergebnis
	 */
	public Scr div(int a) {
		return new Scr(this.x / a, this.y / a);
	}

	/**
	 * Float-Division
	 * 
	 * @param a
	 *            der Divisor
	 * @return das Ergebnis
	 */
	public Scr div(double a) {
		return new Scr((int) (this.x / a), (int) (this.y / a));
	}

	/**
	 * @return die Position in Spielraster-Punkten
	 */
	public Pos toPos() {
		return new Pos(this.x, this.y);
	}

	/**
	 * @return die Position in Kachel-Spielfeld-Koordinaten
	 */
	public MapPos toMapPos() {
		return new MapPos(this.x, this.y, 0);
	}

}
