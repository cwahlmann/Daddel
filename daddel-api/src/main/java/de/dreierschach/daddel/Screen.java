package de.dreierschach.daddel;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import de.dreierschach.daddel.gfx.sprite.InvisibleSprite;
import de.dreierschach.daddel.gfx.sprite.Sprite;
import de.dreierschach.daddel.gfx.text.TextSprite;
import de.dreierschach.daddel.gfx.tilemap.TileMap;
import de.dreierschach.daddel.listener.InputListener;
import de.dreierschach.daddel.listener.KeyListener;
import de.dreierschach.daddel.model.GameLoop;
import de.dreierschach.daddel.model.Pos;
import de.dreierschach.daddel.model.Scr;
import de.dreierschach.daddel.model.Transformation;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

/**
 * Screen realisiert die Bildschirmdarstellung in JavaFx. Einziges
 * Darstellungselement ist ein Canvas.
 * 
 * @author Christian
 *
 */
public class Screen {
	private Pane pane;

	@FXML
	private Canvas output;
	private Map<KeyCode, KeyListener> keyListener = new HashMap<>();
	private InputListener inputListener = input -> {
	};
	private String inputString = "";
	private boolean enableInput = false;
	private int inputLaenge = 0;
	private SortedSet<Sprite> sprites = new TreeSet<>();
	private TileMap tileMap = null;
	private Color foreground = Color.WHITE;
	private Color background = Color.BLACK;
	private Transformation transformation;
	private GameLoop gameLoop = (gesamtZeit, deltaZeit) -> {
	};
	private Sprite mouse = Sprite.NONE;
	private Sprite mouseParent = new InvisibleSprite(transformation, 0, 0, 1).pos(0, 0);
	private boolean mouseVisible = false;

	// debug

	public enum Debug {
		off(false, false), info(false, true), wireframe(true, false), wireframeInfo(true, true);
		private boolean isWireframe, isInfo;

		Debug(boolean isWireframe, boolean isInfo) {
			this.isInfo = isInfo;
			this.isWireframe = isWireframe;
		}

		public boolean wireframe() {
			return isWireframe;
		}

		public boolean info() {
			return isInfo;
		}

		public Debug next() {
			return Debug.values()[(this.ordinal() + 1) % Debug.values().length];
		}
	}

	private Debug debug = Debug.off;
	private TextSprite debugInfo = null;
	private double framerateMin = 9999.9;
	private double framerateMax = 0;

	final LongProperty elapsedTime = new SimpleLongProperty();
	final LongProperty framesTotal = new SimpleLongProperty(0);
	final LongProperty elapsedTimeTotal = new SimpleLongProperty(1);
	final AnimationTimer timer = new AnimationTimer() {
		@Override
		public void handle(long nano) {
			if (elapsedTime.get() == 0) {
				elapsedTime.set(nano);
			}
			long delta = nano - elapsedTime.get();
			elapsedTime.set(nano);
			framesTotal.set(framesTotal.get() + 1);
			elapsedTimeTotal.set(elapsedTimeTotal.get() + delta);
			if (debug.info()) {
				double fps = 1000000000d / ((double) delta);
				if (fps < framerateMin) {
					framerateMin = fps;
				}
				if (fps > framerateMax) {
					framerateMax = fps;
				}
				double fpsAvg = 1000000000d * (double) framesTotal.get() / ((double) elapsedTimeTotal.get());

				debugInfo.text(String.format("fps: %.1f (min: %.1f / max: %.1f / avg: %.1f)", fps, framerateMin,
						framerateMax, fpsAvg)).color(foreground);
			}
			gameLoop.run(nano / 1000000, delta / 1000000);
			refresh();
		}
	};

