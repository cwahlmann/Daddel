package de.dreierschach.daddel.validator;

/**
 * Interface für eine Lambda-Funktion, die Werte prüft,
 * z.B. wert == 2
 * 
 * @author Christian
 *
 * @param <T>
 *            Typ der zu prüfenden Werte
 */
public interface Validator<T> {
	/**
	 * Funktion, um den Inhalt einer Variablen zu prüfen
	 * 
	 * @param t
	 *            die zu prüfende Variable
	 * @return das Ergebnis der Prüfung
	 */
	boolean validate(T t);
}
