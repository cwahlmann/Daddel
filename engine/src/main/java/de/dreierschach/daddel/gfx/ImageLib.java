package de.dreierschach.daddel.gfx;

import java.util.HashMap;
import java.util.Map;

import de.dreierschach.daddel.Daddel;
import javafx.scene.image.Image;

public class ImageLib {
	private Map<String, Image> images = new HashMap<>();
	private static ImageLib instance = new ImageLib();

	private ImageLib() {}
	
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
	
	public static Image image(String path, int maxSize) {
		ImageLib imageLib = instance();
		String id = path + "//" + maxSize;
		if (!imageLib.images.containsKey(id)) {
			imageLib.images.put(id,new Image(Daddel.class.getResourceAsStream(path), maxSize, maxSize, true, true));
		}
		return imageLib.images.get(id);
	}		
}
