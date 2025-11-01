package org.service.utils;

public interface IdGenerator<T> {
    T nextId();
    T currentID();
}
