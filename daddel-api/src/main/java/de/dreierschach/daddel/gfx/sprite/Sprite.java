package de.dreierschach.daddel.gfx.sprite;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.dreierschach.daddel.model.Pos;
import de.dreierschach.daddel.model.Scr;
import de.dreierschach.daddel.model.SpriteGameLoop;
import de.dreierschach.daddel.model.Transformation;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.transform.Rotate;

/**
 * Basisklasse für alle Arten von Sprites
 * 
 * @author Christian
 */
/**
 * @author Christian
 *
 */
public abstract class Sprite {
	public static final int NO_TYPE = -1;
	public static final Sprite NONE = new Sprite(null, NO_TYPE) {

		@Override
		public Sprite onCollision(Sprite other) {
			return null;
		}

		@Override
		public void draw(GraphicsContext g) {
		}
	};
	private Pos pos = new Pos(0, 0);
	private float r;
	private int type;
	private List<SpriteGameLoop> gameLoops = new ArrayList<>();
	private boolean alive = true;
	private long ticks = 0;
	private double rotation = 0;
	private double direction = 0;
	private Transformation transformation;
	private Sprite parent = null;
	private boolean debug = false;

	/**
	 * Sprite mit vorgegebenen Radius erzeigen
	 * 
	 * @param transformation
	 *            Informationen zur Umrechnung von Spielraster-Punkten in
	 *            Bildschirmpixel
	 * @param type
	 *            Benutzerdefinierter Typ, Integer
	 * @param r
	 *            Radius des Sprite, wird zur Kollisionserkennung verwendet
	 */
	public Sprite(Transformation transformation, int type, int r) {
		this.r = r;
		this.type = type;
		this.transformation = transformation;
	}

	/**
	 * Sprite mit Radius 1
	 * 
	 * @param transformation
	 *            Informationen zur Umrechnung von Spielraster-Punkten in
	 *            Bildschirmpixel
	 * @param type
	 *            Benutzerdefinierter Typ, Integer
	 */
	public Sprite(Transformation transformation, int type) {
		this(transformation, type, 1);
	}

	/**
	 * Legt fest, ob debug-Informationen angezeigt werden
	 * 
	 * @param debug
	 *            true, wenn debug Informationen angezeigt werden sollen
	 * @return this
	 */
	public Sprite debug(boolean debug) {
		this.debug = debug;
		return this;
	}

	/**
	 * @return true, wenn debug Informationen angezeigt werden sollen
	 */
	public boolean debug() {
		return debug;
	}

	/**
	 * @return aktuelle Position in Spielraster-Punkten, ggf. relativ zum
	 *         Eltern-Sprite
	 */
	public Pos pos() {
		return !hasParent() ? pos : new Pos(parent.pos().x() + pos.x(), parent.pos().y() + pos.y());
	}

	/**
	 * @return die relative Position in Spielraster-Punkten
	 */
	public Pos relativePos() {
		return pos;
	}

	/**
	 * Setzt die relative Position in Spielraster-Punkten
	 * 
	 * @param pos
	 *            die neue relative Position
	 * @return this
	 */
	public Sprite relativePos(Pos pos) {
		this.pos = pos;
		return this;
	}

	/**
	 * @return die aktuelle Drehrichtung des Sprite (0 ... 360)
	 */
	public double rotation() {
		return rotation;
	}

	/**
	 * setzt die aktuelle Drehrichtung des Sprite
	 * 
	 * @param rotation
	 *            die neue Drehrichtung (0 .. 360)
	 * @return this
	 */
	public Sprite rotation(double rotation) {
		this.rotation = rotation;
		return this;
	}

	/**
	 * Dreht das Sprite um den angegebenen Winkel
	 * 
	 * @param angle
	 *            der Winkel, um den gedreht werden soll (0 ... 360)
	 * @return this
	 */
	public Sprite rotate(double angle) {
		rotation += angle;
		return this;
	}

	/**
	 * @return die aktuelle Bewegungsrichtung (0 ... 360)
	 */
	public double direction() {
		return direction;
	}

	/**
	 * setzt die aktuelle Bewegungsrichtung (0 ... 360)
	 * 
	 * @param direction
	 *            Winkel in Grad (0 ... 360)
	 * @return this
	 */
	public Sprite direction(double direction) {
		this.direction = direction;
		return this;
	}

	/**
	 * @return der Radius in Spielraster-Punkten
	 */
	public float r() {
		return r;
	}

