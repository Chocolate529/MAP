package org.service.utils;

import java.util.concurrent.atomic.AtomicLong;

public class LongIdGenerator implements IdGenerator<Long> {

    public LongIdGenerator(Long start) {
        this.counter = new AtomicLong(start);
    }

    private final AtomicLong counter;

    @Override
    public Long nextId() {
        return counter.getAndIncrement();
    }

    @Override
    public Long currentID() {
        return counter.get();
    }
}
