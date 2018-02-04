package de.dreierschach.daddel.model;

/**
 * Strategie für den Fall, dass ein Partikel das Spielraster verlässt
 * 
 * @author Christian
 *
 */
public enum OutsideGridStrategy {
	/**
	 * lasst ihn sterben
	 */
	kill, 
	/**
	 * zurücktitschen - noch nicht implementiert 
	 */
	bounce, 
	/**
	 * auf der anderen Seite wieder erscheinen
	 */
	reappear, 
	/**
	 * neu starten
	 */
	restart, 
	/**
	 * nichts tun
	 */
	ignore
}