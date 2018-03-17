package de.dreierschach.daddel.model;

/**
 * Speichert Informationen zur Umrechnung von Spielraster-Punkten in
 * Bildschirmkoordinaten
 * 
 * @author Christian
 *
 */
public class Transformation {
	private double zoom;
	private Scr trans;
	private Pos rasterLeftUpper, rasterRightBottom;
	private int width, height;

	/**
	 * @param width
	 *            Breite des Bildschirms
	 * @param height
	 *            Höhe des Bildschirms
	 * @param rasterLeftUpper
	 *            Linke obere Ecke des Rasters in Spielraster-Punkten
	 * @param rasterRightBottom
	 *            Rechte untere Ecke des Rasters in Spielraster-Punkten
	 */
	public Transformation(int width, int height, Pos rasterLeftUpper, Pos rasterRightBottom) {
		this.width = width;
		this.height = height;
		this.rasterLeftUpper = rasterLeftUpper;
		this.rasterRightBottom = rasterRightBottom;
		init();
	}

	/**
	 * @param width
	 *            Breite des Bildschirms
	 * @param height
	 *            Höhe des Bildschirms
	 */
	public Transformation(int width, int height) {
		this(width, height, new Pos(-1, -1), new Pos(1, 1));
	}

	/**
	 * @param rasterLeftUpper
	 *            Linke obere Ecke des Rasters in Spielraster-Punkten
	 * @param rasterRightBottom
	 *            Rechte untere Ecke des Rasters in Spielraster-Punkten
	 */
	public void setRaster(Pos rasterLeftUpper, Pos rasterRightBottom) {
		this.rasterLeftUpper = rasterLeftUpper;
		this.rasterRightBottom = rasterRightBottom;
		init();
	}

	/**
	 * @param width
	 *            Breite des Bildschirms
	 * @param height
	 *            Höhe des Bildschirms
	 */
	public void setScreenSize(int width, int height) {
		this.width = width;
		this.height = height;
		init();
	}

	/**
	 * @return der aktuelle Zoomfaktor
	 */
	public double getZoom() {
		return zoom;
	}

	/**
	 * @return die aktuelle x/y-Verschiebung
	 */
	public Scr getTrans() {
		return trans;
	}

	/**
	 * @return Linke obere Ecke des Rasters in Spielraster-Punkten
	 */
	public Pos getRasterLeftUpper() {
		return rasterLeftUpper;
	}

	/**
	 * @return Rechte untere Ecke des Rasters in Spielraster-Punkten
	 */
	public Pos getRasterRightBottom() {
		return rasterRightBottom;
	}

	/**
	 * @param scr
	 *            Position in Bildschirmkoordinaten
	 * @return die Position in Spielraster-Punkten
	 */
	public Pos t(Scr scr) {
		return new Pos( //
				((double) (scr.x() - trans.x())) / zoom + rasterLeftUpper.x(), //
				((double) (scr.y() - trans.y())) / zoom + rasterLeftUpper.y() //
		);
	}

	/**
	 * @param pos
	 *            Position in Spielraster-Punkten
	 * @return Position in Bildschirm-Koordinaten
	 */
	public Scr t(Pos pos) {
		return new Scr( //
				(int) ((pos.x() - rasterLeftUpper.x()) * zoom) + trans.x(), //
				(int) ((pos.y() - rasterLeftUpper.y()) * zoom) + trans.y() //
		);
	}

	/**
	 * @param scr
	 *            Position in Bildschirm-Koordinaten
	 * @return Position in Spielraster-Punkten
	 */
	public Pos zoom(Scr scr) {
		return new Pos( //
				((double) scr.x()) / zoom, //
				((double) scr.y()) / zoom);
	}

	/**
	 * @param pos
	 *            Position in Spielraster-Punkten
	 * @return Position in Bildschirm-Koordinaten
	 */
	public Scr zoom(Pos pos) {
		return new Scr( //
				(int) (pos.x() * zoom), //
				(int) (pos.y() * zoom));
	}

	/**
	 * @param i
	 *            Integer-Koordinate
	 * @return gezoomte Koordinate
	 */
	public double zoom(int i) {
		return ((double) i) / zoom;
	}

	/**
	 * @param f
	 *            Float-Koordinate
	 * @return gezoomte Koordinate
	 */
	public int zoom(double f) {
		return (int) (f * zoom);
	}

	// private Methoden

	private final void init() {
		double dx = Math.abs(rasterLeftUpper.x() - rasterRightBottom.x());
		double dy = Math.abs(rasterLeftUpper.y() - rasterRightBottom.y());
		double zoomx = (double) this.width / dx;
		double zoomy = (double) this.height / dy;
		this.zoom = zoomx < zoomy ? zoomx : zoomy;

		int scrDx = (int) (dx * zoom);
		int scrDy = (int) (dy * zoom);
		this.trans = new Scr((width - scrDx) / 2, (height - scrDy) / 2);
	}

}
