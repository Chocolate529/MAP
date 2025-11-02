package org.domain.users.relationships;

import org.domain.Entity;
import org.domain.users.User;

public class Friendship extends Entity<Long> {
    private User user1;
    private User user2;

    public Friendship( User user1, User user2) {
        this.user1 = user1;
        this.user2 = user2;
        user1.addFriend(user2);
    }

    public User getUser1() {
        return user1;
    }

    public void setUser1(User user1) {
        this.user1 = user1;
    }

    public User getUser2() {
        return user2;
    }

    public void setUser2(User user2) {
        this.user2 = user2;
    }

    @Override
    public String toString() {
        return super.toString()+"Friendship{" +
                "user1=" + user1 +
                ", user2=" + user2 +
                '}';
    }
}
