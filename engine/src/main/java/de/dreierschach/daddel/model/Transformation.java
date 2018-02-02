package de.dreierschach.daddel.model;

public class Transformation {
	private float zoom;
	private Scr trans;
	private Pos rasterLeftUpper, rasterRightBottom;
	private int width, height;

	public Transformation(int width, int height, Pos rasterLeftUpper, Pos rasterRightBottom) {
		this.width = width;
		this.height = height;
		this.rasterLeftUpper = rasterLeftUpper;
		this.rasterRightBottom = rasterRightBottom;
		init();
	}
	
	public Transformation(int width, int height) {
		this(width, height, new Pos(-1,-1), new Pos(1,1));
	}

	public void setRaster(Pos rasterLeftUpper, Pos rasterRightBottom) {
		this.rasterLeftUpper = rasterLeftUpper;
		this.rasterRightBottom = rasterRightBottom;
		init();
	}
	
	public void setScreenSize(int width, int height) {
		this.width = width;
		this.height = height;
		init();		
	}
	
	private final void init() {
		float dx = Math.abs(rasterLeftUpper.x() - rasterRightBottom.x());
		float dy = Math.abs(rasterLeftUpper.y() - rasterRightBottom.y());
		float zoomx = (float)this.width / dx;
		float zoomy = (float)this.height / dy;
		this.zoom = zoomx < zoomy ? zoomx : zoomy; 
		
		int scrDx = (int)(dx * zoom);
		int scrDy = (int)(dy * zoom);
		this.trans = new Scr((width - scrDx) / 2, (height - scrDy) / 2);
	}

	public float getZoom() {
		return zoom;
	}
	
	public Scr getTrans() {
		return trans;
	}
	
	public Pos getRasterLeftUpper() {
		return rasterLeftUpper;
	}
	
	public Pos getRasterRightBottom() {
		return rasterRightBottom;
	}
	
	public Pos t(Scr scr) {
		return new Pos( //
				((float) (scr.x() - trans.x())) / zoom + rasterLeftUpper.x(), //
				((float) (scr.y() - trans.y())) / zoom + rasterLeftUpper.y() //
				);
	}

	public Scr t(Pos pos) {
		return new Scr( //
				(int) ((pos.x() - rasterLeftUpper.x()) * zoom) + trans.x(), //
				(int) ((pos.y() - rasterLeftUpper.y()) * zoom) + trans.y() //
				);
	}

	public Pos zoom(Scr scr) {
		return new Pos( //
				((float) scr.x()) / zoom, //
				((float) scr.y()) / zoom);
	}

	public Scr zoom(Pos pos) {
		return new Scr( //
				(int) (pos.x() * zoom), //
				(int) (pos.y() * zoom));
	}
	
	public float zoom(int i) {
		return ((float) i) / zoom;
	}

	public int zoom(float f) {
		return (int) (f * zoom);
	}
}
