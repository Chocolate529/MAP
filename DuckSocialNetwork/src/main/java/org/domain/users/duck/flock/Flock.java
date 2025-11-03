package org.domain.users.duck.flock;

import org.domain.Entity;
import org.domain.dtos.FlockPerformanceDTO;
import org.domain.exceptions.FlockException;
import org.domain.users.duck.Duck;

import java.util.ArrayList;
import java.util.List;

public class Flock<T extends Duck> extends Entity<Long> {
    private String flockName;
    private List<T> members;

    public Flock(String flockName) {
        this.flockName = flockName;
        this.members = new ArrayList<>();
    }

    public String getFlockName() {
        return flockName;
    }

    public List<T> getMembers() {
        return members;
    }

    public void addMember(T member) {
        boolean exists = members.stream()
                .anyMatch(m -> m.getId().equals(member.getId()));

        if (exists) {
            throw new FlockException("Duck with ID " + member.getId() + " is already in this flock!");
        }

        member.setFlock(this);
        members.add(member);
    }

    public void removeMember(T member) {
        members.remove(member);
    }

    @Override
    public String toString() {
        return super.toString()+"Flock{" +
                "flockName='" + flockName + "NrOfMembers=" + members.size() + '\'' +
                '}';
    }

    public FlockPerformanceDTO getAveragePerformance(){
        if(members.isEmpty()){
            return new FlockPerformanceDTO(this.getId(), this.getFlockName(),0.0,0.0);
        }

        Double speedAvg = members.stream().mapToDouble(Duck::getSpeed).average().orElse(0.0);
        Double rezistanceAvg = members.stream().mapToDouble(Duck::getRezistance).average().orElse(0.0);

        return new FlockPerformanceDTO(this.getId(), this.getFlockName(),speedAvg,rezistanceAvg);
    }
}
