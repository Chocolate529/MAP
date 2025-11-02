package org.domain.validators;

import org.domain.users.relationships.Friendship;
import org.domain.exceptions.ValidationException;

public class FriendshipValidator implements Validator<Friendship> {
    @Override
    public void validate(Friendship f) throws ValidationException {
        if (f.getUser1() == null) {
            throw new ValidationException("User1 cannot be null");
        }
        if (f.getUser2() == null) {
            throw new ValidationException("User2 cannot be null");
        }
    }
}