	/**
	 * @param screenWidth
	 *            Fensterbreite
	 * @param screenHeight
	 *            Fensterhöhe
	 * @param font
	 *            Standart-Zeichensatz
	 * @throws IOException
	 *             wird geworfen, wenn die Resource "screen.fxml" nicht geladen
	 *             werden kann
	 */
	public Screen(int screenWidth, int screenHeight, Font font) throws IOException {

		FXMLLoader loader = new FXMLLoader(getClass().getResource("screen.fxml"));
		loader.setController(this);
		pane = loader.load();
		output.setWidth(screenWidth);
		output.setHeight(screenHeight);

		output.setOnKeyTyped(keyEvent -> onOutputKeyTyped(keyEvent));

		output.setFocusTraversable(true);
		GraphicsContext g = output.getGraphicsContext2D();
		g.setFont(font);
		g.setTextAlign(TextAlignment.CENTER);
		g.setTextBaseline(VPos.CENTER);
		g.setFill(background);
		g.fillRect(0, 0, output.getWidth(), output.getHeight());

		// output.setClip(new Rectangle(0, 0, screenWidth, screenHeight));
		timer.start();
	}

	public void setTransformation(Transformation transformation) {
		this.transformation = transformation;
	}

	public Color getForeground() {
		return foreground;
	}

	public Color getBackground() {
		return background;
	}

	public void setForeground(Color foreground) {
		this.foreground = foreground;
		output.setStyle(//
				"-fx-color: " + getRGB(foreground) + "; " //
		);
	}

	public void setBackground(Color background) {
		this.background = background;
		output.setStyle(//
				"-fx-background-color: " + getRGB(background) + "; ");
	}

	/**
	 * Aktiviert / deaktiviert die Debug-Anzeige
	 * 
	 * @param debug
	 *            true: Debug-Informationen werden angezeigt (Standart: FPS)
	 */
	public void setDebug(Debug debug) {
		this.debug = debug;
	}

	/**
	 * @return true, wenn die Debug-Anzeige aktiviert ist
	 */
	public Debug getDebug() {
		return debug;
	}

	/**
	 * legt den Textsprite fest, der im Debug-Modus angezeigt wird
	 * 
	 * @param debugInfo
	 *            ein TextSprite
	 */
	public void setDebugInfo(TextSprite debugInfo) {
		this.debugInfo = debugInfo;
	}

	/**
	 * @return Textsprite, der im Debug-Modus angezeigt wird
	 */
	public TextSprite getDebugInfo() {
		return debugInfo;
	}

	/**
	 * Wandelt ein Farbobjekt in einen entsprechenden CSS-Farbcode um; wird für die
	 * Textdarstellung verwendet
	 * 
	 * @param color
	 *            Farbe
	 * @return Farbcode
	 */
	public String getRGB(Color color) {
		return String.format("#%02X%02X%02X", ((int) color.getRed()) * 255, ((int) color.getGreen()) * 255,
				((int) color.getBlue()) * 255);
	}

	/**
	 * Legt den anzuzeigenden Bildschirmausschnitt fest
	 * 
	 * @param scr0
	 *            linke, obere Ecke
	 * @param scr1
	 *            rechte, untere Ecke
	 */
	public void setClipping(Scr scr0, Scr scr1) {
		output.setClip(new Rectangle(scr0.x(), scr0.y(), //
				scr1.x() - scr0.x(), scr1.y() - scr0.y()));
	}

	/**
	 * legt die Aktion fest, die bei jecem Durchlauf der Spielschleife durchgeführt
	 * werden soll
	 * 
	 * @param gameLoop
	 *            Aktion
	 */
	public void setGameLoop(GameLoop gameLoop) {
		this.gameLoop = gameLoop;
	}

	/**
	 * Legt eine Aktion fest, die bei Drücken einer bestimmten Taste ausgeführt
	 * werden soll
	 * 
	 * @param keyCode
	 *            der KeyCode der Taste, z.B. KeyCode.SPACE
	 * @param keyListener
	 *            die Aktion
	 */
	public void addKeyListener(KeyCode keyCode, KeyListener keyListener) {
		this.keyListener.put(keyCode, keyListener);
	}

	/**
	 * legt fest, dass für die angegebene Taste keine Aktion mehr ausgeführt werden
	 * soll
	 * 
	 * @param keyCode
	 *            der KeyCode der Taste, z.B. KeyCode.SPACE
	 */
	public void removeKeyListener(KeyCode keyCode) {
		this.keyListener.remove(keyCode);
	}

