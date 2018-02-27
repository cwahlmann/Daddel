package de.dreierschach.daddel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import de.dreierschach.daddel.Screen.Debug;
import de.dreierschach.daddel.audio.AudioLib;
import de.dreierschach.daddel.gfx.menu.MenuBuilder;
import de.dreierschach.daddel.gfx.roll.Roll;
import de.dreierschach.daddel.gfx.sprite.ImageSprite;
import de.dreierschach.daddel.gfx.sprite.Particle;
import de.dreierschach.daddel.gfx.sprite.ParticleSwarmBuilder;
import de.dreierschach.daddel.gfx.sprite.Sprite;
import de.dreierschach.daddel.gfx.text.TextParticle;
import de.dreierschach.daddel.gfx.text.TextSprite;
import de.dreierschach.daddel.gfx.tilemap.Entity;
import de.dreierschach.daddel.gfx.tilemap.TileMap;
import de.dreierschach.daddel.listener.InputListener;
import de.dreierschach.daddel.listener.KeyListener;
import de.dreierschach.daddel.listener.TimeoutListener;
import de.dreierschach.daddel.model.ParticleStrategy;
import de.dreierschach.daddel.model.Pos;
import de.dreierschach.daddel.model.Scr;
import de.dreierschach.daddel.model.SpriteGameLoop;
import de.dreierschach.daddel.model.Timer;
import de.dreierschach.daddel.model.Transformation;
import de.dreierschach.daddel.setup.Setup;
import de.dreierschach.daddel.util.FileUtils;
import javafx.application.Application;
import javafx.geometry.VPos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

/**
 * @author Christian
 *
 */
/**
 * @author Christian
 *
 */
/**
 * @author Christian
 *
 */
public abstract class Daddel extends Application {
	private static Logger log = Logger.getLogger(Daddel.class);

	// Defaults

	public final static String DEFAULT_WIDTH = "1360";
	public final static String DEFAULT_HEIGHT = "768";
	public final static String DEFAULT_FULLSCREEN = "false";

	// Properties:

	private Screen screen;
	private Stage stage;
	private Transformation transformation;
	private int level = 1;
	private Roll roll = null;
	private Setup setup = new Setup();
	private String setupFile = "";
	private int witdh = 1920;
	private int height = 1080;

	public final static String INI_SETUP_FILE = "setup-file";
	public final static String INI_WIDTH = "width";
	public final static String INI_HEIGHT = "height";
	public final static String INI_FULLSCREEN = "fullscreen";
	public List<Timer> timers = new ArrayList<>();

	// strategy constants

	public final static ParticleStrategy PARTICLE_BOUNCE = ParticleStrategy.bounce;
	public final static ParticleStrategy PARTICLE_IGNORE = ParticleStrategy.ignore;
	public final static ParticleStrategy PARTICLE_KILL = ParticleStrategy.kill;
	public final static ParticleStrategy PARTICLE_RESTART = ParticleStrategy.restart;
	public final static ParticleStrategy PARTICLE_REAPPEAR = ParticleStrategy.reappear;
	public final static ParticleStrategy PARTICLE_STOP = ParticleStrategy.stop;

	// text align constants

	public final static TextAlignment ALIGN_LEFT = TextAlignment.LEFT;
	public final static TextAlignment ALIGN_RIGHT = TextAlignment.RIGHT;
	public final static TextAlignment ALIGN_JUSTIFY = TextAlignment.JUSTIFY;
	public final static TextAlignment ALIGN_CENTER = TextAlignment.CENTER;

	public final static VPos VALIGN_TOP = VPos.TOP;
	public final static VPos VALIGN_BOTTOM = VPos.BOTTOM;
	public final static VPos VALIGN_CENTER = VPos.CENTER;
	public final static VPos VALIGN_BASELINE = VPos.BASELINE;

	// ======================== API methods ==

	public Timer timer(long timeout, TimeoutListener timeoutListener) {
		Timer timer = new Timer(timeout, timeoutListener);
		timers.add(timer);
		return timer;
	}

	// ------------------------ game phase methods ==

	/**
	 * Wird beim Start des Programms aufgerufen. Hier werden alle globalen
	 * Spielvariablen initialisiert, und die einzelnen Spielphasen definiert.
	 */
	public abstract void initGame();

	public Color background() {
		return screen.getBackground();
	};

	public Color foreground() {
		return screen.getForeground();
	};

	public Daddel background(Color background) {
		screen.setBackground(background);
		return this;
	};

	public Daddel foreground(Color foreground) {
		screen.setForeground(foreground);
		return this;
	};

	/**
	 * @author Christian Interface, um die einzelnen Spielphasen zu definieren
	 */
	public interface GamePhaseAction extends Runnable {
	}

	/**
	 * Startet die Spielphase "Title".
	 */
	public void toTitle() {
		clear();
		screen.setGameLoop((gesamtZeit, deltaZeit) -> basicScreenLoop(gesamtZeit, deltaZeit));
		runGamePhase(GamePhase.TITLE);
	}

