package de.dreierschach.daddel.validator;

/**
 * Ein Ausdruck, mit dem mehrere Validator-Funktionen zu einem einzigen
 * Validator verknüpft werden können
 * 
 * @author Christian
 *
 * @param <T>
 *            Typ der zu prüfenden Werte
 */
public class Expression<T> {

	/**
	 * der aktuelle Validator
	 */
	public Validator<T> result = t -> true;

	private Expression() {
	}

	/**
	 * Erzeugt eine neue Instanz des Ausdrucks
	 * 
	 * @param clazz
	 *            gibt den Typ der zu prüfenden Werte an
	 * @param <T>
	 *            Typ der zu prüfenden Werte
	 * @return die neue Instanz
	 */
	public static <T> Expression<T> instance(Class<T> clazz) {
		return new Expression<>();
	}

	/**
	 * legt die initiale Validator-Funktion fest, z.B. x == 10
	 * 
	 * @param validator
	 *            die initiale Validator-Funktion
	 * @return this
	 */
	public Expression<T> with(Validator<T> validator) {
		result = validator;
		return this;
	}

	/**
	 * legt die initiale Validator-Funktion als Negation fest, z.B. x == 10 ergibt
	 * dann x != 10
	 * 
	 * @param validator
	 *            die initiale Validator-Funktion
	 * @return this
	 */
	public Expression<T> not(Validator<T> validator) {
		result = new Operation<T>(result, validator, (a, b) -> !b);
		return this;
	}

	/**
	 * verknüpft die aktuelle Validator-Funktion mit der angegebenen als
	 * UND-Verknüpfung
	 * 
	 * @param validator
	 *            die Validator-Funktion
	 * @return this
	 */
	public Expression<T> and(Validator<T> validator) {
		result = new Operation<T>(result, validator, (a, b) -> a && b);
		return this;
	}

	/**
	 * verknüpft die aktuelle Validator-Funktion mit der angegebenen als
	 * ODER-Verknüpfung
	 * 
	 * @param validator
	 *            die Validator-Funktion
	 * @return this
	 */
	public Expression<T> or(Validator<T> validator) {
		result = new Operation<T>(result, validator, (a, b) -> a || b);
		return this;
	}

	/**
	 * verknüpft die aktuelle Validator-Funktion mit der angegebenen als
	 * UND-NICHT-Verknüpfung
	 * 
	 * @param validator
	 *            die Validator-Funktion
	 * @return this
	 */
	public Expression<T> andNot(Validator<T> validator) {
		result = new Operation<T>(result, validator, (a, b) -> a && !b);
		return this;
	}

	/**
	 * verknüpft die aktuelle Validator-Funktion mit der angegebenen als
	 * ODER-NICHT-Verknüpfung
	 * 
	 * @param validator
	 *            die Validator-Funktion
	 * @return this
	 */
	public Expression<T> orNot(Validator<T> validator) {
		result = new Operation<T>(result, validator, (a, b) -> a || !b);
		return this;
	}

	/**
	 * @return die endgültige zusammengesetzte Validatorfunktion
	 */
	public Validator<T> create() {
		return result;
	}
}