	/**
	 * legt fest, dass für keine Taste mehr keine Aktion mehr ausgeführt werden soll
	 */
	public void removeKeyListeners() {
		this.keyListener.clear();
	}

	/**
	 * legt eine Aktion fest, die bei Eingabe eines Zeichens bei aktiviertem Input
	 * ausgeführt wird
	 * 
	 * @param inputListener
	 *            die Aktion
	 */
	public void setInputListener(InputListener inputListener) {
		this.inputListener = inputListener;
	}

	/**
	 * Löscht das Ergebnis des Inputs
	 */
	public void clearInput() {
		inputString = "";
	}

	/**
	 * Aktiviert Input, d.h. die Eingabe von Zeichen in einen Eingabe-String
	 * 
	 * @param enableInput
	 *            true: Input wird aktiviert
	 */
	public void setEnableInput(boolean enableInput) {
		this.enableInput = enableInput;
	}

	/**
	 * Legt die maximale Anzahl Zeichen des Input-String fest
	 * 
	 * @param inputLaenge
	 *            max. Länge des Input-Strings in Zeichen
	 */
	public void setInputLaenge(int inputLaenge) {
		this.inputLaenge = inputLaenge;
	}

	/**
	 * @return die aktuelle Eingabe
	 */
	public String getInputString() {
		return inputString;
	}

	/**
	 * @param inputString
	 *            setzt die aktuellen Eingabe auf den angegebenen Text
	 */
	public void setInputString(String inputString) {
		this.inputString = inputString;
	}

	/**
	 * Fügt einen Sprite hinzu, der auf dem Bildschirm angezeigt werden soll
	 * 
	 * @param sprite
	 *            der Sprite
	 */
	public void addSprite(Sprite sprite) {
		this.sprites.add(sprite);
	}

	/**
	 * entfernt einen Sprite aus der Liste anzuzeigender Sprites
	 * 
	 * @param sprite
	 *            der Sprite
	 * @return false: Sprite existiert nicht in der Liste
	 */
	public boolean deleteSprite(Sprite sprite) {
		if (!sprites.contains(sprite)) {
			return false;
		}
		this.sprites.remove(sprite);
		return true;
	}

	/**
	 * @return die Liste mit allen Sprites
	 */
	public SortedSet<Sprite> getSprites() {
		return sprites;
	}

	/**
	 * Fügt einen Text-Sprite hinzu, der auf dem Bildschirm angezeigt werden soll
	 * 
	 * @param text
	 *            der Text-Sprite
	 */
	public void addText(TextSprite text) {
		this.sprites.add(text);
		// this.texts.add(text);
	}

	/**
	 * entfernt einen Text-Sprite aus der Liste anzuzeigender Sprites
	 * 
	 * @param text
	 *            der Text-Sprite
	 * @return false: Text-Sprite existiert nicht in der Liste
	 */
	public boolean deleteText(TextSprite text) {
		if (!sprites.contains(text)) {
			// if (!texts.contains(text)) {
			return false;
		}
		this.sprites.remove(text);
		return true;
	}

	// /**
	// * @return einen Liste mit allen Text-Sprites
	// */
	// public SortedSet<TextSprite> getTexts() {
	// return texts;
	// }

	/**
	 * Legt das anzuzeigende gekachelte Spielfeld fest; ist es null, wird kein
	 * Spielfeld sngezeigt
	 * 
	 * @param tileMap
	 *            das Spielfeld
	 */
	public void setTileMap(TileMap tileMap) {
		this.tileMap = tileMap;
		sprites.add(tileMap);
	}

	/**
	 * @return das aktuell angezeigte gekachelte Spielfeld
	 */
	public TileMap getTileMap() {
		return tileMap;
	}

	/**
	 * @return der Canvas, auf dem alle Inhalte angezeigt werden
	 */
	public Pane getPane() {
		return pane;
	}

	// Mouse

	public void setMouse(Sprite mouse) {
		this.mouse = mouse.parent(mouseParent);
	}

	public Sprite getMouse() {
		return mouse;
	}

	public void setMouseSpot(Pos pos) {
		this.mouseParent.pos(pos.mul(-1));		
	}
	
