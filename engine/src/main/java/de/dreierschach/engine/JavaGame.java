package de.dreierschach.engine;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

import de.dreierschach.engine.audio.AudioLib;
import de.dreierschach.engine.gfx.menu.MenuBuilder;
import de.dreierschach.engine.gfx.sprite.ImageSprite;
import de.dreierschach.engine.gfx.sprite.Particle;
import de.dreierschach.engine.gfx.sprite.ParticleSwarmBuilder;
import de.dreierschach.engine.gfx.sprite.Sprite;
import de.dreierschach.engine.gfx.sprite.SpriteGameLoop;
import de.dreierschach.engine.gfx.text.TextParticle;
import de.dreierschach.engine.gfx.text.TextSprite;
import de.dreierschach.engine.gfx.tilemap.Entity;
import de.dreierschach.engine.gfx.tilemap.TileMap;
import de.dreierschach.engine.listener.InputListener;
import de.dreierschach.engine.listener.KeyListener;
import de.dreierschach.engine.model.Pos;
import de.dreierschach.engine.model.Scr;
import de.dreierschach.engine.model.Transformation;
import de.dreierschach.engine.setup.Setup;
import javafx.application.Application;
import javafx.geometry.VPos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public abstract class JavaGame extends Application {
	private static Logger log = Logger.getLogger(JavaGame.class);

	// Sprites

	public final static String GFX_ROCKET = "gfx/invader/rocket.png";
	public final static String GFX_ROCKET_SCHIRM = "gfx/invader/rocket-schirm.png";
	public final static String GFX_LASER = "gfx/invader/laser-red.png";
	public final static String GFX_LASER_GEGNER = "gfx/invader/laser-green.png";

	public final static String GFX_UFO_1 = "gfx/invader/ufo-1.png";
	public final static String GFX_UFO_2 = "gfx/invader/ufo-2.png";

	public final static String GFX_STERN = "gfx/invader/stern.png";

	public final static String[] GFX_EXPLOSION = { "gfx/invader/explosion-1.png", "gfx/invader/explosion-2.png", "gfx/invader/explosion-3.png",
			"gfx/invader/explosion-4.png" };

	// Pacman

	public final static String GFX_PAC_BASHFUL_INKY_D0 = "gfx/pacman/bashful-inky_D0.png";
	public final static String GFX_PAC_BASHFUL_INKY_D1 = "gfx/pacman/bashful-inky_D1.png";
	public final static String GFX_PAC_BASHFUL_INKY_L0 = "gfx/pacman/bashful-inky_L0.png";
	public final static String GFX_PAC_BASHFUL_INKY_L1 = "gfx/pacman/bashful-inky_L1.png";
	public final static String GFX_PAC_BASHFUL_INKY_R0 = "gfx/pacman/bashful-inky_R0.png";
	public final static String GFX_PAC_BASHFUL_INKY_R1 = "gfx/pacman/bashful-inky_R1.png";
	public final static String GFX_PAC_BASHFUL_INKY_U0 = "gfx/pacman/bashful-inky_U0.png";
	public final static String GFX_PAC_BASHFUL_INKY_U1 = "gfx/pacman/bashful-inky_U1.png";
	public final static String GFX_PAC_DEADEYES_D = "gfx/pacman/deadeyes_D.png";
	public final static String GFX_PAC_DEADEYES_L = "gfx/pacman/deadeyes_L.png";
	public final static String GFX_PAC_DEADEYES_R = "gfx/pacman/deadeyes_R.png";
	public final static String GFX_PAC_DEADEYES_U = "gfx/pacman/deadeyes_U.png";
	public final static String GFX_PAC_FRUIT_ANANAS = "gfx/pacman/fruit_ananas.png";
	public final static String GFX_PAC_FRUIT_APPLE = "gfx/pacman/fruit_apple.png";
	public final static String GFX_PAC_FRUIT_CHERRY = "gfx/pacman/fruit_cherry.png";
	public final static String GFX_PAC_FRUIT_ORANGE = "gfx/pacman/fruit_orange.png";
	public final static String GFX_PAC_FRUIT_STRAWBERRY = "gfx/pacman/fruit_strawberry.png";
	public final static String GFX_PAC_GHOST_BLUE0 = "gfx/pacman/ghost_blue0.png";
	public final static String GFX_PAC_GHOST_BLUE1 = "gfx/pacman/ghost_blue1.png";
	public final static String GFX_PAC_GHOST_WHITE0 = "gfx/pacman/ghost_white0.png";
	public final static String GFX_PAC_GHOST_WHITE1 = "gfx/pacman/ghost_white1.png";
	public final static String GFX_PAC_PACMAN_DIE_0 = "gfx/pacman/pacman_die_0.png";
	public final static String GFX_PAC_PACMAN_DIE_1 = "gfx/pacman/pacman_die_1.png";
	public final static String GFX_PAC_PACMAN_DIE_2 = "gfx/pacman/pacman_die_2.png";
	public final static String GFX_PAC_PACMAN_DIE_3 = "gfx/pacman/pacman_die_3.png";
	public final static String GFX_PAC_PACMAN_DIE_4 = "gfx/pacman/pacman_die_4.png";
	public final static String GFX_PAC_PACMAN_L0 = "gfx/pacman/pacman_L0.png";
	public final static String GFX_PAC_PACMAN_L1 = "gfx/pacman/pacman_L1.png";
	public final static String GFX_PAC_PACMAN_L2 = "gfx/pacman/pacman_L2.png";
	public final static String GFX_PAC_PACMAN_L3 = "gfx/pacman/pacman_L3.png";
	public final static String GFX_PAC_PACMAN_LIFE = "gfx/pacman/pacman_life.png";
	public final static String GFX_PAC_PILLE_GROSS = "gfx/pacman/pille_gross.png";
	public final static String GFX_PAC_PILLE_KLEIN = "gfx/pacman/pille_klein.png";
	public final static String GFX_PAC_POKEY_CLYDE_D0 = "gfx/pacman/pokey-clyde_D0.png";
	public final static String GFX_PAC_POKEY_CLYDE_D1 = "gfx/pacman/pokey-clyde_D1.png";
	public final static String GFX_PAC_POKEY_CLYDE_L0 = "gfx/pacman/pokey-clyde_L0.png";
	public final static String GFX_PAC_POKEY_CLYDE_L1 = "gfx/pacman/pokey-clyde_L1.png";
	public final static String GFX_PAC_POKEY_CLYDE_R0 = "gfx/pacman/pokey-clyde_R0.png";
	public final static String GFX_PAC_POKEY_CLYDE_R1 = "gfx/pacman/pokey-clyde_R1.png";
	public final static String GFX_PAC_POKEY_CLYDE_U0 = "gfx/pacman/pokey-clyde_U0.png";
	public final static String GFX_PAC_POKEY_CLYDE_U1 = "gfx/pacman/pokey-clyde_U1.png";
	public final static String GFX_PAC_SCORE_200 = "gfx/pacman/score_200.png";
	public final static String GFX_PAC_SHADOW_BLINKY_D0 = "gfx/pacman/shadow-blinky_D0.png";
	public final static String GFX_PAC_SHADOW_BLINKY_D1 = "gfx/pacman/shadow-blinky_D1.png";
	public final static String GFX_PAC_SHADOW_BLINKY_L0 = "gfx/pacman/shadow-blinky_L0.png";
	public final static String GFX_PAC_SHADOW_BLINKY_L1 = "gfx/pacman/shadow-blinky_L1.png";
	public final static String GFX_PAC_SHADOW_BLINKY_R0 = "gfx/pacman/shadow-blinky_R0.png";
	public final static String GFX_PAC_SHADOW_BLINKY_R1 = "gfx/pacman/shadow-blinky_R1.png";
	public final static String GFX_PAC_SHADOW_BLINKY_U0 = "gfx/pacman/shadow-blinky_U0.png";
	public final static String GFX_PAC_SHADOW_BLINKY_U1 = "gfx/pacman/shadow-blinky_U1.png";
	public final static String GFX_PAC_SPEEDY_PINKY_D0 = "gfx/pacman/speedy-pinky_D0.png";
	public final static String GFX_PAC_SPEEDY_PINKY_D1 = "gfx/pacman/speedy-pinky_D1.png";
	public final static String GFX_PAC_SPEEDY_PINKY_L0 = "gfx/pacman/speedy-pinky_L0.png";
	public final static String GFX_PAC_SPEEDY_PINKY_L1 = "gfx/pacman/speedy-pinky_L1.png";
	public final static String GFX_PAC_SPEEDY_PINKY_R0 = "gfx/pacman/speedy-pinky_R0.png";
	public final static String GFX_PAC_SPEEDY_PINKY_R1 = "gfx/pacman/speedy-pinky_R1.png";
	public final static String GFX_PAC_SPEEDY_PINKY_U0 = "gfx/pacman/speedy-pinky_U0.png";
	public final static String GFX_PAC_SPEEDY_PINKY_U1 = "gfx/pacman/speedy-pinky_U1.png";
	public final static String GFX_PAC_WALL_GATE = "gfx/pacman/wall_gate.png";
	public final static String GFX_PAC_WALL_H = "gfx/pacman/wall_h.png";
	public final static String GFX_PAC_WALL_V = "gfx/pacman/wall_v.png";
	public final static String GFX_PAC_WALL_LO = "gfx/pacman/wall_lo.png";
	public final static String GFX_PAC_WALL_RO = "gfx/pacman/wall_ro.png";
	public final static String GFX_PAC_WALL_LU = "gfx/pacman/wall_lu.png";
	public final static String GFX_PAC_WALL_RU = "gfx/pacman/wall_ru.png";
	public final static String GFX_PAC_WALL_ROUND_LO = "gfx/pacman/wall_round_lo.png";
	public final static String GFX_PAC_WALL_ROUND_LU = "gfx/pacman/wall_round_lu.png";
	public final static String GFX_PAC_WALL_ROUND_RO = "gfx/pacman/wall_round_ro.png";
	public final static String GFX_PAC_WALL_ROUND_RU = "gfx/pacman/wall_round_ru.png";

	// Sounds

	public final static String AUDIO_ROCKET_LASER = "sound/laser-rocket.mp3";
	public final static String AUDIO_UFO_EXPLOSION = "sound/explosion-gegner.mp3";
	public final static String AUDIO_UFO_LASER = "sound/laser-gegner.mp3";
	public final static String AUDIO_ROCKET_EXPLOSION = "sound/explosion-rocket.mp3";

	// Defaults

	private int witdh = 1920;
	private int height = 1080;
	private Color background = Color.BLACK;
	private Color foreground = Color.WHITE;
	private Screen screen;
	private Stage stage;
	private Transformation transformation;
	private int level = 1;

	// Properties:

	private Properties properties;
	private Setup setup = new Setup();
	private String setupFile = DEFAULT_SETUP_FILE;
	private String iniFile = DEFAULT_INI_FILE;
	public final static String DEFAULT_INI_FILE = "config/game.ini";
	public final static String DEFAULT_SETUP_FILE = "config/setup.ini";
	public final static String INI_SETUP_FILE = "setup-file";
	public final static String INI_WIDTH = "width";
	public final static String INI_HEIGHT = "height";
	public final static String INI_FULLSCREEN = "fullscreen";

	// Lifecycle

	public LifecycleAction TO_TITLE = new LifecycleAction(() -> titleScreen());
	public LifecycleAction TO_INTRO = new LifecycleAction(() -> introScreen());
	public LifecycleAction TO_MENU = new LifecycleAction(() -> menuScreen());
	public LifecycleAction TO_SETUP = new LifecycleAction(() -> setupScreen());
	public LifecycleAction TO_LEVEL_INTRO = new LifecycleAction(() -> levelIntroScreen(level));
	public LifecycleAction TO_LEVEL = new LifecycleAction(() -> {
		screen.setGameLoop((gesamtZeit, deltaZeit) -> basicGameLoop(gesamtZeit, deltaZeit));
		startLevel(level);
	});
	public LifecycleAction TO_GAMEOVER = new LifecycleAction(() -> gameOver());
	public LifecycleAction TO_WINGAME = new LifecycleAction(() -> win());
	public LifecycleAction TO_CREDITS = new LifecycleAction(() -> credits());
	public LifecycleAction TO_HIGHSCORE = new LifecycleAction(() -> highscore());

	// default sprite gameloops

	public final static SpriteGameLoop animation(final int imageStart, final int imageEnd, final boolean bounce,
			final float speed) {
		return (sprite, total, delta) -> {
			if (bounce) {
				int d = imageEnd - imageStart;
				int d2 = d * 2;
				int actual = imageStart + ((int) ((float) total / 1000f * speed * (float) d) % d);
				if (actual < d) {
					((ImageSprite) sprite).actualImage(actual);
				} else {
					((ImageSprite) sprite).actualImage(d2 - actual + 1);
				}
				return;
			}
			int d = imageEnd - imageStart + 1;
			((ImageSprite) sprite).actualImage(((int) (total / 1000f * speed * (float) d)) % d);
		};
	}

	public class LifecycleAction {
		private Runnable action;

		public LifecycleAction(Runnable action) {
			this.action = action;
		}

		public void run() {
			clear();
			screen.setGameLoop((gesamtZeit, deltaZeit) -> basicScreenLoop(gesamtZeit, deltaZeit));
			action.run();
		}
	}

	@Override
	public void start(Stage stage) throws Exception {
		this.stage = stage;
		readIniFile(iniFile);
		this.witdh = Integer.valueOf(properties.getProperty(INI_WIDTH, "1920" + this.witdh));
		this.height = Integer.valueOf(properties.getProperty(INI_HEIGHT, "1080" + this.height));
		boolean fullscreen = Boolean.valueOf(properties.getProperty(INI_FULLSCREEN, "true"));
		this.setupFile = (String) properties.getProperty(INI_SETUP_FILE, DEFAULT_SETUP_FILE);
		setupLoad();

		stage.setFullScreen(fullscreen);

		this.transformation = new Transformation(this.witdh, this.height);
		screen = new Screen(witdh, height, new Font(12), foreground, background);
		screen.setDebugInfo(new TextSprite(transformation, "DEBUG").size(0.5f).color(Color.WHITE).relativePos(transformation.getRasterLeftUpper())
				.align(TextAlignment.LEFT, VPos.TOP));
		Scene scene = new Scene(screen.getPane(), witdh, height);
		if (fullscreen) {
			scene.setCursor(Cursor.NONE);
		}
		stage.setTitle("JavaGame");
		stage.setScene(scene);
		stage.show();
		TO_TITLE.run();
	}
	
	// API methods

	public void exit() {
		setupSave();
		this.stage.close();
	}

	public TextSprite debugInfo() {
		return screen.getDebugInfo();
	}
	
	public void debug(boolean debug) {
		screen.setDebug(debug);
	}
	
	public boolean debug() {
		return screen.isDebug();
	}

	public Setup getSetup() {
		return this.setup;
	}

	public void setupSave() {
		this.setup.save(this.setupFile);
	}

	public void setupLoad() {
		this.setup.load(this.setupFile);
	}

	public void key(KeyCode keyCode, KeyListener keyListener) {
		getScreen().addKeyListener(keyCode, keyListener);
	}

	public void key(KeyCode keyCode) {
		getScreen().removeKeyListener(keyCode);
	}

	public void removeKeys() {
		getScreen().removeKeyListeners();
	}

	public void input(int laenge, InputListener inputListener) {
		getScreen().clearInput();
		getScreen().setInputLaenge(laenge);
		getScreen().setInputListener(inputListener);
		getScreen().setEnableInput(true);
	}

	public void noInput() {
		getScreen().setEnableInput(false);
		getScreen().setInputListener(input -> {
		});
		getScreen().clearInput();
	}

	public ImageSprite sprite(int type, float groesse, String... bilder) {
		ImageSprite sprite = new ImageSprite(transformation, type, groesse, bilder);
		screen.addSprite(sprite);
		return sprite;
	}

	public Particle particle(int type, long lebensdauerMS, float groesse, String... bilder) {
		Particle particle = new Particle(transformation, type, groesse, lebensdauerMS, bilder);
		screen.addSprite(particle);
		return particle;
	}

	public ParticleSwarmBuilder particleSwarmBuilder(int count, int typ, String... images) {
		ParticleSwarmBuilder builder = new ParticleSwarmBuilder(count, transformation, typ, images,
				swarm -> swarm.getParticles().forEach(particle -> screen.addSprite(particle)));
		return builder;
	}

	public void killSprites(int type) {
		screen.getSprites().stream().filter(sprite -> sprite.type() == type).forEach(sprite -> sprite.kill());
	}

	public void killallSprites() {
		screen.getSprites().stream().forEach(sprite -> sprite.kill());
	}

	public void killallText() {
		screen.getTexts().stream().forEach(text -> text.kill());
	}

	public void sound(String path) {
		sound(path, 1.0);
	}

	public void sound(String path, double volume) {
		sound(path, volume, 0.0);
	}

	public void sound(String path, double volume, double balance) {
		sound(path, volume, balance, 1.0, balance, 1);
	}

	public void sound(String path, double volume, double balance, double rate, double pan, int priority) {
		String url = JavaGame.class.getResource(path).toExternalForm();
		AudioLib.audioclip(url).play(volume, balance, rate, pan, priority);
	}

	public TextSprite text(String text, String family, float size, Color color) {
		TextSprite textSprite = new TextSprite(transformation, text).family(family).color(color).size(size);
		screen.addText(textSprite);
		return textSprite;
	}

	public TextParticle textParticle(String text, long lebensdauer, String family, float size, Color color) {
		TextParticle textParticle = new TextParticle(transformation, lebensdauer, text).family(family).color(color)
				.size(size);
		screen.addText(textParticle);
		return textParticle;
	}

	public MenuBuilder menu() {
		return new MenuBuilder(transformation, screen);
	}

	public TileMap tilemap(float tileSize) {
		TileMap tileMap = new TileMap(transformation, tileSize);
		screen.setTileMap(tileMap);
		return tileMap;
	}
	
	public Entity entity(int type, float maxSize, String... imagefiles) {
		Entity entity = new Entity(transformation, screen.getTileMap(), type, maxSize, imagefiles);
		entity.parent(screen.getTileMap());
		screen.addSprite(entity);
		return entity;
	}

	public Screen getScreen() {
		return screen;
	}

	public void grid(float x0, float x1, float y0, float y1) {
		Pos pos0 = new Pos(x0, y0);
		Pos pos1 = new Pos(x1, y1);
		this.transformation.setRaster(pos0, pos1);
		Scr scr0 = this.transformation.t(pos0);
		Scr scr1 = this.transformation.t(pos1);
		getScreen().setClipping(scr0, scr1);
		getScreen().getDebugInfo().relativePos(transformation.getRasterLeftUpper());
	}

	public float strecke(long delta, float speed) {
		return (float) delta / (float) 1000 * speed;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public void nextLevel() {
		this.level++;
		TO_LEVEL_INTRO.run();
	}

	public void clear() {
		removeKeys();
		killallSprites();
		killallText();
	}
	// abstract methods

	public abstract void titleScreen();

	public abstract void menuScreen();

	public abstract void setupScreen();

	public abstract void introScreen();

	public abstract void levelIntroScreen(int level);

	public abstract void startLevel(int level);

	public abstract void gameLoop(long gesamtZeit, long deltaZeit);

	public abstract void win();

	public abstract void gameOver();

	public abstract void highscore();

	public abstract void credits();

	// private methods

	private void basicScreenLoop(long gesamtZeit, long deltaZeit) {
		runTileMap(deltaZeit);
		runSprites(deltaZeit);
		runTexts(deltaZeit);
	}

	private void basicGameLoop(long gesamtZeit, long deltaZeit) {
		runTileMap(deltaZeit);
		runSprites(deltaZeit);
		runTexts(deltaZeit);
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

	private void runTileMap(long delta) {
		if (screen.getTileMap() == null) {
			return;
		}
		screen.getTileMap().gameLoop(delta);
	}

	private void checkCollisions() {
		List<Sprite> sprites = new ArrayList<>(screen.getSprites());
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

	private void readIniFile(String inifile) {
		this.properties = new Properties();
		try {
			this.properties.load(new FileInputStream(inifile));
		} catch (IOException e) {
			log.warn("Konnte .ini-File nicht laden: " + inifile, e);
		}
	}
}
