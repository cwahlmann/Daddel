package de.dreierschach.daddel.model;

public interface Validator<T> {
	/**
	 * Funktion, um den Inhalt einer Variablen zu prüfen
	 * @param t
	 * die zu prüfende Variable
	 * @return das Ergebnis der Prüfung
	 */
	boolean validate(T t);
}
