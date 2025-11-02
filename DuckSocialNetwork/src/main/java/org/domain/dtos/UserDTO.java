package org.domain.dtos;

import java.util.List;

public abstract class UserDTO implements DTO{
    public UserDTO(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public UserDTO(List<String> attributes) {
        this.username = attributes.get(0);
        this.password = attributes.get(1);
        this.email = attributes.get(2);
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    private final String username;
    private final String password;
    private final String email;


}
