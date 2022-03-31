package me.angelstoyanov.sporton.management.user.resource;


import io.quarkus.runtime.annotations.RegisterForReflection;
import me.angelstoyanov.sporton.management.user.exception.InvalidUserIdException;
import me.angelstoyanov.sporton.management.user.exception.UserAlreadyExistsException;
import me.angelstoyanov.sporton.management.user.exception.UserNotExistException;
import me.angelstoyanov.sporton.management.user.model.User;
import me.angelstoyanov.sporton.management.user.repository.UserRepository;
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
@RegisterForReflection
public class UserResource {

    @Inject
    protected UserRepository userRepository;

    @POST
    @ResponseStatus(201)
    @Path("/user")
    public RestResponse<User> createUser(User user,
                                         @HeaderParam("X-Requesting-User-Role") String userRole,
                                         @HeaderParam("X-Requesting-User-Id") String userId) {
        try {
            if (!user.getUserId().equals(userId) && !userRole.equals("ADMIN")) {
                return ResponseBuilder.ok((User) null).status(RestResponse.Status.FORBIDDEN).build();
            }
            userRepository.addUser(user);
            return ResponseBuilder.ok(userRepository.findByEmail(user.getEmail())).build();
        } catch (UserAlreadyExistsException e) {
            return ResponseBuilder.ok((User) null).status(RestResponse.Status.CONFLICT).build();
        }
    }

    @PUT
    @ResponseStatus(200)
    @Path("/user/{id}")
    public RestResponse<User> updateUser(@PathParam("id") String id,
                                         User user,
                                         @HeaderParam("X-Requesting-User-Role") String userRole,
                                         @HeaderParam("X-Requesting-User-Id") String userId) {
        try {
            User existingUser = userRepository.findByUserId(id);
            if (!existingUser.getUserId().equals(userId) && !userRole.equals("ADMIN")) {
                return ResponseBuilder.ok((User) null).status(RestResponse.Status.FORBIDDEN).build();
            }
            return ResponseBuilder.ok(userRepository.replaceUser(id, user)).build();
        } catch (UserNotExistException e) {
            return ResponseBuilder.ok((User) null).status(RestResponse.Status.NOT_FOUND).build();
        } catch (InvalidUserIdException e) {
            return ResponseBuilder.ok((User) null).status(RestResponse.Status.BAD_REQUEST).build();
        }
    }

    @GET
    @ResponseStatus(200)
    @Path("/user/{id}")
    public RestResponse<User> getUser(@PathParam("id") String id,
                                      @HeaderParam("X-Requesting-User-Role") String userRole,
                                      @HeaderParam("X-Requesting-User-Id") String userId) {
        try {
            User user = userRepository.findByUserId(id);
            if (user == null) {
                return ResponseBuilder.ok((User) null).status(RestResponse.Status.NOT_FOUND).build();
            }
            if (!user.getUserId().equals(userId) && !userRole.equals("ADMIN")) {
                return ResponseBuilder.ok((User) null).status(RestResponse.Status.FORBIDDEN).build();
            }
            return ResponseBuilder.ok(user).build();
        } catch (InvalidUserIdException e) {
            return ResponseBuilder.ok((User) null).status(RestResponse.Status.BAD_REQUEST).build();
        }
    }

    @DELETE
    @ResponseStatus(200)
    @Path("/user/{id}")
    public RestResponse<User> deleteUser(@PathParam("id") String id,
                                         @HeaderParam("X-Requesting-User-Role") String userRole,
                                         @HeaderParam("X-Requesting-User-Id") String userId) {
        try {
            User user = userRepository.findByUserId(id);
            if (!user.getUserId().equals(userId) && !userRole.equals("ADMIN")) {
                return ResponseBuilder.ok((User) null).status(RestResponse.Status.FORBIDDEN).build();
            }
            userRepository.deleteUserById(id);
            return ResponseBuilder.ok((User) null).build();
        } catch (UserNotExistException e) {
            return ResponseBuilder.ok((User) null).status(RestResponse.Status.NOT_FOUND).build();
        } catch (InvalidUserIdException e) {
            return ResponseBuilder.ok((User) null).status(RestResponse.Status.BAD_REQUEST).build();
        }
    }

    @GET
    @ResponseStatus(200)
    @Path("/user/all")
    public List<User> getUsers(List<String> userIds) {
        List<User> users = new LinkedList<>();
        if (userIds != null && !userIds.isEmpty()) {
            userIds.forEach(id -> {
                try {
                    User user = userRepository.findByUserId(id);
                    if (user != null) {
                        users.add(user);
                    }
                } catch (InvalidUserIdException ignored) {
                }
            });
        }
        return users;
    }
}
