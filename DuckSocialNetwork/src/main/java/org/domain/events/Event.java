package org.domain.events;

import org.domain.Entity;
import org.domain.Observable;
import org.domain.Observer;
import org.domain.users.User;
import org.domain.users.duck.Duck;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public abstract class Event<T extends Observer> extends Entity<Long> implements Observable<T> {
    List<T> subscribers;

    public Event(List<T> subscribers) {
        this.subscribers = (subscribers != null) ? subscribers : new ArrayList<>();
    }

    @Override
    public void addObserver(T o) {
        subscribers.add((T) o);
    }

    @Override
    public void removeObserver(T o) {
        subscribers.remove(o);
    }

    @Override
    public String toString() {
        return "Event{" +
                ", id=" + id +
                '}';
    }

    @Override
    public void notifyObservers() {
        for(T o : subscribers){
            o.update();
        }
    }

    public List<T> getSubscribers() {
        return subscribers;
    }

}