	/**
	 * Startet die Spielphase "Intro".
	 */
	public void toIntro() {
		clear();
		screen.setGameLoop((gesamtZeit, deltaZeit) -> basicScreenLoop(gesamtZeit, deltaZeit));
		runGamePhase(GamePhase.INTRO);
	}

	/**
	 * Startet die Spielphase "Menu".
	 */
	public void toMenu() {
		clear();
		screen.setGameLoop((gesamtZeit, deltaZeit) -> basicScreenLoop(gesamtZeit, deltaZeit));
		runGamePhase(GamePhase.MENU);
	}

	/**
	 * Startet die Spielphase "Setup".
	 */
	public void toSetup() {
		clear();
		screen.setGameLoop((gesamtZeit, deltaZeit) -> basicScreenLoop(gesamtZeit, deltaZeit));
		runGamePhase(GamePhase.SETUP);
	}

	/**
	 * Startet die Spielphase "LevelIntro".
	 */
	public void toLevelIntro() {
		clear();
		screen.setGameLoop((gesamtZeit, deltaZeit) -> basicScreenLoop(gesamtZeit, deltaZeit));
		runGamePhase(GamePhase.LEVEL_INTRO);
	}

	/**
	 * Startet die Spielphase "Level".
	 */
	public void toLevel() {
		clear();
		screen.setGameLoop((gesamtZeit, deltaZeit) -> basicGameLoop(gesamtZeit, deltaZeit));
		runGamePhase(GamePhase.LEVEL);
	}

	/**
	 * Startet die Spielphase "GameOver".
	 */
	public void toGameOver() {
		clear();
		screen.setGameLoop((gesamtZeit, deltaZeit) -> basicScreenLoop(gesamtZeit, deltaZeit));
		runGamePhase(GamePhase.GAMEOVER);
	}

	/**
	 * Startet die Spielphase "WinGame".
	 */
	public void toWinGame() {
		clear();
		screen.setGameLoop((gesamtZeit, deltaZeit) -> basicScreenLoop(gesamtZeit, deltaZeit));
		runGamePhase(GamePhase.WINGAME);
	}

	/**
	 * Startet die Spielphase "Credits".
	 */
	public void toCredits() {
		clear();
		screen.setGameLoop((gesamtZeit, deltaZeit) -> basicScreenLoop(gesamtZeit, deltaZeit));
		runGamePhase(GamePhase.CREDITS);
	}

	/**
	 * Startet die Spielphase "Highscore".
	 */
	public void toHighscore() {
		clear();
		screen.setGameLoop((gesamtZeit, deltaZeit) -> basicScreenLoop(gesamtZeit, deltaZeit));
		runGamePhase(GamePhase.HIGHSCORE);
	}

	/**
	 * Definiert die bei der Spielphase "Title" auszuführende Aktion.
	 * 
	 * @param action
	 *            Aktion
	 * @return this
	 */
	public Daddel toTitle(GamePhaseAction action) {
		gamePhase(GamePhase.TITLE, action);
		return this;
	}

	/**
	 * Definiert die bei der Spielphase "Intro" auszuführende Aktion.
	 * 
	 * @param action
	 *            Aktion
	 * @return this
	 */
	public Daddel toIntro(GamePhaseAction action) {
		gamePhase(GamePhase.INTRO, action);
		return this;
	}

	/**
	 * Definiert die bei der Spielphase "Menu" auszuführende Aktion.
	 * 
	 * @param action
	 *            Aktion
	 * @return this
	 */
	public Daddel toMenu(GamePhaseAction action) {
		gamePhase(GamePhase.MENU, action);
		return this;
	}

	/**
	 * Definiert die bei der Spielphase "Setup" auszuführende Aktion.
	 * 
	 * @param action
	 *            Aktion
	 * @return this
	 */
	public Daddel toSetup(GamePhaseAction action) {
		gamePhase(GamePhase.SETUP, action);
		return this;
	}

	/**
	 * Definiert die bei der Spielphase "LevelIntro" auszuführende Aktion.
	 * 
	 * @param action
	 *            Aktion
	 * @return this
	 */
	public Daddel toLevelIntro(GamePhaseAction action) {
		gamePhase(GamePhase.LEVEL_INTRO, action);
		return this;
	}

	/**
	 * Definiert die bei der Spielphase "Level" auszuführende Aktion.
	 * 
	 * @param action
	 *            Aktion
	 * @return this
	 */
	public Daddel toLevel(GamePhaseAction action) {
		gamePhase(GamePhase.LEVEL, action);
		return this;
	}

	/**
	 * Definiert die bei der Spielphase "GameOver" auszuführende Aktion.
	 * 
	 * @param action
	 *            Aktion
	 * @return this
	 */
	public Daddel toGameOver(GamePhaseAction action) {
		gamePhase(GamePhase.GAMEOVER, action);
		return this;
	}

