package de.dreierschach.daddel.model;

public class MapPos {
	int x = 0;
	int y = 0;
	int z = 0;

	public MapPos() {
	}

	public MapPos(int x, int y, int z) {
		super();
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public int x() {
		return x;
	}

	public int y() {
		return y;
	}

	public int z() {
		return z;
	}

	public MapPos add(MapPos p) {
		return new MapPos(this.x + p.x, this.y + p.y, this.z + p.z);
	}

	public MapPos sub(MapPos p) {
		return new MapPos(this.x - p.x, this.y - p.y, this.z - p.z);
	}

	public MapPos mul(int a) {
		return new MapPos(this.x * a, this.y * a, this.z * a);
	}

	public MapPos mul(float a) {
		return new MapPos((int) (this.x * a), (int) (this.y * a), (int) (this.z * a));
	}

	public MapPos div(int a) {
		return new MapPos(this.x / a, this.y / a, this.z / a);
	}

	public MapPos div(float a) {
		return new MapPos((int) (this.x / a), (int) (this.y / a), (int) (this.z / a));
	}

	public Pos toPos() {
		return new Pos(this.x, this.y);
	}

	public Scr toScr() {
		return new Scr(this.x, this.y);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + x;
		result = prime * result + y;
		result = prime * result + z;
		return result;
	}

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
