package de.dreierschach.engine;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.dreierschach.engine.gfx.sprite.Sprite;
import de.dreierschach.engine.gfx.text.TextSprite;
import de.dreierschach.engine.gfx.tilemap.TileMap;
import de.dreierschach.engine.listener.InputListener;
import de.dreierschach.engine.listener.KeyListener;
import de.dreierschach.engine.model.Scr;
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
//				double fps = 1000d / ((double)delta / 1000000d);
				double fps = 1000000000d / ((double)delta );
				debugInfo.text(String.format("fps: %.1f\n", fps));
			}
			gameLoop.run(nano / 1000000, delta / 1000000);
			elapsedTime.set(nano);
			refresh();
		}
	};

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

	public void setDebug(boolean debug) {
		this.debug = debug;
	}
	
	public boolean isDebug() {
		return debug;
	}
	
	public void setDebugInfo(TextSprite debugInfo) {
		this.debugInfo = debugInfo;
	}
	
	public TextSprite getDebugInfo() {
		return debugInfo;
	}
	
	public String getRGB(Color color) {
		return String.format("#%02X%02X%02X", ((int) color.getRed()) * 255, ((int) color.getGreen()) * 255,
				((int) color.getBlue()) * 255);
	}

	public void setClipping(Scr scr0, Scr scr1) {
		output.setClip(new Rectangle(scr0.x(), scr0.y(), //
				scr1.x() - scr0.x(), scr1.y() - scr0.y()));
	}

	public void setGameLoop(GameLoop gameLoop) {
		this.gameLoop = gameLoop;
	}

	public void addKeyListener(KeyCode keyCode, KeyListener keyListener) {
		this.keyListener.put(keyCode, keyListener);
	}

	public void removeKeyListener(KeyCode keyCode) {
		this.keyListener.remove(keyCode);
	}

	public void removeKeyListeners() {
		this.keyListener.clear();
	}

	public void onOutputKeyTyped(KeyEvent keyEvent) {
		manageInput(keyEvent);
		if (this.keyListener.containsKey(keyEvent.getCode())) {
			this.keyListener.get(keyEvent.getCode()).onKey();
		}
	}

	public void setInputListener(InputListener inputListener) {
		this.inputListener = inputListener;
	}

	public void clearInput() {
		inputString = "";
	}

	public void setEnableInput(boolean enableInput) {
		this.enableInput = enableInput;
	}

	public void setInputLaenge(int inputLaenge) {
		this.inputLaenge = inputLaenge;
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

	public void addSprite(Sprite sprite) {
		this.sprites.add(sprite);
	}

	public boolean deleteSprite(Sprite sprite) {
		if (!sprites.contains(sprite)) {
			return false;
		}
		this.sprites.remove(sprite);
		return true;
	}

	public List<Sprite> getSprites() {
		return sprites;
	}

	public void addText(TextSprite text) {
		this.texts.add(text);
	}

	public boolean deleteText(TextSprite text) {
		if (!texts.contains(text)) {
			return false;
		}
		this.sprites.remove(text);
		return true;
	}

	public List<TextSprite> getTexts() {
		return texts;
	}

	public void setTileMap(TileMap tileMap) {
		this.tileMap = tileMap;
	}

	public TileMap getTileMap() {
		return tileMap;
	}

	public void refresh() {
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

	public Pane getPane() {
		return pane;
	}

}
