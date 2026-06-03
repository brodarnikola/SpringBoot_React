package com.example.polls.exception;

import com.example.polls.payload.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Global exception handler so the API always responds with a JSON body.
 * Without this, security exceptions bubble up to the entry point and return an
 * empty body, which makes the client crash with "Unexpected end of JSON input".
 */
@RestControllerAdvice
public class RestExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(RestExceptionHandler.class);

    /**
     * Wrong username/password (or user not found). We deliberately return the
     * same generic message for all of these so we don't leak whether a given
     * account exists.
     */
    @ExceptionHandler({BadCredentialsException.class, UsernameNotFoundException.class})
    public ResponseEntity<ApiResponse> handleBadCredentials(Exception ex) {
        logger.warn("Failed login attempt: {}", ex.getMessage());
        return new ResponseEntity<>(
                new ApiResponse(false, "Incorrect username or password"),
                HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<ApiResponse> handleDisabled(DisabledException ex) {
        return new ResponseEntity<>(
                new ApiResponse(false, "Your account is not enabled yet. " +
                        "Please confirm the link we sent to your email."),
                HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(LockedException.class)
    public ResponseEntity<ApiResponse> handleLocked(LockedException ex) {
        return new ResponseEntity<>(
                new ApiResponse(false, "Your account is locked."),
                HttpStatus.UNAUTHORIZED);
    }

    /**
     * Catch-all so unexpected errors never reach the client as a stack trace
     * or an empty body. The real cause is logged server-side.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> handleGeneric(Exception ex) {
        logger.error("Unexpected error handling request", ex);
        return new ResponseEntity<>(
                new ApiResponse(false, "Something went wrong. Please try again later."),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
