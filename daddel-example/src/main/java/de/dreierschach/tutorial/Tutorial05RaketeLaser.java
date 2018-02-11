package de.dreierschach.tutorial;

import de.dreierschach.daddel.Daddel;
import de.dreierschach.daddel.gfx.sprite.ImageSprite;
import de.dreierschach.daddel.gfx.sprite.Sprite;
import de.dreierschach.daddel.model.Pos;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;

//Das Spiel erweitert die Spiele-API Daddel
public class Tutorial05RaketeLaser extends Daddel {

	// Sprites können einen Typ haben, z.B. einen für Spieler und einen für Gegner
	private final static int TYP_SPIELER = 1;
	private final static int TYP_GEGNER = 2;
	private final static int TYP_LASER = 3;

	// Die Größe der Rakete wird in Spielraster-Punkten angegeben
	private final static double RAKETE_GROESSE = 2f;
	private final static double GEGNER_GROESSE = 2f;

	// Startposition der Rakete
	private final static Pos RAKETE_STARTPOS = new Pos(0, 3.5f);

	// Die Geschwindigkeit der Rakete in Rasterpunkten pro Sekunde
	private final static double RAKETE_GESCHWINDIGKEIT = 5f;
	private final static double GEGNER_GESCHWINDIGKEIT = 3f;

	// Ein Enum ist einfach eine Aufzählung. Diese Aufzählung beinhaltet die
	// möglichen Richtungen der Rakete
	enum Richtung {
		stop, links, rechts, hoch, runter
	};

	// In dieser Variablen wird die aktuelle Richtung der Rakete gespeichert
	private Richtung raketeRichtung = Richtung.stop;

	// In dieser Variablen merke ich mir den Sprite Rakete
	private ImageSprite rakete;

	// In dieser Methode wird das Spiel einmal initialisiert.
	@Override
	public void initGame() {
		// Das Grid ist ein Raster, das über den ganzen Bildschirm gelegt wird. Die
		// Kästchen sind quadratisch.
		grid(-10, 10, -5, 5);

		// Bestimme die Hintergrundfarbe
		background(Color.rgb(0, 0, 32));

		// Für jede Phase des Spiels kann eine Methode festgelegt werden. Hier reicht
		// die Phase Level, also das Spielen eines Levels.
		toLevel(() -> startLevel());
	}

	// Hier wird ein Level gestartet
	private void startLevel() {
		erzeugeRakete();
		erzeugeGegner();
		definiereSteuerung();
	}

	private void erzeugeRakete() {
		// erzeuge die Rakete
		rakete = sprite(TYP_SPIELER, RAKETE_GROESSE, GFX_ROCKET, GFX_ROCKET_SCHIRM) //
				.pos(RAKETE_STARTPOS) //
				// In der Spielschleife der Rakete wird diese bewegt
				.gameLoop((me, totaltime, deltatime) -> {
					// Die Strecke kann mit der vordefinierten Methode strecke() aus delta-Zeit und
					// Geschwindigkeit errechnet werden
					double strecke = strecke(deltatime, RAKETE_GESCHWINDIGKEIT);
					bewegeRakete(strecke);
				}) //
				.collision((me, other) -> exit()) //
				// berechne einen kleineren Radius für die Kollisionskontrolle
				// (statt die Hälfte nur ein Viertel der Größe des Ufos)
				.r(RAKETE_GROESSE / 4f);
	}

	private void erzeugeGegner() {
		// erzeuge Ufos
		for (int i = 0; i < 3 + level(); i++) {
			// zufällige Position
			Pos pos = new Pos((double) Math.random() * 20f - 10f, (double) Math.random() * 5f - 5f);
			sprite(TYP_GEGNER, GEGNER_GROESSE, GFX_UFO_1) //
					.pos(pos) //
					.gameLoop((ufo, totaltime, deltatime) -> bewegeUfo(ufo, deltatime)) //
					// berechne einen kleineren Radius für die Kollisionskontrolle
					// (statt die Hälfte nur ein Drittel der Größe des Ufos)
					.r(GEGNER_GROESSE / 3f);
		}
	}

