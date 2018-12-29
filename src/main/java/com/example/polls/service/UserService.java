package com.example.polls.service;

import com.example.polls.model.VerificationToken;
import com.example.polls.model.User;
import com.example.polls.repository.PasswordResetTokenRepository;
import com.example.polls.repository.UserRepository;
import com.example.polls.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
public class UserService implements IUserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private PasswordResetTokenRepository tokenRepository;

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

    @Override
    public void createVerificationTokenForUser(User user, String token) {
        final VerificationToken myToken = new VerificationToken(token, user);
        tokenRepository.save(myToken);
    }

    @Override
    public VerificationToken getVerificationToken(String VerificationToken) {
        return tokenRepository.findByToken(VerificationToken);
    }

    @Override
    public void saveRegisteredUser(User user) {
        userRepository.save(user);
    }

    @Override
    public VerificationToken generateNewVerificationToken(String existingVerificationToken) {
        VerificationToken vToken = tokenRepository.findByToken(existingVerificationToken);
        vToken.updateToken(UUID.randomUUID()
                .toString());
        vToken = tokenRepository.save(vToken);
        return vToken;
    }

}