	/**
	 * Definiert die bei der Spielphase "WinGame" auszuführende Aktion.
	 * 
	 * @param action
	 *            Aktion
	 * @return this
	 */
	public Daddel toWinGame(GamePhaseAction action) {
		gamePhase(GamePhase.WINGAME, action);
		return this;
	}

	/**
	 * Definiert die bei der Spielphase "Credits" auszuführende Aktion.
	 * 
	 * @param action
	 *            Aktion
	 * @return this
	 */
	public Daddel toCredits(GamePhaseAction action) {
		gamePhase(GamePhase.CREDITS, action);
		return this;
	}

	/**
	 * Definiert die bei der Spielphase "Highscore" auszuführende Aktion.
	 * 
	 * @param action
	 *            Aktion
	 * @return this
	 */
	public Daddel toHighscore(GamePhaseAction action) {
		gamePhase(GamePhase.HIGHSCORE, action);
		return this;
	}

	/**
	 * Beendet das Programm nach Sichern des aktuellen Setup-Stands.
	 */
	public void exit() {
		setupSave();
		this.stage.close();
	}

	// ------------------------ debug methods --

	/**
	 * @return der Text-Sprite, mit dem Debug-Informationen angezeigt werden.
	 *         Enthält per default die aktuellen Frames Per Second (FPS) und kann in
	 *         der eigenen Spielschleife ergänzt werden.
	 * 
	 */
	public TextSprite debugInfo() {
		return screen.getDebugInfo();
	}

	/**
	 * Steuert die Aazeige des Debut-Text-Sprite
	 * 
	 * @param debug
	 *            true: Debug wird angezeit, false: Debug wird nicht angezeigt
	 */
	public void debug(Debug debug) {
		screen.setDebug(debug);
	}

	/**
	 * @return true, wenn die Debug-Anzeige aktiviert ist.
	 */
	public Debug debug() {
		return screen.getDebug();
	}

	// ------------------------ setup methods --

	/**
	 * @return Die Hashmap mit den aktuell gespeicherten Setup-Informationen, z.B.
	 *         eine Highscoreliste.
	 */
	public Setup getSetup() {
		return this.setup;
	}

	/**
	 * Speichert das aktuelle Setup auf Festplatte.
	 */
	public void setupSave() {
		this.setup.save(this.setupFile);
	}

	/**
	 * Lädt das Setup von Festplatte.
	 */
	public void setupLoad() {
		this.setup.load(this.setupFile);
	}

	// ------------------------ keyboard methods --

	/**
	 * Bindet eine Aktion an einen Tastendruck.
	 * 
	 * @param keyCode
	 *            Der KeyCode der Taste
	 * @param keyListener
	 *            Die auszuführende Aktion
	 */
	public void key(KeyCode keyCode, KeyListener keyListener) {
		getScreen().addKeyListener(keyCode, keyListener);
	}

	/**
	 * Entfernt die einer Taste zugeordnete Aktion
	 * 
	 * @param keyCode
	 *            Der KeyCode der Taste
	 * 
	 */
	public void key(KeyCode keyCode) {
		getScreen().removeKeyListener(keyCode);
	}

	/**
	 * Entfernt alle bestimmten Tasten zugeordnete Aktionen
	 */
	public void removeKeys() {
		getScreen().removeKeyListeners();
	}

	/**
	 * Aktiviert die Eingabe einer Textzeile. Bei Tastendruck wird die angegebene
	 * Aktion ausgeführt. Diese ist auch für die Anzeige auf dem Bildschirm
	 * verantwortlich, z.B. durch die Aktualisierung eines Text-Sprites.
	 * 
	 * @param laenge
	 *            Maximale Länge der Textzeile
	 * @param inputListener
	 *            Aktion, die bei Tastendruck ausgeführt wird
	 */
	public void input(int laenge, InputListener inputListener) {
		getScreen().clearInput();
		getScreen().setInputLaenge(laenge);
		getScreen().setInputListener(inputListener);
		getScreen().setEnableInput(true);
	}

	/**
	 * schaltet die Eingabe in eine Textzeile aus.
	 */
	public void noInput() {
		getScreen().setEnableInput(false);
		getScreen().setInputListener(input -> {
		});
	}

	/**
	 * @return der aktuelle Eingabe-String
	 */
	public String input() {
		return screen.getInputString();
	}

	/**
	 * @param inputString
	 *            setzt den aktuellen Eingabe-String
	 */
	public void input(String inputString) {
		screen.setInputString(inputString);
	}

	// ------------------------ sprite methods --

