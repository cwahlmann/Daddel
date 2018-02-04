package de.dreierschach.daddel.audio;

import java.util.HashMap;
import java.util.Map;

import javafx.scene.media.AudioClip;

/**
 * Verwaltet alle Audioclips; einmal geladene Clips werden aus
 * Performance-Gründen im Speicher gehalten
 * 
 * @author Christian
 *
 */
public class AudioLib {
	private Map<String, AudioClip> audioclips = new HashMap<>();
	private static AudioLib instance = new AudioLib();

	private static AudioLib instance() {
		if (instance == null) {
			synchronized (AudioLib.class) {
				if (instance == null) {
					instance = new AudioLib();
				}
			}
		}
		return instance;
	}

	/**
	 * Lädt einen Audioclip vom angegebenen Dateipfad und gibt ihn zurück. Wenn der
	 * Clip schon einmal geladen wurde, wird der im Speicher abgelegte Clip
	 * zurückgegeben
	 * 
	 * @param path
	 *            der Dateipfad des Audioclips
	 * @return das Audioclip-Objekt
	 */
	public static AudioClip audioclip(String path) {
		AudioLib audioLib = instance();

		if (!audioLib.audioclips.containsKey(path)) {
			AudioClip audioClip = new AudioClip(path);
			audioLib.audioclips.put(path, audioClip);
		}
		return audioLib.audioclips.get(path);
	}
}
