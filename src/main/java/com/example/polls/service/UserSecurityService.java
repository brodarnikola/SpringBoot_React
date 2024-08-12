package com.example.polls.service;

import java.util.Arrays;
import java.util.Calendar;

import com.example.polls.security.TokenProvider;
import jakarta.transaction.Transactional;

import com.example.polls.model.VerificationToken;
import com.example.polls.model.User;
import com.example.polls.payload.JwtAuthenticationResponse;
import com.example.polls.repository.PasswordResetTokenRepository;
import com.example.polls.security.CustomUserDetailsService;
import com.example.polls.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class UserSecurityService implements ISecurityUserService {

    @Autowired
    private PasswordResetTokenRepository passwordTokenRepository;

    @Autowired
    CustomUserDetailsService customUserDetailsService;


    @Autowired
    AuthenticationManager authenticationManager;


    @Autowired
    TokenProvider tokenProvider;

    // API
    @Override
    public ResponseEntity<?> validatePasswordResetToken(long id, String token) {
        final VerificationToken passToken = passwordTokenRepository.findByToken(token);
        if ((passToken == null) || (passToken.getUser().getId() != id)) {
            return ResponseEntity.ok("invalidToken");
        }

        final Calendar cal = Calendar.getInstance();
        if ((passToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
            return ResponseEntity.ok("expired");
        }

        User user = passToken.getUser();
        UserPrincipal userPrincipal = UserPrincipal.create(user);

        Authentication auth = new UsernamePasswordAuthenticationToken(
                userPrincipal, null, Arrays.asList(
                new SimpleGrantedAuthority("CHANGE_PASSWORD_PRIVILEGE")));
        SecurityContextHolder.getContext().setAuthentication(auth);


        String jwt = tokenProvider.generate(auth);
        return ResponseEntity.ok(new JwtAuthenticationResponse(jwt));
    }

}