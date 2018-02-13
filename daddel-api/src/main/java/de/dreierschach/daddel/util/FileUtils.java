package de.dreierschach.daddel.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import de.dreierschach.daddel.Daddel;

public class FileUtils {
	public enum Filetype {
		CLASSPATH, FILESYSTEM
	}

	public static Filetype determineFiletype(String filename) {
		int index = filename.indexOf(':');
		if (index < 0) {
			return Filetype.CLASSPATH;
		}
		if (index > 0) {
			String prefix = filename.substring(0, index).toLowerCase();
			if (prefix.equals("classpath")) {
				return Filetype.CLASSPATH;
			}
			if (prefix.equals("file:") || (prefix.length() == 1 && Character.isAlphabetic(prefix.charAt(0)))) {
				return Filetype.FILESYSTEM;
			}
		}
		throw new RuntimeException(String.format("Invalid Filename '%s'", filename));
	}

	public static String cutPrefix(String filename) {
		int index = filename.indexOf(':');
		if (index == 0) {
			return filename.substring(1);
		}
		if (index <= 1) {
			return filename;
		}
		if (index == filename.length() - 1) {
			return "";
		}
		return filename.substring(index + 1);
	}

	public static InputStream getInputStream(String filename) {
		Filetype type = determineFiletype(filename);
		String cutFilename = cutPrefix(filename);
		switch (type) {
		case CLASSPATH:
			return new BufferedInputStream(Daddel.class.getResourceAsStream(cutFilename));
		default:
		case FILESYSTEM:
			try {
				return new BufferedInputStream(new FileInputStream(cutFilename));
			} catch (FileNotFoundException e) {
				throw new RuntimeException(e);
			}
		}
	}

	public static URL getInputUrl(String filename) {
		Filetype type = determineFiletype(filename);
		String cutFilename = cutPrefix(filename);
		switch (type) {
		case CLASSPATH:
			return Daddel.class.getResource(cutFilename);
		default:
		case FILESYSTEM:
			try {
				if (cutFilename.contains(":")) {
					return new URL("file:///" + cutFilename);
				} else {
					return new URL("file://" + cutFilename);
				}
			} catch (MalformedURLException e) {
				throw new RuntimeException(e);
			}
		}
	}
}
