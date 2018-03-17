package de.dreierschach.daddel.model;

/**
 * Speichert die Position auf einem gekachelten Spielfeld
 * 
 * @author Christian
 *
 */
public class MapPos {
	int x = 0;
	int y = 0;
	int z = 0;

	public MapPos() {
	}

	/**
	 * @param x
	 *            die X-Koordinate
	 * @param y
	 *            die Y-Koordinate
	 * @param z
	 *            die Tiefe-Koordinate
	 */
	public MapPos(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
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
	 * @return die Tiefe-Koordinate
	 */
	public int z() {
		return z;
	}

	/**
	 * Addition
	 * 
	 * @param p
	 *            zu addierender Wert
	 * @return das Ergebnis
	 */
	public MapPos add(MapPos p) {
		return new MapPos(this.x + p.x, this.y + p.y, this.z + p.z);
	}

	/**
	 * Subtraktion
	 * 
	 * @param p
	 *            zu subtrahierender Wert
	 * @return das Ergebnis
	 */
	public MapPos sub(MapPos p) {
		return new MapPos(this.x - p.x, this.y - p.y, this.z - p.z);
	}

	/**
	 * Integer-Multiplikation
	 * 
	 * @param a
	 *            der Faktor
	 * @return das Ergebnis
	 */
	public MapPos mul(int a) {
		return new MapPos(this.x * a, this.y * a, this.z * a);
	}

	/**
	 * Float-Multiplikation
	 * 
	 * @param a
	 *            der Faktor
	 * @return das Ergebnis
	 */
	public MapPos mul(double a) {
		return new MapPos((int) (this.x * a), (int) (this.y * a), (int) (this.z * a));
	}

	/**
	 * Integer-Division
	 * 
	 * @param a
	 *            der Divisor
	 * @return das Ergebnis
	 */
	public MapPos div(int a) {
		return new MapPos(this.x / a, this.y / a, this.z / a);
	}

	/**
	 * Float-Division
	 * 
	 * @param a
	 *            der Divisor
	 * @return das Ergebnis
	 */
	public MapPos div(double a) {
		return new MapPos((int) (this.x / a), (int) (this.y / a), (int) (this.z / a));
	}

	/**
	 * @return die Position in Spielraster-Punkten
	 */
	public Pos toPos() {
		return new Pos(this.x, this.y);
	}

	/**
	 * @return die Position in Bildschirm-Koordinaten
	 */
	public Scr toScr() {
		return new Scr(this.x, this.y);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + x;
		result = prime * result + y;
		result = prime * result + z;
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MapPos other = (MapPos) obj;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		if (z != other.z)
			return false;
		return true;
	}
}
