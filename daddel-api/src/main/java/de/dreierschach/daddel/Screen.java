package de.dreierschach.daddel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.dreierschach.daddel.gfx.sprite.Sprite;
import de.dreierschach.daddel.gfx.text.TextSprite;
import de.dreierschach.daddel.gfx.tilemap.TileMap;
import de.dreierschach.daddel.listener.InputListener;
import de.dreierschach.daddel.listener.KeyListener;
import de.dreierschach.daddel.model.GameLoop;
import de.dreierschach.daddel.model.Scr;
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
	private List<Sprite> sprites = new ArrayList<>();
	private List<TextSprite> texts = new ArrayList<>();
	private TileMap tileMap = null;
	private Color foreground, background;
	private GameLoop gameLoop = (gesamtZeit, deltaZeit) -> {
	};

	// debug

	private boolean debug = false;
	private TextSprite debugInfo = null;

	final LongProperty elapsedTime = new SimpleLongProperty();
	final AnimationTimer timer = new AnimationTimer() {
		@Override
		public void handle(long nano) {
			if (elapsedTime.get() == 0) {
				elapsedTime.set(nano);
			}
			long delta = nano - elapsedTime.get();
			if (debug) {
				// double fps = 1000d / ((double)delta / 1000000d);
				double fps = 1000000000d / ((double) delta);
				debugInfo.text(String.format("fps: %.1f\n", fps));
			}
			gameLoop.run(nano / 1000000, delta / 1000000);
			elapsedTime.set(nano);
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
	 * @param foreground
	 *            Vordergrundfarbe, wird derzeit nicht verwendet
	 * @param background
	 *            Hintergrundfarbe
	 * @throws IOException
	 *             wird geworfen, wenn die Resource "screen.fxml" nicht geladen
	 *             werden kann
	 */
	public Screen(int screenWidth, int screenHeight, Font font, Color foreground, Color background) throws IOException {

		this.foreground = foreground;
		this.background = background;

		FXMLLoader loader = new FXMLLoader(getClass().getResource("screen.fxml"));
		loader.setController(this);
		pane = loader.load();
		output.setWidth(screenWidth);
		output.setHeight(screenHeight);
		output.setOnKeyTyped(keyEvent -> onOutputKeyTyped(keyEvent));
		output.setFocusTraversable(true);
		output.setStyle(//
				"-fx-background-color: " + getRGB(background) + "; " //
						+ "-fx-color: " + getRGB(foreground) + "; " //
		);
		GraphicsContext g = output.getGraphicsContext2D();
		g.setFont(font);
		g.setTextAlign(TextAlignment.CENTER);
		g.setTextBaseline(VPos.CENTER);
		g.setFill(background);
		g.fillRect(0, 0, output.getWidth(), output.getHeight());

		// output.setClip(new Rectangle(0, 0, screenWidth, screenHeight));
		timer.start();
	}

	/**
	 * Aktiviert / deaktiviert die Debug-Anzeige
	 * 
	 * @param debug
	 *            true: Debug-Informationen werden angezeigt (Standart: FPS)
	 */
	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	/**
	 * @return true, wenn die Debug-Anzeige aktiviert ist
	 */
	public boolean isDebug() {
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
	public List<Sprite> getSprites() {
		return sprites;
	}

	/**
	 * Fügt einen Text-Sprite hinzu, der auf dem Bildschirm angezeigt werden soll
	 * 
	 * @param text
	 *            der Text-Sprite
	 */
	public void addText(TextSprite text) {
		this.texts.add(text);
	}

	/**
	 * entfernt einen Text-Sprite aus der Liste anzuzeigender Sprites
	 * 
	 * @param text
	 *            der Text-Sprite
	 * @return false: Text-Sprite existiert nicht in der Liste
	 */
	public boolean deleteText(TextSprite text) {
		if (!texts.contains(text)) {
			return false;
		}
		this.sprites.remove(text);
		return true;
	}

	/**
	 * @return einen Liste mit allen Text-Sprites
	 */
	public List<TextSprite> getTexts() {
		return texts;
	}

	/**
	 * Legt das anzuzeigende gekachelte Spielfeld fest; ist es null, wird kein
	 * Spielfeld sngezeigt
	 * 
	 * @param tileMap
	 *            das Spielfeld
	 */
	public void setTileMap(TileMap tileMap) {
		this.tileMap = tileMap;
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

	// private methoden

	private void refresh() {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				GraphicsContext g = output.getGraphicsContext2D();
				g.setFill(background);
				g.fillRect(0, 0, output.getWidth(), output.getHeight());
				if (tileMap != null) {
					tileMap.draw(g);
				}
				sprites.forEach(sprite -> {
					g.save();
					sprite.drawSprite(g);
					g.restore();
				});
				texts.forEach(text -> {
					g.save();
					text.draw(g);
					g.restore();
				});
				if (debug) {
					g.save();
					debugInfo.draw(g);
					g.restore();
				}
			}
		});
	}

	private void onOutputKeyTyped(KeyEvent keyEvent) {
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
