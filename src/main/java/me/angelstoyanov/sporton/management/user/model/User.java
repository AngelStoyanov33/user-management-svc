package me.angelstoyanov.sporton.management.user.model;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonRootName;
import io.quarkus.mongodb.panache.PanacheMongoEntity;
import io.quarkus.mongodb.panache.common.MongoEntity;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;

import java.util.Objects;

@JsonRootName("user")
@JsonPropertyOrder({"id", "email", "first_name", "last_name", "location"})
@MongoEntity(collection = "User", database = "sporton-dev-db")
public class User extends PanacheMongoEntity {

    @JsonProperty("id")
    public ObjectId id; // used by MongoDB for the _id field

    @JsonProperty(value = "email", required = true)
    private String email;

    @JsonProperty(value = "first_name", required = true)
    @BsonProperty("first_name")
    private String firstName;

    @JsonProperty(value = "last_name", required = true)
    @BsonProperty("last_name")
    private String lastName;

    @JsonProperty("location")
    private String location;

    public User() {
    }

    public User(String email, String firstName, String lastName, String location) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.location = location;
    }

    public User(User other){
        this.email = other.getEmail();
        this.firstName = other.getFirstName();
        this.lastName = other.getLastName();
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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) && Objects.equals(email, user.email) && Objects.equals(firstName, user.firstName) && Objects.equals(lastName, user.lastName) && Objects.equals(location, user.location);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, firstName, lastName, location);
    }

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", location='" + location + '\'' +
                '}';
    }
}
