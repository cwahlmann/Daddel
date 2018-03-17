package de.dreierschach.daddel.model;

/**
 * @author Christian
 *
 */
public interface GameLoop {
	/**
	 * Definiert eine Aktion innerhalb der Spielschleife
	 * 
	 * @param gesamtZeit
	 *            die Zeit in ms, die seit Spielstart vergangen ist
	 * @param deltaZeit
	 *            die Zeit in ms, die seit dem letzten Durchlauf vergangen ist
	 */
	void run(long gesamtZeit, long deltaZeit);
}
