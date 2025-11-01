package org.domain;

import org.utils.enums.DuckTypes;

public class    Duck extends User {
    private DuckTypes duckType;
    private Double speed;
    private Double rezistance;


    public DuckTypes getDuckType() {
        return duckType;
    }

    public void setDuckType(DuckTypes duckType) {
        this.duckType = duckType;
    }

    public Double getSpeed() {
        return speed;
    }

    public void setSpeed(Double speed) {
        this.speed = speed;
    }

    public Double getRezistance() {
        return rezistance;
    }

    public void setRezistance(Double rezistance) {
        this.rezistance = rezistance;
    }




    public Duck( String username, String password, String email, DuckTypes duckType, Double speed, Double rezistance) {
        super( username, password, email);
        this.duckType = duckType;
        this.speed = speed;
        this.rezistance = rezistance;
    }

    @Override
    public void login() {

    }

    @Override
    public void logout() {

    }

    @Override
    public void sendMessage() {

    }

    @Override
    public void receiveMessage() {

    }

    @Override
    public String toString() {

        return "Duck{" +
                ", rezistance=" + rezistance +
                ", speed=" + speed +
                ", duckType=" + duckType +
                '}';
    }
}
