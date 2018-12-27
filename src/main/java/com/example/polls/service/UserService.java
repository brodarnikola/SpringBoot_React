package com.example.polls.service;

import com.example.polls.model.User;
import com.example.polls.repository.RoleRepository;
import com.example.polls.repository.UserRepository;
import com.example.polls.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserService implements IUserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // API
    @Override
    public void changeUserPassword(final UserPrincipal user, final String password) {

        user.setPassword(passwordEncoder.encode(password));

        User correctUser = userRepository.findByUsernameOrEmail( "", user.getEmail())
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found with username or email : " + user.getEmail())
        );

        correctUser.setPassword(user.getPassword());

        userRepository.save(correctUser);
    }

}