	/**
	 * erzeugt eine Animation, die einem Sprite zugeordnet werden kann
	 * 
	 * @param imageStart
	 *            Index des Startbilds der Animation
	 * @param imageEnd
	 *            Index des Startbilds der Animation
	 * @param bounce
	 *            true: animiere die Bilder in beide Richtungen
	 * @param speed
	 *            Geschwindigkeit der Animation in Frames / s
	 * @return die SpriteGameLoop, die an den Sprite angehangen werden kann
	 */
	public final static SpriteGameLoop animation(final int imageStart, final int imageEnd, final boolean bounce,
			final double speed) {
		return (sprite, total, delta) -> {
			if (bounce) {
				int d = imageEnd - imageStart;
				int d2 = d * 2;
				int actual = imageStart + ((int) ((double) total / 1000f * speed * (double) d) % d);
				if (actual < d) {
					((ImageSprite) sprite).actualImage(actual);
				} else {
					((ImageSprite) sprite).actualImage(d2 - actual + 1);
				}
				return;
			}
			int d = imageEnd - imageStart + 1;
			((ImageSprite) sprite).actualImage(((int) (total / 1000f * speed * (double) d)) % d);
		};
	}

	/**
	 * Erzeugt einen neuen Sprite und hängt ihn in die View-Hierarchie ein.
	 * 
	 * @param type
	 *            Benutzerdefinierter Typ, ein Integer
	 * @param groesse
	 *            Die maximale Breite und Höhe des Sprite, in Spielrasterpunkten.
	 * @param bilder
	 *            Die einzelnen Bilder (Frames) des Sprite. Diese können über die
	 *            Methode animation() gesteuert werden.
	 * @return Eine neue Instanz der Klasse ImageSprite
	 */
	public ImageSprite sprite(int type, double groesse, String... bilder) {
		ImageSprite sprite = new ImageSprite(transformation, type, groesse, bilder);
		screen.addSprite(sprite);
		return sprite;
	}

	/**
	 * Ein Partikel ist ein Sprite mit begrenzter lebensdauer, der automatisch
	 * animiert wird. Erzeugt einen neuen Partikel und hängt ihn in die
	 * View-Hierarchie ein.
	 *
	 * @param pos
	 *            Start-Position des Partikel
	 * @param type
	 *            Benutzerdefinierter Typ, ein Integer
	 * @param lebensdauerMS
	 *            Die Lebensdauer des Partikel in ms.
	 * @param groesse
	 *            Die maximale Breite und Höhe des Sprite, in Spielrasterpunkten. *
	 * @param bilder
	 *            Die einzelnen Bilder (Frames) des Sprite. Diese können über die
	 *            Methode animation() gesteuert werden.
	 * @return eine neue Instanz der Klasse Particle.
	 */
	public Particle particle(Pos pos, int type, long lebensdauerMS, double groesse, String... bilder) {
		Particle particle = new Particle(transformation, type, groesse, lebensdauerMS, bilder).pos(pos);
		screen.addSprite(particle);
		return particle;
	}

	/**
	 * Ein Partikel ist ein Sprite mit begrenzter lebensdauer, der automatisch
	 * animiert wird. Erzeugt einen neuen Partikel und hängt ihn in die
	 * View-Hierarchie ein.
	 *
	 * @param type
	 *            Benutzerdefinierter Typ, ein Integer
	 * @param lebensdauerMS
	 *            Die Lebensdauer des Partikel in ms.
	 * @param groesse
	 *            Die maximale Breite und Höhe des Sprite, in Spielrasterpunkten. *
	 * @param bilder
	 *            Die einzelnen Bilder (Frames) des Sprite. Diese können über die
	 *            Methode animation() gesteuert werden.
	 * @return eine neue Instanz der Klasse Particle.
	 */
	public Particle particle(int type, long lebensdauerMS, double groesse, String... bilder) {
		Particle particle = new Particle(transformation, type, groesse, lebensdauerMS, bilder);
		screen.addSprite(particle);
		return particle;
	}

	/**
	 * Erzeugt einen Partikel-Schwarm-Builder. Ein Partikel-Schwarm ist ein Schwarm
	 * von Partikeln mit zufälliger Verteilung. Ein Partikel ist ein Sprite mit
	 * begrenzter lebensdauer, der automatisch animiert wird.
	 * 
	 * @param count
	 *            Anzahl zu erzeugender Partikel
	 * @param typ
	 *            Benutzerdefinierter Typ, ein Integer
	 * @param images
	 *            Die einzelnen Bilder (Frames) des Sprite. Diese können über die
	 *            Methode animation() gesteuert werden.
	 * @return eine Instanz der Klasse ParticleSwarmBuilder. Über die Methode
	 *         create() wird der Partikel erzeugt und in die View-Hierarchie
	 *         eingefügt.
	 */
	public ParticleSwarmBuilder particleSwarmBuilder(int count, int typ, String... images) {
		ParticleSwarmBuilder builder = new ParticleSwarmBuilder(count, transformation, typ, images,
				swarm -> swarm.getParticles().forEach(particle -> screen.addSprite(particle)));
		return builder;
	}

