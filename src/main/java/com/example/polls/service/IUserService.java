package com.example.polls.service;

import com.example.polls.security.UserPrincipal;

public interface IUserService {

    void changeUserPassword(UserPrincipal user, String password);
}
