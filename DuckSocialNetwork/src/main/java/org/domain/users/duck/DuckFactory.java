package org.domain.users.duck;

import org.domain.Factory;
import org.domain.dtos.DuckData;
import org.utils.enums.DuckTypes;

public class DuckFactory implements Factory<Duck, DuckTypes, DuckData> {
    @Override
    public Duck create(DuckTypes duckTypes, DuckData duckData) {
        if (duckTypes == null) {
            return null;
        }
        switch (duckTypes) {
            case FLYING -> {
                return new FlyingDuck(duckData);
            }
            case SWIMMING -> {
                return new SwimmingDuck(duckData);
            }
            case FLYING_AND_SWIMMING -> {
                return new FlyingAndSwimmingDuck(duckData);
            }
            default -> {
                return null;
            }
        }
    }

    public DuckFactory() {
    }
}
