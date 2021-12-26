package me.angelstoyanov.sporton.management.user.resource;


import me.angelstoyanov.sporton.management.user.model.User;
import me.angelstoyanov.sporton.management.user.repository.UserRepository;
import org.bson.types.ObjectId;
import org.jboss.resteasy.reactive.ResponseStatus;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.LinkedList;
import java.util.List;

@Path("/")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {

    @Inject
    protected UserRepository userRepository;

    @POST
    @ResponseStatus(201)
    @Path("/user")
    public User createUser(User user) {
        userRepository.addUser(user);
        return userRepository.findByEmail(user.getEmail());
    }

    @PATCH
    @ResponseStatus(200)
    @Path("/user/{id}")
    public User updateUser(@PathParam("id") String id, User user) {
        return userRepository.updateUser(new ObjectId(id), user);
    }

    @GET
    @ResponseStatus(200)
    @Path("/user/{id}")
    public User getUser(@PathParam("id") String id) {
        return userRepository.findById(new ObjectId(id));
    }

    @GET
    @ResponseStatus(200)
    @Path("/user")
    public List<User> getUsers(@QueryParam("location") String location) {
        if (location != null) {
            return userRepository.findByLocation(location);
        }
        return new LinkedList<>();
    }

    @DELETE
    @ResponseStatus(200)
    @Path("/user/{id}")
    public void deleteUser(@PathParam("id") String id) {
        userRepository.deleteById(new ObjectId(id));
    }
}