	/**
	 * Markiert alle Sprites eines Typs als gestorben. Sie werden dann bei der
	 * nächsten Gelegenheit aus der View-Hierarchie entfernt.
	 * 
	 * @param type
	 *            Benutzerdefinierter Typ, ein Integer
	 */
	public void killSprites(int type) {
		screen.getSprites().stream().filter(sprite -> sprite.type() == type).forEach(sprite -> sprite.kill());
	}

	/**
	 * Markiert alle Sprites als gestorben. Sie werden dann bei der nächsten
	 * Gelegenheit aus der View-Hierarchie entfernt.
	 */
	public void killallSprites() {
		screen.getSprites().stream().forEach(sprite -> sprite.kill());
	}

	// ------------------------ sound methods --

	/**
	 * Spielt unmittelbar den angegebenen Sampler ab.
	 * 
	 * @param path
	 *            Pfad des Samplers
	 */
	public void sound(String path) {
		sound(path, 1.0);
	}

	/**
	 * Spielt unmittelbar den angegebenen Sampler ab.
	 * 
	 * @param path
	 *            Pfad des Samplers
	 * @param volume
	 *            Lautstärke (0 ... 1.0)
	 */
	public void sound(String path, double volume) {
		sound(path, volume, 0.0);
	}

	/**
	 * Spielt unmittelbar den angegebenen Sampler ab.
	 * 
	 * @param path
	 *            Pfad des Samplers
	 * @param volume
	 *            Lautstärke (0 ... 1.0)
	 * @param balance
	 *            Balance (-1.0 ... 1.0)
	 */
	public void sound(String path, double volume, double balance) {
		sound(path, volume, balance, 1.0, balance, 1);
	}

	/**
	 * Spielt unmittelbar den angegebenen Sampler ab.
	 * 
	 * @param path
	 *            Pfad des Samplers
	 * @param volume
	 *            Lautstärke (0 ... 1.0)
	 * @param balance
	 *            relative Lauststärke des Samples bzgl. linker / rechter
	 *            Lautsprecher (-1.0 ... 1.0)
	 * @param rate
	 *            Geschwindigkeit (0.125 ... 8.0)
	 * @param pan
	 *            verschiebt die Mitte des Samples bzgl. linker / rechter
	 *            Lautsprecher (-1.0 ... 1.0)
	 * @param priority
	 *            Priorität, mit der der Sample abgespielt wird
	 */
	public void sound(String path, double volume, double balance, double rate, double pan, int priority) {
		String url = FileUtils.getInputUrl(path).toExternalForm();
		AudioLib.audioclip(url).play(volume, balance, rate, pan, priority);
	}

	// ------------------------ text methods --

	/**
	 * erzeugt ein Text-Sprite und fügt ihn der View-Hierarchie hinzu.
	 * 
	 * @param text
	 *            Der anzuzeigende Text
	 * @param family
	 *            Die Zeichensatz-Familie des Text-Sprites (z.B. sans-serif)
	 * @param size
	 *            Die Zeichensatz-Höhe des Texts
	 * @param color
	 *            Die Farbe des Texts
	 * @return eine Instanz der Klasse TextSprite
	 */
	public TextSprite text(String text, String family, double size, Color color) {
		TextSprite textSprite = new TextSprite(transformation, text).family(family).color(color).size(size);
		screen.addText(textSprite);
		return textSprite;
	}

	/**
	 * 
	 * erzeugt ein Text-Partikel und fügt ihn der View-Hierarchie hinzu. Ein
	 * Partikel ist ein Sprite mit begrenzter lebensdauer, der automatisch animiert
	 * wird.
	 * 
	 * @param text
	 *            Der anzuzeigende Text
	 * @param lebensdauer
	 *            Die Lebensdauer des Text-Partikels in ms
	 * @param family
	 *            Die Zeichensatz-Familie des Text-Sprites (z.B. sans-serif)
	 * @param size
	 *            Die Zeichensatz-Höhe des Texts
	 * @param color
	 *            Die Farbe des Texts
	 * @return eine Instanz der Klasse TextParticle
	 */
	public TextParticle textParticle(String text, long lebensdauer, String family, double size, Color color) {
		TextParticle textParticle = new TextParticle(transformation, lebensdauer, text).family(family).color(color)
				.size(size);
		screen.addText(textParticle);
		return textParticle;
	}

	/**
	 * Erzeugt einen Menu-Builder Ein Menu ist eine Liste von Optionen. Wird eine
	 * Option ausgewählt, dann wird eine bestimmte Aktion ausgeführt.
	 * 
	 * @return Eine Instanz der Klasse MenuBuilder. Mit der Methode create() wird
	 *         eine Instanz der Klasse Menu erzeugt und in die View-Hierarchie
	 *         eingebunden.
	 */
	public MenuBuilder menu() {
		return new MenuBuilder(transformation, screen);
	}

	/**
	 * Markiert alle Text-Sprites als gestorben. Sie werden dann bei der nächsten
	 * Gelegenheit aus der View-Hierarchie entfernt.
	 */
	public void killallText() {
		screen.getTexts().stream().forEach(text -> text.kill());
	}