	public void setMouseVisible(boolean mouseVisible) {
		this.mouseVisible = mouseVisible;
	}

	private void refresh() {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				GraphicsContext g = output.getGraphicsContext2D();
				g.setFill(background);
				g.fillRect(0, 0, output.getWidth(), output.getHeight());
				if (tileMap == null) {
					if (debug.info()) {
						drawGrid(g);
					}
					if (debug.wireframe()) {
						drawGrid(g);
						drawGridInfo(g);
					}
				}
				sprites.forEach(sprite -> {
					g.save();
					sprite.debug(getDebug());
					sprite.drawSprite(g);
					g.restore();
				});
				if (debug.info()) {
					g.save();
					debugInfo.draw(g);
					g.restore();
				}
				if (mouseVisible) {
					g.save();
					mouse.drawSprite(g);
					g.restore();
				}
			}
		});
	}

	private void drawGrid(GraphicsContext g) {
		g.setFill(Color.gray(0.5));
		int x0 = (int) transformation.getRasterLeftUpper().x();
		int x1 = (int) transformation.getRasterRightBottom().x();
		int y0 = (int) transformation.getRasterLeftUpper().y();
		int y1 = (int) transformation.getRasterRightBottom().y();
		for (int x = x0; x <= x1; x++) {
			for (int y = y0; y <= y1; y++) {
				Scr scr = transformation.t(new Pos((double) x, (double) y));
				Scr scr0 = transformation.t(new Pos((double) x0, (double) y0));
				Scr scr1 = transformation.t(new Pos((double) x1, (double) y1));
				g.fillRect(scr.x(), scr0.y(), x == 0 ? 2 : 1, scr1.y() - scr0.y());
				g.fillRect(scr0.x(), scr.y(), scr1.x() - scr0.x(), y == 0 ? 2 : 1);
			}
		}
	}

	private void drawGridInfo(GraphicsContext g) {
		Scr scr0 = transformation.t(transformation.getRasterLeftUpper());
		Scr scr1 = transformation.t(transformation.getRasterRightBottom());

		g.setFont(Font.font(24));

		g.setTextAlign(TextAlignment.LEFT);

		g.setTextBaseline(VPos.TOP);
		g.fillText(String.format("(%.3f / %.3f)", transformation.getRasterLeftUpper().x(),
				transformation.getRasterLeftUpper().y()), scr0.x(), scr0.y());

		g.setTextBaseline(VPos.BOTTOM);
		g.fillText(String.format("(%.3f / %.3f)", transformation.getRasterLeftUpper().x(),
				transformation.getRasterRightBottom().y()), scr0.x(), scr1.y());

		g.setTextAlign(TextAlignment.RIGHT);

		g.setTextBaseline(VPos.TOP);
		g.fillText(String.format("(%.3f / %.3f)", transformation.getRasterRightBottom().x(),
				transformation.getRasterLeftUpper().y()), scr1.x(), scr0.y());

		g.setTextBaseline(VPos.BOTTOM);
		g.fillText(String.format("(%.3f / %.3f)", transformation.getRasterRightBottom().x(),
				transformation.getRasterRightBottom().y()), scr1.x(), scr1.y());
	}

	public void onOutputKeyTyped(KeyEvent keyEvent) {
		manageInput(keyEvent);
		if (this.keyListener.containsKey(keyEvent.getCode())) {
			this.keyListener.get(keyEvent.getCode()).onKey(keyEvent.getCode());
		}
	}

	private void manageInput(KeyEvent keyEvent) {
		if (!enableInput) {
			return;
		}
		if (keyEvent.getCode().equals(KeyCode.BACK_SPACE)) {
			if (inputString.length() < 2) {
				inputString = "";
			} else {
				inputString = inputString.substring(0, inputString.length() - 1);
			}
			inputListener.onInput(inputString);
			return;
		}
		char c = keyEvent.getCharacter().charAt(0);
		if (inputString.length() < inputLaenge && Character.isAlphabetic(c) || c == '.' || c == '-' || c == ' '
				|| c == '_') {
			inputString += c;
			inputListener.onInput(inputString);
		}
	}
}
