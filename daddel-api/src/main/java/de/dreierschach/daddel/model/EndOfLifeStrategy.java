package de.dreierschach.daddel.model;

/**
 * Strategien, was mit einem Partikel geschehen soll, wenn die Lebensspanne
 * abgelaufen ist
 * 
 * @author Christian
 *
 */
public enum EndOfLifeStrategy {
	/**
	 * lasst ihn sterben
	 */
	die, 
	/**
	 * neu starten
	 */
	restart, 
	/**
	 * einfrieren
	 */
	stop, 
	/**
	 * zurücktitschen - noch nicht implementiert
	 */
	bounce, 
	/**
	 * einfach weiter
	 */
	ignore
}