package org.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class User extends Entity<Long>{
    private String username;
    private String password;
    private String email;
    private List<User> friends;


    public User( String username, String password, String email) {

        this.username = username;
        this.password = password;
        this.email = email;
        this.friends = new ArrayList<>();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<User> getFriends() {
        return friends;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", id=" + id +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof User user)) return false;
        if (!super.equals(o)) return false;
        return Objects.equals(username, user.username) && Objects.equals(email, user.email) && Objects.equals(friends, user.friends);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), username, email, friends);
    }

    public abstract void login();
    public abstract void logout();
    public abstract void sendMessage();
    public abstract void receiveMessage();

    public void addFriend(User user) {
        if (user == null) return;
        if (friends == null) friends = new ArrayList<>();
        if (!friends.contains(user)) {
            friends.add(user);
            user.addFriend(this);
        }
    }

    public void removeFriend(User user) {
        if (user == null || friends == null) return;
        if (friends.contains(user)) {
            friends.remove(user);
            user.removeFriend(this);
        }
    }


}