	// ------------------------ tilemap methods --

	/**
	 * Erzeugt ein Spielfeld, das aus Kacheln zusammengesetzt ist. In diesem
	 * Spielfeld können sich Entities (besondere Sprites) bewegen.
	 * 
	 * @param tileSize
	 *            Größe einer Kachel in Spielraster-Punkten.
	 * @return eine Instanz der Klasse TileMap
	 */
	public TileMap tilemap(double tileSize) {
		TileMap tileMap = new TileMap(transformation, tileSize);
		screen.setTileMap(tileMap);
		return tileMap;
	}

	/**
	 * Erzeugt eine bewegliche Entität, die sich auf einer TileMap bewegen kann
	 * 
	 * @param type
	 *            Benutzerdefinierter Typ, Integer
	 * @param maxSize
	 *            Maximale Breite und Höhe der Entität in Spielrasterpunkten
	 * @param imagefiles
	 *            Die einzelnen Bilder (Frames) der Entität. Diese können über die
	 *            Methode animation() gesteuert werden.
	 * @return this
	 */
	public Entity entity(int type, double maxSize, String... imagefiles) {
		Entity entity = new Entity(transformation, screen.getTileMap(), type, maxSize, imagefiles);
		entity.parent(screen.getTileMap());
		screen.getTileMap().entity(entity);
		// screen.addSprite(entity);
		return entity;
	}

	// ------------------------ screen methods --

	/**
	 * @return Gibt die ScreenView des Spiels zurück
	 */
	public Screen getScreen() {
		return screen;
	}

	/**
	 * Definiert die Größe des Spielrasters (Grid). Der Zoom wird automatisch so
	 * gewählt, dass das gesamte Grid auf den Bildschirm passt.
	 * 
	 * @param x0
	 *            die linke Ausdehnung des Spielrasters
	 * @param x1
	 *            die rechte Ausdehnung des Spielrasters
	 * @param y0
	 *            die obere Ausdehnung des Spielrasters
	 * @param y1
	 *            die untere Ausdehnung des Spielrasters
	 * @return this
	 */
	public Daddel grid(double x0, double x1, double y0, double y1) {
		Pos pos0 = new Pos(x0, y0);
		Pos pos1 = new Pos(x1, y1);
		this.transformation.setRaster(pos0, pos1);
		Scr scr0 = this.transformation.t(pos0);
		Scr scr1 = this.transformation.t(pos1);
		getScreen().setClipping(scr0, scr1);
		getScreen().getDebugInfo().pos(transformation.getRasterLeftUpper());
		return this;
	}

	/**
	 * @return Linke Grenze des Rasters in Spielraster-Punkten
	 */
	public double gridLeft() {
		return transformation.getRasterLeftUpper().x();
	}

	/**
	 * @return Obere Grenze des Rasters in Spielraster-Punkten
	 */
	public double gridTop() {
		return transformation.getRasterLeftUpper().y();
	}

	/**
	 * @return Rechte Grenze des Rasters in Spielraster-Punkten
	 */
	public double gridRight() {
		return transformation.getRasterRightBottom().x();
	}

	/**
	 * @return Untere Grenze des Rasters in Spielraster-Punkten
	 */
	public double gridBottom() {
		return transformation.getRasterRightBottom().y();
	}

	/**
	 * Prüft, ob die angegebene Position im Spielraster liegt.
	 * 
	 * @param pos
	 *            die Position
	 * @return true: die Position liegt im Spielraster
	 */
	public boolean onGrid(Pos pos) {
		return pos.x() >= gridLeft() && pos.x() <= gridRight() && pos.y() >= gridTop() && pos.y() <= gridBottom();
	}

	/**
	 * Prüft, ob die angegebene Position im Spielraster liegt. Dabei wird der in
	 * padding angegebene Rand vom Spielraster abgezogen.
	 * 
	 * @param pos
	 *            die Position
	 * @param padding
	 *            gibt an, wieviel Rand einkalkuliert werden soll
	 * @return true: die Position liegt im Spielraster
	 */
	public boolean onGrid(Pos pos, double padding) {
		return pos.x() - padding >= gridLeft() && pos.x() + padding <= gridRight() && pos.y() - padding >= gridTop()
				&& pos.y() + padding <= gridBottom();
	}

	// ------------------------ utility methods --

	/**
	 * errechnet aus der verstrichenen Zeit die mit der angegebenen Geschwindigkeit
	 * zurückgelegte Strecke
	 * 
	 * @param delta
	 *            verstrichene Zeit in ms
	 * @param speed
	 *            Geschwindigkeit in Spielrasterpunkten / s
	 * @return zurückgelegte Strecke in Spielrasterpunkten
	 */
	public double strecke(long delta, double speed) {
		return (double) delta / (double) 1000 * speed;
	}