	/**
	 * setzt den Radius in Spielraster-Punkten. Wird für die Kollisionserkennung
	 * verwendet.
	 * 
	 * @param r
	 *            Radius in Spielraster-Punkten
	 * @return this
	 */
	public Sprite r(float r) {
		this.r = r;
		return this;
	}

	/**
	 * @return der benutzerdefinierte Typ des Sprites
	 */
	public int type() {
		return type;
	}

	/**
	 * setzt den benutzerdefinierte Typ des Sprites
	 * 
	 * @param type
	 *            der benutzerdefinierte Typ des Sprites, Integer
	 * @return this
	 */
	public Sprite type(int type) {
		this.type = type;
		return this;
	}

	/**
	 * prüft, ob dieser Sprite mit dem angegebenen Sprite kollidiert.
	 * Ausschlaggebend sind der Abstand und die Radien der Sprites.
	 * 
	 * @param other
	 *            der andere Sprite
	 * @return true, wenn eine Kollision vorliegt
	 */
	public boolean collides(Sprite other) {
		if (!other.alive()) {
			return false;
		}
		float dx = other.pos().x() - this.pos().x();
		float dy = other.pos().y() - this.pos().y();
		float dd = dx * dx + dy * dy;
		float dr = other.r + this.r;
		float ddr = dr * dr;
		return dd < ddr;
	}

	/**
	 * @return true, wenn der Sprite noch lebt
	 */
	public boolean alive() {
		return alive;
	}

	/**
	 * tötet den Sprite. Bei nächster Gelegenheit wird er aus der View-Hierarchie
	 * entfernt
	 */
	public void kill() {
		this.alive = false;
	}

	/**
	 * Fügt die angegebenen Aktionen zur Spielschleife des Sprite hinzu
	 * 
	 * @param gameLoops
	 *            die benutzerdefinierten Aktionen
	 * 
	 * @return this
	 */
	public Sprite gameLoop(SpriteGameLoop... gameLoops) {
		this.gameLoops.addAll(Arrays.asList(gameLoops));
		return this;
	}

	/**
	 * Wird intern ausgeführt
	 * 
	 * @param deltatime
	 *            die Zeitspanne seit dem letzten Aufruf in ms
	 */
	public void gameLoop(long deltatime) {
		for (SpriteGameLoop gameLoop : gameLoops) {
			gameLoop.run(this, ticks, deltatime);
		}
		ticks += deltatime;
	}

	/**
	 * @return die Informationen, um Bildschirmpixel in Spielraster-Punkten
	 *         umzurechnen
	 */
	public Transformation transformation() {
		return transformation;
	}

	/**
	 * Gibt den Eltern-Sprite zurück, null falls es keinen gibt.
	 * 
	 * @return den Eltern-Sprite
	 */
	public Sprite parent() {
		return parent;
	}

	/**
	 * Setzt den Eltern-Sprite; die effektive Position ist nun relativ zu diesem
	 * Sprite
	 * 
	 * @param parent
	 *            der neue Elternsprite
	 * @return this
	 */
	public Sprite parent(Sprite parent) {
		this.parent = parent;
		return this;
	}

	/**
	 * @return true, falls es einen Eltern-Sprite gibt
	 */
	public boolean hasParent() {
		return parent != null;
	}

	// ------------- interne methoden

	protected static void rotate(GraphicsContext g, double angle, Scr middle) {
		Rotate r = new Rotate(angle, middle.x(), middle.y());
		g.setTransform(r.getMxx(), r.getMyx(), r.getMxy(), r.getMyy(), r.getTx(), r.getTy());
	}

	protected long getTicks() {
		return ticks;
	}

	protected void setTicks(long ticks) {
		this.ticks = ticks;
	}

	public void drawSprite(GraphicsContext g) {
		rotate(g, rotation, transformation.t(pos()));
		draw(g);
	}

	public Sprite move(Pos direction) {
		this.pos = new Pos(pos.x() + direction.x(), pos.y() + direction.y());
		return this;
	}

	public Sprite move(float distance) {
		Rotate r = new Rotate(direction);
		Point2D v2d = r.transform(new Point2D(1, 0));
		Pos v = new Pos((float) (v2d.getX()), (float) (v2d.getY()));
		this.pos = new Pos(this.pos.x() + v.x() * distance, pos.y() + v.y() * distance);
		return this;
	}

	public abstract Sprite onCollision(Sprite other);

	public abstract void draw(GraphicsContext g);
}
