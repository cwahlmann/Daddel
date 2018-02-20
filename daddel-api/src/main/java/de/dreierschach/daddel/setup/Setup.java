package de.dreierschach.daddel.setup;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javafx.scene.paint.Color;

/**
 * Lädt und speichert Setup-Werte
 * 
 * @author Christian
 *
 */
public class Setup {
	private static Logger log = Logger.getLogger(Setup.class);

	private Properties properties = new Properties();
	private boolean changed = false;

	public boolean hasChanged() {
		return changed;
	}
	
	public void setChanged() {
		this.changed = true;
	}
	
	public void clearChanged() {
		this.changed = false;
	}

	/**
	 * Lade eine Setup-Datei
	 * 
	 * @param setupFile
	 *            Dateipfad
	 */
	public void load(String setupFile) {
		this.properties = new Properties();
		try (FileReader reader = new FileReader(setupFile)) {
			this.properties.load(reader);
		} catch (IOException e) {
			log.warn(String.format("Konnte Setup-File '%s' nicht laden.", setupFile), e);
		}
	}

	/**
	 * Speichere das Setup in eine Setup-Datei
	 * 
	 * @param setupFile
	 *            Dateipfad der Datei
	 */
	public void save(String setupFile) {
		
		if (!hasChanged()) {
			return;
		}
		clearChanged();
		Path path = Paths.get(setupFile).getParent();
		if	(!path.toFile().exists()) {
			path.toFile().mkdirs();
		}
		
		try (FileWriter writer = new FileWriter(setupFile)) {
			this.properties.store(writer, setupFile);
		} catch (IOException e) {
			log.warn(String.format("Konnte Setup-File '%s' nicht speichern.") + setupFile, e);
		}
	}

	/**
	 * Hole einen Setup-Wert
	 * 
	 * @param key
	 *            Key des Werts
	 * @param defaultValue
	 *            Default-Inhalt des Setup-Werts
	 * @return dee Setup-Wert
	 */
	public String get(String key, String defaultValue) {
		return properties.getProperty(key, defaultValue);
	}

	/**
	 * Prüft, ob ein Setup-Wert existiert
	 * 
	 * @param key
	 *            Key des Werts
	 * @return true, wenn der Wert existiert
	 */
	public boolean contains(String key) {
		return properties.containsKey(key);
	}

	/**
	 * Hole ein Setup-Objekt
	 * 
	 * @param key
	 *            der Key des Setup-Objekts
	 * @param clazz
	 *            die Klasse des Setup-Objekts
	 * @param <T>
	 *            Typ des Setup-Objekts
	 * @return der Wert des Setup-Objekts
	 */
	public <T> T get(String key, Class<T> clazz) {
		if (!contains(key)) {
			return null;
		}
		String base64 = properties.getProperty(key);
		String json = new String(Base64.getDecoder().decode(base64));
		Gson gson = new GsonBuilder().create();
		T t = gson.fromJson(json, clazz);
		return t;
	}

	/**
	 * Setzt einen Setup-Wert
	 * 
	 * @param key
	 *            der Schlüssel des Setup-Werts
	 * @param value
	 *            der Setup-Wert
	 */
	public void set(String key, String value) {
		properties.setProperty(key, value);
		setChanged();
	}

	/**
	 * Setzt einen Setup-Wert, wenn er nicht schon existiert
	 * 
	 * @param key
	 *            der Schlüssel des Setup-Objekts
	 * @param value
	 *            der Setup-Wert
	 */
	public void setIfNew(String key, String value) {
		if (!properties.containsKey(key)) {
			set(key, value);
		}
	}

	/**
	 * Setzt ein Setup-Objekt
	 * 
	 * @param key
	 *            der Schlüssel des Setup-Objekts
	 * @param o
	 *            der Setup-Objekt
	 */
	public void set(String key, Object o) {
		Gson gson = new GsonBuilder().create();
		String json = gson.toJson(o);
		String base64 = Base64.getEncoder().encodeToString(json.getBytes());
		properties.setProperty(key, base64);
		setChanged();
	}

	/**
	 * Setzt ein Setup-Objekt, wenn es nicht schon existiert
	 * 
	 * @param key
	 *            der Schlüssel des Setup-Objekts
	 * @param o
	 *            der Setup-Objekt
	 */
	public void setIfNew(String key, Object o) {
		if (!properties.containsKey(key)) {
			set(key, o);
		}
	}
}