	/**
	 * Errechnet eine Position, die sich abhängig von der verstrichenen Zeit auf
	 * einer Kreisbahn (bzw. elliptischen Bahn) bewegt
	 * 
	 * @param delta
	 *            die verstrichene Zeit in ms
	 * @param wavelength
	 *            die Länge der Welle in ms
	 * @param min
	 *            die linke obere Ecke des Rechtecks, das die Größe des Kreises (der
	 *            Ellipse) bestimmt
	 * @param max
	 *            die rechte untere Ecke des Rechtecks, das die Größe des Kreises
	 *            (der Ellipse) bestimmt
	 * @return die errechnete Position
	 */
	public Pos circlePosition(long delta, long wavelength, Pos min, Pos max) {
		return new Pos( //
				cosinusWave(delta, wavelength, min.x(), max.x()), //
				sinusWave(delta, wavelength, min.y(), max.y()));
	}

	/**
	 * Errechnet einen Wert, der sich abhängig von der verstrichenen Zeit
	 * wellenförmig auf und ab bewegt
	 * 
	 * @param delta
	 *            die verstrichene Zeit in ms
	 * @param wavelength
	 *            die Länge der Welle in ms
	 * @param min
	 *            der untere Wert der Welle
	 * @param max
	 *            der obere Wert der Welle
	 * @return der errechnete Wert
	 */
	public double sinusWave(long delta, long wavelength, double min, double max) {
		double w = ((double) delta) / (double) wavelength * 2 * Math.PI;
		double r = (max - min) / 2;
		return (double) (Math.sin(w) * r + r + min);
	}

	/**
	 * Errechnet einen Wert, der sich abhängig von der verstrichenen Zeit
	 * wellenförmig auf und ab bewegt
	 * 
	 * @param delta
	 *            die verstrichene Zeit in ms
	 * @param wavelength
	 *            die Länge der Welle in ms
	 * @param min
	 *            der untere Wert der Welle
	 * @param max
	 *            der obere Wert der Welle
	 * @return der errechnete Wert
	 */
	public double cosinusWave(long delta, long wavelength, double min, double max) {
		double w = ((double) delta) / (double) wavelength * 2 * Math.PI;
		double r = (max - min) / 2;
		return (double) (Math.cos(w) * r + r + min);
	}

	// ------------------------ level methods --

	/**
	 * @return der aktuelle Level
	 */
	public int level() {
		return level;
	}

	/**
	 * setzt den aktuellen Lebel
	 * 
	 * @param level
	 *            zu setzender Level
	 */
	public void level(int level) {
		this.level = level;
	}

	/**
	 * startet den nächsten Level
	 */
	public void nextLevel() {
		this.level++;
		toLevelIntro();
	}

	// ------------------------ gameloop method --

	/**
	 * Diese Methode stellt die Spielschleife dar und wird pro Frame einmal
	 * aufgerufen.
	 * 
	 * @param gesamtZeit
	 *            Gesamte bisher verstrichene Zeit in ms
	 * @param deltaZeit
	 *            Seit dem letzten Frame verstrichene Zeit in ms
	 */
	public void gameLoop(long gesamtZeit, long deltaZeit) {
		// nichts zu tun
	}

	// Abspann

	public Roll roll() {
		this.roll = new Roll(getScreen(), transformation);
		return this.roll;
	}

	// ------------------------ private methods --

	// Lifecycle

	private enum GamePhase {
		TITLE, INTRO, MENU, SETUP, LEVEL_INTRO, LEVEL, GAMEOVER, WINGAME, CREDITS, HIGHSCORE
	}

	private Map<GamePhase, GamePhaseAction> gamePhases = new HashMap<GamePhase, GamePhaseAction>() {
		private static final long serialVersionUID = 1L;
		{
			// defaults
			put(GamePhase.TITLE, () -> toIntro());
			put(GamePhase.INTRO, () -> toMenu());
			put(GamePhase.MENU, () -> toLevelIntro());
			put(GamePhase.SETUP, () -> toMenu());
			put(GamePhase.LEVEL_INTRO, () -> toLevel());
			put(GamePhase.LEVEL, () -> toMenu());
			put(GamePhase.GAMEOVER, () -> toHighscore());
			put(GamePhase.WINGAME, () -> toHighscore());
			put(GamePhase.CREDITS, () -> exit());
			put(GamePhase.HIGHSCORE, () -> toMenu());
		}
	};

	private Daddel gamePhase(GamePhase gamePhase, GamePhaseAction action) {
		this.gamePhases.put(gamePhase, action);
		return this;
	}

	private void runGamePhase(GamePhase gamePhase) {
		if (gamePhases.containsKey(gamePhase)) {
			gamePhases.get(gamePhase).run();
		}
	}

	// screen loop
	private void basicScreenLoop(long gesamtZeit, long deltaZeit) {
		runSprites(deltaZeit);
		runTexts(deltaZeit);
		runTileMap(deltaZeit);
		runRoll(deltaZeit);
		runTimers(deltaZeit);
	}

