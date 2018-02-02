package de.dreierschach.daddel.setup;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javafx.scene.paint.Color;

public class Setup {
	private static Logger log = Logger.getLogger(Setup.class);

	private Properties properties = new Properties();
	
	public void load(String setupFile) {
		this.properties = new Properties();
		try (FileReader reader = new FileReader(setupFile)) {
			this.properties.load(reader);
		} catch (IOException e) {
			log.warn(String.format("Konnte Setup-File '%s' nicht laden.", setupFile), e);
		}		
	}
	
	public void save(String setupFile) {
		try (FileWriter writer = new FileWriter(setupFile)) {
			this.properties.store(writer, setupFile);
		} catch (IOException e) {
			log.warn(String.format("Konnte Setup-File '%s' nicht speichern.") + setupFile, e);
		}				
	}
	
	public String get(String key, String defaultValue) {
		return properties.getProperty(key, defaultValue);
	}
	
	public boolean contains(String key) {
		return properties.containsKey(key);
	}
	
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

	public void set(String key, String value) {
		properties.setProperty(key, value);
	}

	public void set(String key, Object o) {
		Gson gson = new GsonBuilder().create();
		String json = gson.toJson(o);
		String base64 = Base64.getEncoder().encodeToString(json.getBytes());
		properties.setProperty(key, base64);
	}
	
	public static void main(String[] args) {
		String setupFile = "test.ini";
		Setup setup = new Setup();
		setup.set("eins", "1");
		List<Color> list = Arrays.asList(
				new Color[] {
				Color.ALICEBLUE,
				Color.AQUA,
		}); 
		setup.set("list", list);
		setup.save(setupFile);
		System.out.println("--------------- saved --");

		setup = new Setup();
		setup.load(setupFile);
		List<Color> readList = Arrays.asList(setup.get("list", Color[].class));
		for (int i=0; i<readList.size(); i++) {
			Color color = readList.get(i);
			System.out.println(color.hashCode());
		}
	}
}
