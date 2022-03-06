package me.angelstoyanov.sporton.management.user.exception;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class UserAlreadyExistsException extends RuntimeException{
    public UserAlreadyExistsException(String message) {
        super(message);
    }
    public UserAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}
