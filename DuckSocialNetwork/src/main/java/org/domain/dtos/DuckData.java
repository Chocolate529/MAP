package org.domain.dtos;

import java.util.List;

public class DuckData extends UserDTO implements DTO{

    private final Double speed;
    private final Double rezistance;

    public DuckData(List<String> attributes) {
        super(List.of(attributes.get(0), attributes.get(1),attributes.get(2)));

        this.speed = Double.parseDouble(attributes.get(3));
        this.rezistance = Double.parseDouble(attributes.get(4));
    }

    public Double getSpeed() {
        return speed;
    }

    public Double getRezistance() {
        return rezistance;
    }
}
