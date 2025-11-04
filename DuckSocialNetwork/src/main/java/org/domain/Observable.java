package org.domain;

public interface Observable<T> {
    void addObserver(T o);
    void removeObserver(T o);
    void notifyObservers();
}
