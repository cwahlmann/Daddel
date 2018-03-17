package de.dreierschach.daddel.gfx;

import java.util.HashMap;
import java.util.Map;

import de.dreierschach.daddel.util.FileUtils;
import javafx.scene.image.Image;

/**
 * Verwaltet alle Bilder; einmal geladene Bilder werden aus Performance-Gründen
 * im Speicher gehalten
 * 
 * @author Christian
 *
 */
public class ImageLib {
	private Map<String, Image> images = new HashMap<>();
	private static ImageLib instance = new ImageLib();

	private static ImageLib instance() {
		if (instance == null) {
			synchronized (ImageLib.class) {
				if (instance == null) {
					instance = new ImageLib();
				}
			}
		}
		return instance;
	}

	/**
	 * /** Lädt ein Bild vom angegebenen Dateipfad und gibt es zurück. Wenn das Bild
	 * schon einmal geladen wurde, wird das im Speicher abgelegte Bild zurückgegeben
	 * 
	 * @param path
	 *            der Dateipfad des Bilds
	 * @param maxSize
	 *            maximale Breite und Höhe des Bilds in Spielraster-Punkten
	 * @return das Image-Objekt
	 */
	public static Image image(String path, int maxSize) {
		ImageLib imageLib = instance();
		String id = path + "//" + maxSize;
		if (!imageLib.images.containsKey(id)) {
			imageLib.images.put(id, new Image(FileUtils.getInputStream(path), maxSize, maxSize, true, true));
		}
		return imageLib.images.get(id);
	}
}
