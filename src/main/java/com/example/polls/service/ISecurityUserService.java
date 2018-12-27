package com.example.polls.service;

import org.springframework.http.ResponseEntity;

public interface ISecurityUserService {

    ResponseEntity<?> validatePasswordResetToken(long id, String token);
}
