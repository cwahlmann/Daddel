package de.dreierschach.daddel.validator;

public interface Operator<T> {
	boolean operate(T a, T b);
}
