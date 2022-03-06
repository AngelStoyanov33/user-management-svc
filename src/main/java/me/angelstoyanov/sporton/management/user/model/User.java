package me.angelstoyanov.sporton.management.user.model;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonRootName;
import io.quarkus.mongodb.panache.PanacheMongoEntity;
import io.quarkus.mongodb.panache.common.MongoEntity;
import io.quarkus.runtime.annotations.RegisterForReflection;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;

@JsonRootName("user")
@JsonPropertyOrder({"id", "user_id", "email", "username", "location"})
@MongoEntity(collection = "User", database = "sporton-dev-db")
@RegisterForReflection
public class User extends PanacheMongoEntity {

    @JsonProperty("id")
    private ObjectId id;

    @JsonProperty(value = "user_id", required = true)
    @BsonProperty("user_id")
    private String userId;

    @JsonProperty(value = "email", required = true)
    private String email;

    @JsonProperty(value = "username", required = true)
    private String username;

    @JsonProperty("location")
    private String location;

    @JsonProperty("role")
    private Role role;


    public enum Role {
        ADMIN, DEVELOPER, MODERATOR, USER
    }

    public User() {
    }

    public User(String userId, String email, String firstName, String location) {
        this.userId = userId;
        this.email = email;
        this.username = firstName;
        this.location = location;
    }

    public User(User other) {
        this.email = other.getEmail();
        this.id = other.getId();
        this.username = other.getUsername();
        this.location = other.getLocation();
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
