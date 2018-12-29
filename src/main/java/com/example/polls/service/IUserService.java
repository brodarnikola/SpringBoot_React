package com.example.polls.service;

import com.example.polls.model.VerificationToken;
import com.example.polls.model.User;
import com.example.polls.security.UserPrincipal;

public interface IUserService {

    void changeUserPassword(UserPrincipal user, String password);

    void createVerificationTokenForUser(User user, String token);

    VerificationToken getVerificationToken(String VerificationToken);

    void saveRegisteredUser(User user);

    VerificationToken generateNewVerificationToken(String token);
}
