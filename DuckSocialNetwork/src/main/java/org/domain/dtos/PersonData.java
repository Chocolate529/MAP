package org.domain.dtos;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.utils.Constants.DATE_FORMAT;

public class PersonData extends UserDTO implements DTO{

    private final String firstName;
    private final String lastName;
    private final String occupation;
    private final LocalDate dateOfBirth;
    private final Double empathyLevel;

    public PersonData(List<String> attributes) {
        super(List.of(attributes.get(0), attributes.get(1),attributes.get(2)));
        this.firstName = attributes.get(3);
        this.lastName = attributes.get(4);
        this.occupation = attributes.get(5);
        this.dateOfBirth = LocalDate.parse(attributes.get(6), DATE_FORMAT);
        this.empathyLevel = Double.parseDouble(attributes.get(7));
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getOccupation() {
        return occupation;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public Double getEmpathyLevel() {
        return empathyLevel;
    }

}
