package de.dreierschach.engine.listener;

import java.util.Observer;

public interface IObservable {
	void addObserver(Observer observer);
	void deleteObservers();
	boolean hasChanged();
}
