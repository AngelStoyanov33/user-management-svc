package me.angelstoyanov.sporton.management.user.resource;


import me.angelstoyanov.sporton.management.user.exception.UserAlreadyExistsException;
import me.angelstoyanov.sporton.management.user.exception.UserNotExistException;
import me.angelstoyanov.sporton.management.user.model.User;
import me.angelstoyanov.sporton.management.user.repository.UserRepository;
import org.bson.types.ObjectId;
import org.jboss.resteasy.reactive.ResponseStatus;
import org.jboss.resteasy.reactive.RestResponse;
import org.jboss.resteasy.reactive.RestResponse.ResponseBuilder;

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
    public RestResponse<User> createUser(User user) {
        try {
            userRepository.addUser(user);
            return ResponseBuilder.ok(userRepository.findByEmail(user.getEmail())).build();
        } catch (UserAlreadyExistsException e) {
            return ResponseBuilder.ok((User) null).status(RestResponse.Status.CONFLICT).build();
        }
    }

    @PUT
    @ResponseStatus(200)
    @Path("/user/{id}")
    public RestResponse<User> updateUser(@PathParam("id") String id, User user) {
        try {
            return ResponseBuilder.ok(userRepository.replaceUser(new ObjectId(id), user)).build();
        } catch (UserAlreadyExistsException e) {
            return ResponseBuilder.ok((User) null).status(RestResponse.Status.NOT_FOUND).build();
        }
    }

    @GET
    @ResponseStatus(200)
    @Path("/user/{id}")
    public RestResponse<User> getUser(@PathParam("id") String id) {
        User user = userRepository.findById(new ObjectId(id));
        if (user == null) {
            return ResponseBuilder.ok((User) null).status(RestResponse.Status.NOT_FOUND).build();
        }
        return ResponseBuilder.ok(user).build();
    }

    @GET
    @ResponseStatus(200)
    @Path("/user")
    public List<User> getUsersByLocation(@QueryParam("location") String location) {
        if (location != null) {
            return userRepository.findByLocation(location);
        }
        return new LinkedList<>();
    }

    @DELETE
    @ResponseStatus(200)
    @Path("/user/{id}")
    public RestResponse<User> deleteUser(@PathParam("id") String id) {
        try {
            userRepository.deleteUserById(new ObjectId(id));
            return ResponseBuilder.ok((User) null).build();
        } catch (UserNotExistException e) {
            return ResponseBuilder.ok((User) null).status(RestResponse.Status.NOT_FOUND).build();
        }
    }

    @GET
    @ResponseStatus(200)
    @Path("/users")
    public List<User> getUsers(List<String> userIds) {
        List<User> users = new LinkedList<>();
        if (!userIds.isEmpty()) {
            userIds.forEach(id -> {
                User user = userRepository.findById(new ObjectId(id));
                if(user != null) {
                    users.add(user);
                }
            });
        }
        return users;
    }
}
