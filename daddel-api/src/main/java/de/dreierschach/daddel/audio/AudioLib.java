package de.dreierschach.daddel.audio;

import java.util.HashMap;
import java.util.Map;

import javafx.scene.image.Image;
import javafx.scene.media.AudioClip;

public class AudioLib {
	private Map<String, AudioClip> audioclips = new HashMap<>();
	private static AudioLib instance = new AudioLib();

	private AudioLib() {}
	
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
	
	public static AudioClip audioclip(String path) {
		AudioLib audioLib = instance();
		
		if (!audioLib.audioclips.containsKey(path)) {
			AudioClip audioClip = new AudioClip(path);
			audioLib.audioclips.put(path, audioClip);
		}
		return audioLib.audioclips.get(path);
	}		
}
