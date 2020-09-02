package ir.afarinesh.realhope.modules.user_management.features.authentication.application.ports.in.exceptions;

public class AuthenticationUseCaseException extends Exception {
    public AuthenticationUseCaseException(String message) {
        super(message);
    }
}
