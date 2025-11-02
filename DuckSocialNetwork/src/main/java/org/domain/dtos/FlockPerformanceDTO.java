package org.domain.dtos;


import java.util.List;

public class FlockPerformanceDTO implements DTO {

    public List<String> getData() {
        return List.of(flockId.toString(),flockName, speedAvg.toString(), rezistanceAvg.toString());
    }

    private Long flockId;
    private String flockName;
    private Double speedAvg;
    private Double rezistanceAvg;

    public FlockPerformanceDTO(Long flockId, String flockName, Double speedAvg, Double rezistanceAvg) {
        this.flockId = flockId;
        this.flockName = flockName;
        this.speedAvg = speedAvg;
        this.rezistanceAvg = rezistanceAvg;
    }

    public String getFlockName() {
        return flockName;
    }

    public void setFlockName(String flockName) {
        this.flockName = flockName;
    }

    public Long getFlockId() {
        return flockId;
    }

    public void setFlockId(Long flockId) {
        this.flockId = flockId;
    }



    @Override
    public String toString() {
        return super.toString() + "FlockPerformanceDTO{" +
                "speedAvg=" + speedAvg +
                ", rezistanceAvg=" + rezistanceAvg +
                '}';
    }

    public Double getSpeedAvg() {
        return speedAvg;
    }

    public void setSpeedAvg(Double speedAvg) {
        this.speedAvg = speedAvg;
    }

    public Double getRezistanceAvg() {
        return rezistanceAvg;
    }

    public void setRezistanceAvg(Double rezistanceAvg) {
        this.rezistanceAvg = rezistanceAvg;
    }


}