	private void clear() {
		removeKeys();
		key(KeyCode.F3, keyCode -> debug(debug().next()));
		killallSprites();
		killallText();
	}

	// game loop

	private void basicGameLoop(long gesamtZeit, long deltaZeit) {
		runSprites(deltaZeit);
		runTexts(deltaZeit);
		runTileMap(deltaZeit);
		runTimers(deltaZeit);
		gameLoop(gesamtZeit, deltaZeit);
		checkCollisions();
	}

	private void runSprites(long delta) {
		List<Sprite> spritesCopy = new ArrayList<>(screen.getSprites());
		Iterator<Sprite> it = spritesCopy.iterator();
		while (it.hasNext()) {
			Sprite sprite = it.next();
			if (sprite.alive()) {
				sprite.gameLoop(delta);
			}
		}

		it = screen.getSprites().iterator();
		while (it.hasNext()) {
			Sprite sprite = it.next();
			if (!sprite.alive()) {
				it.remove();
			}
		}
	}

	private void runTexts(long delta) {
		List<TextSprite> textCopy = new ArrayList<>(screen.getTexts());
		Iterator<TextSprite> it = textCopy.iterator();
		while (it.hasNext()) {
			TextSprite textSprite = it.next();
			if (textSprite.alive()) {
				textSprite.gameLoop(delta);
			}
		}

		it = screen.getTexts().iterator();
		while (it.hasNext()) {
			TextSprite textSprite = it.next();
			if (!textSprite.alive()) {
				it.remove();
			}
		}
	}

	private void runTimers(long delta) {
		List<Timer> timersCopy = new ArrayList<>(timers);
		Iterator<Timer> it = timersCopy.iterator();
		while (it.hasNext()) {
			Timer timer = it.next();
			timer.gameLoop(delta);
		}

		it = timers.iterator();
		while (it.hasNext()) {
			Timer timer = it.next();
			if (timer.finished()) {
				it.remove();
			}
		}
	}

	private void runTileMap(long delta) {
		if (screen.getTileMap() == null) {
			return;
		}
		screen.getTileMap().gameLoop(delta);
	}

	private void checkCollisions() {
		List<Sprite> sprites = new ArrayList<>(screen.getSprites());
		if (screen.getTileMap() != null) {
			sprites.addAll(screen.getTileMap().entities());
		}
		for (int i = 0; i < sprites.size() - 1; i++) {
			if (sprites.get(i).alive()) {
				for (int j = i + 1; j < sprites.size(); j++) {
					if (sprites.get(i).collides(sprites.get(j))) {
						sprites.get(i).onCollision(sprites.get(j));
						sprites.get(j).onCollision(sprites.get(i));
					}
				}
			}
		}
	}

	private void runRoll(long delta) {
		if (this.roll != null) {
			this.roll.gameloop(delta);
		}
	}

	/**
	 * @return der Dateipfad der Setup-Datei
	 */
	public String getSetupFile() {
		return this.getClass().getSimpleName().toLowerCase() + "/setup.properties";
	}

	/**
	 * @return der Titel der Anwendung
	 */
	public String getTitle() {
		return this.getClass().getSimpleName();
	};

	private void initSetup() {
		setupLoad();
		setup.setIfNew(INI_WIDTH, DEFAULT_WIDTH);
		setup.setIfNew(INI_HEIGHT, DEFAULT_HEIGHT);
		setup.setIfNew(INI_FULLSCREEN, DEFAULT_FULLSCREEN);
		setupSave();
	}

	// ======================== Programmstart durch JavaFx ==

	/*
	 * (non-Javadoc)
	 * 
	 * @see javafx.application.Application#start(javafx.stage.Stage)
	 */
	@Override
	public void start(Stage stage) throws Exception {
		this.stage = stage;
		this.setupFile = getSetupFile();
		initSetup();
		this.witdh = Integer.valueOf(setup.get(INI_WIDTH, DEFAULT_WIDTH));
		this.height = Integer.valueOf(setup.get(INI_HEIGHT, DEFAULT_HEIGHT));
		boolean fullscreen = Boolean.valueOf(setup.get(INI_FULLSCREEN, DEFAULT_FULLSCREEN));

		stage.setFullScreen(fullscreen);

		this.transformation = new Transformation(this.witdh, this.height);
		screen = new Screen(this.witdh, this.height, new Font(12));
		screen.setDebugInfo(new TextSprite(transformation, "DEBUG").size(1f).color(Color.WHITE)
				.pos(transformation.getRasterLeftUpper()).align(TextAlignment.LEFT, VPos.TOP));
		screen.setTransformation(transformation);
		Scene scene = new Scene(screen.getPane(), witdh, height);
		if (fullscreen) {
			scene.setCursor(Cursor.NONE);
		}
		stage.setTitle(getTitle());
		stage.setScene(scene);
		stage.show();
		initGame();
		toTitle();
	}
}
