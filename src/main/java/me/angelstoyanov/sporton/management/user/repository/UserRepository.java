package me.angelstoyanov.sporton.management.user.repository;


import io.quarkus.mongodb.panache.PanacheMongoRepository;
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

    public void addUser(User user) {
        persist(user);
    }

    public void deleteUser(User user) {
        delete(user);
    }

    public User updateUser(ObjectId id, User user) {
        User userToUpdate = findById(id);
        // Just testing update
        //FIXME: NOT NULL SAFETY, DEPENDENT, ABSOLUTE DATA REPLACE
        userToUpdate.setEmail(user.getEmail());
        userToUpdate.setFirstName(user.getFirstName());
        userToUpdate.setLastName(user.getLastName());
        userToUpdate.setLocation(user.getLocation());
        userToUpdate.setId(userToUpdate.getId());
        persistOrUpdate(userToUpdate);
        return userToUpdate;
    }
}