package me.angelstoyanov.sporton.management.user.repository;


import io.quarkus.mongodb.panache.PanacheMongoRepository;
import me.angelstoyanov.sporton.management.user.exception.InvalidUserIdException;
import me.angelstoyanov.sporton.management.user.exception.UserAlreadyExistsException;
import me.angelstoyanov.sporton.management.user.exception.UserNotExistException;
import me.angelstoyanov.sporton.management.user.model.User;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Named("UserRepository")
@ApplicationScoped
public class UserRepository implements PanacheMongoRepository<User> {

    public User findByEmail(String email) {
        return find("email", email).firstResult();
    }

    public User findByUserId(String id) {
        try {
            UUID.fromString(id);
        } catch (IllegalArgumentException exception) {
            throw new InvalidUserIdException("Invalid user id " + id);
        }
        User user = find(String.format(Locale.US, "{\"user_id\":\"%s\"}", id)).firstResult();
        if (user == null) {
            throw new UserNotExistException("User with id " + id + " does not exist");
        }
        return user;
    }

    public void deleteByUserId(String id) {
        User user = findByUserId(id);
        delete(user);
    }

    public List<User> findByLocation(String location) {
        return list("location", location);
    }

    public void addUser(User user) throws UserAlreadyExistsException {
        if (findByEmail(user.getEmail()) != null || findByUserId(user.getUserId()) != null) {
            throw new UserAlreadyExistsException("User with email " + user.getEmail() + " already exists");
        }
        user.setRole(User.Role.USER);
        persist(user);
    }

    public void deleteUser(User user) throws UserNotExistException {
        if (findByUserId(user.getUserId()) == null) {
            throw new UserNotExistException("User with id " + user.getId() + " does not exist");
        }
        delete(user);
    }

    public void deleteUserById(String id) throws UserNotExistException {
        if (findByUserId(id) == null) {
            throw new UserNotExistException("User with id " + id + " does not exist");
        }
        deleteByUserId(id);
    }

    public User replaceUser(String id, User user) throws UserNotExistException {
        if (findByUserId(id) == null) {
            throw new UserNotExistException("User with id " + user.getId() + " does not exist");
        }
        User userToUpdate = new User(user);
        userToUpdate.setId(user.getId());
        persistOrUpdate(userToUpdate);
        return userToUpdate;
    }
}