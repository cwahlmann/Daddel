package de.dreierschach.daddel.model;

/**
 * Strategien, was mit einem Partikel geschehen soll, wenn die Lebensspanne
 * abgelaufen ist
 * 
 * @author Christian
 *
 */
public enum ParticleStrategy {
	/**
	 * lasst ihn sterben
	 */
	kill, 
	/**
	 * neu starten
	 */
	restart,
	/**
	 * auf der anderen Seite wieder erscheinen
	 */	
	reappear,
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