package me.angelstoyanov.sporton.management.user.repository;


import io.quarkus.mongodb.panache.PanacheMongoRepository;
import me.angelstoyanov.sporton.management.user.exception.UserAlreadyExistsException;
import me.angelstoyanov.sporton.management.user.exception.UserNotExistException;
import me.angelstoyanov.sporton.management.user.model.User;
import org.bson.types.ObjectId;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import java.util.List;

@Named("UserRepository")
@ApplicationScoped
public class UserRepository implements PanacheMongoRepository<User> {

    public User findByEmail(String email) {
        return find("email", email).firstResult();
    }

    public List<User> findByLocation(String location) {
        return list("location", location);
    }

    public void addUser(User user) throws UserAlreadyExistsException {
        if (findByEmail(user.getEmail()) != null) {
            throw new UserAlreadyExistsException("User with email " + user.getEmail() + " already exists");
        }
        persist(user);
    }

    public void deleteUser(User user) throws UserNotExistException {
        if (findById(user.getId()) == null) {
            throw new UserNotExistException("User with id " + user.getId() + " does not exist");
        }
        delete(user);
    }

    public void deleteUserById(ObjectId id) throws UserNotExistException {
        if (findById(id) == null) {
            throw new UserNotExistException("User with id " + id + " does not exist");
        }
        deleteById(id);
    }

    public User replaceUser(ObjectId id, User user) throws UserNotExistException {
        if (findById(id) == null) {
            throw new UserNotExistException("User with id " + user.getId() + " does not exist");
        }
        User userToUpdate = new User(user);
        userToUpdate.setId(id);
        persistOrUpdate(userToUpdate);
        return userToUpdate;
    }
}