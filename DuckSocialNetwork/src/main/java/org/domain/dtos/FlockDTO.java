package org.domain.dtos;

import java.util.List;

public class FlockDTO implements DTO{
    private String flockName;
    List<Integer> memebersIds;

    public FlockDTO(List<String> attr) {
        this.flockName = attr.get(0);
        this.memebersIds = attr.stream().mapToInt(Integer::parseInt).boxed().toList();
    }

    public String getFlockName() {
        return flockName;
    }

    public void setFlockName(String flockName) {
        this.flockName = flockName;
    }

    public List<Integer> getMemebersIds() {
        return memebersIds;
    }

    public void setMemebersIds(List<Integer> memebersIds) {
        this.memebersIds = memebersIds;
    }
}