	private void definiereSteuerung() {
		// Je nach Taste wird eine andere Richtung eingeschlagen
		key(KeyCode.LEFT, keyCode -> raketeRichtung = Richtung.links);
		key(KeyCode.RIGHT, keyCode -> raketeRichtung = Richtung.rechts);
		key(KeyCode.UP, keyCode -> raketeRichtung = Richtung.hoch);
		key(KeyCode.DOWN, keyCode -> raketeRichtung = Richtung.runter);
		key(KeyCode.CONTROL, keyCode -> raketeRichtung = Richtung.stop);

		// die Leertaste feuert einen Laser ab
		key(KeyCode.SPACE, keyCode -> laserAbfeuern());

		// Wenn die Taste ESC gedrückt wird, wird das Programm beendet
		key(KeyCode.ESCAPE, keyCode -> exit());
	}

	// Methode, um die Rakete in die richtige Richtung zu bewegen.
	private void bewegeRakete(double strecke) {
		Pos neuePosition = rakete.pos().add(getPosRichtung(raketeRichtung, strecke));
		if (!onGrid(neuePosition, rakete.r())) {
			// Wenn die Rakete aus dem Bildschirm fliegen würde, wird sie gestoppt
			raketeRichtung = Richtung.stop;
		} else {
			// Ansonsten wird die Position verändert
			rakete.pos(neuePosition);
		}
	}

	// Diese Methode gibt je nach Richtung die richtige Positions-Veränderung zurück
	private Pos getPosRichtung(Richtung richtung, double strecke) {
		switch (raketeRichtung) {
		case links:
			return new Pos(-strecke, 0);
		case rechts:
			return new Pos(strecke, 0);
		case hoch:
			return new Pos(0, -strecke);
		case runter:
			return new Pos(0, strecke);
		case stop:
		default:
			return new Pos(0, 0);
		}
	}

	// Dies Methode wird von der Sprite-Spielschleife jedes Ufos aufgerufen
	private void bewegeUfo(Sprite ufo, long deltatime) {
		// berechne die Strecke aus Zeitspanne und Geschwindigkeit
		double strecke = strecke(deltatime, GEGNER_GESCHWINDIGKEIT);

		// berechne die neue Position
		Pos neuePosition = ufo.pos().add(new Pos(0, strecke));

		// wenn das Ufo unten ankommt, wird es an den oberen Bildschirmrand gesetzt. Die
		// X-Position ist zufällig.
		if (neuePosition.y() > 6) {
			neuePosition = new Pos((double) Math.random() * 20f - 10f, -6);
		}
		// setze neue Position
		ufo.pos(neuePosition);

	}

	// Diese Methode wird bei Drücken der Leertaste aufgerufen. Sie erzeugt einen
	// neuen Laserstrahl, der Ufos abschiesst.
	private void laserAbfeuern() {
		// Ein Partikel wird automatisch gesteuert und hat eine begrenzte Lebensdauer.
		// Dieser hier bewegt sich bis zum oberen Bildschirmrand und reagiert auf eine
		// Kollision mit einem Ufo.
		// Die Lebensdauer beträgt 0 Millisekunden (= unendlich). Die Größe ist ein
		// halber (0.5) Rasterpunkt.
		particle(TYP_LASER, 0, 0.5f, GFX_LASER) //
				// Die Startposition ist ein Rasterpunkt über der Rakete.
				.pos(rakete.pos().add(new Pos(0, -1))) //
				// der Laser soll nach oben fliegen (rechts = 0 Grad, unten = 90 Grad, links =
				// 180 Grad)
				.direction(-90) //
				// Die Geschwindigkeit ist 12 Rasterpunkte pro Sekunde
				.speed(12f)
				// Wenn der Laser das Raster verlässt (am oberen Bildschirmrand), wird er
				// entfernt
				.outsideGrid(PARTICLE_KILL);
	}

	// ===================== Standart-Main-Methode, um das Programm zu starten

	// Diese Methode muss vorhanden sein, damit das Spiel überhaupt gestartet werden
	// kann.
	// Sie ist immer gleich.
	public static void main(String[] args) {
		launch(args);
	}
}
