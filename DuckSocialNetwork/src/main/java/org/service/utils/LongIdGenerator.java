package org.service.utils;

import java.util.concurrent.atomic.AtomicLong;

public class LongIdGenerator implements IdGenerator<Long> {

    private final AtomicLong counter = new AtomicLong(1);

    @Override
    public Long nextId() {
        return counter.getAndIncrement();
    }

    @Override
    public Long currentID() {
        return counter.get();
    }
}
