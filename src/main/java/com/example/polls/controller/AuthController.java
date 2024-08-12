package com.example.polls.controller;


import com.example.polls.exception.AppException;
import com.example.polls.model.*;
import com.example.polls.payload.*;
import com.example.polls.repository.PasswordResetTokenRepository;
import com.example.polls.repository.RoleRepository;
import com.example.polls.repository.UserRepository;
import com.example.polls.security.TokenProvider;
import com.example.polls.security.UserPrincipal;
import com.example.polls.service.RegistrationListener;
import com.example.polls.service.UserSecurityService;
import com.example.polls.service.UserService;
import org.springframework.core.env.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import jakarta.validation.Valid;
import java.net.URI;
import java.util.*;

import org.springframework.mail.javamail.JavaMailSender;

import org.springframework.mail.SimpleMailMessage;



/**
 * Created by rajeevkumarsingh on 02/08/17.
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordResetTokenRepository passwordResetTokenRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    private UserSecurityService securityUserService;

    @Autowired
    private UserService userService;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private Environment env;

    @Autowired
    TokenProvider tokenProvider;

    @Autowired
    private RegistrationListener registrationListener;

    @PostMapping("/forgotPassword")
    public ResponseEntity<?> forgotPassword(@Valid @RequestBody ForgotPasswordRequest forgotPasswordRequest) {

        User user = userRepository.findByUsernameOrEmail("", forgotPasswordRequest.getEmail())
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found with username or email : " + forgotPasswordRequest.getEmail())
                );

        //Locale locale =  LocaleContextHolder.getLocale();

        String token = UUID.randomUUID().toString();
        createPasswordResetTokenForUser(user, token);
        mailSender.send(constructResetTokenEmail(token, user));
        //return new GenericResponse(
        //        messages.getMessage("message.resetPasswordEmail", null,
        //                locale));

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/login")
                .buildAndExpand(forgotPasswordRequest.getEmail()).toUri();

        return ResponseEntity.created(location).body(new ApiResponse(true, "Reset password has been send"));
    }

    /* private String getAppUrl(HttpServletRequest request) {
        return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    } */

    private String getAppUrl() {
        return "http://localhost:3000";
    }

    public void createPasswordResetTokenForUser(User user, String token) {
        VerificationToken myToken = new VerificationToken(token, user);
        passwordResetTokenRepository.save(myToken);
    }

    private SimpleMailMessage constructResetTokenEmail(String token, User user) {

        String url = getAppUrl() + "/changePassword?id=" +
                user.getId() + "&token=" + token;
        return constructEmail("Reset Password",
                "Reset password message: " + url + " \r\n", user);
    }

    private SimpleMailMessage  constructEmail(String subject, String body,
                                             User user) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setSubject(subject);
        message.setText(body);

        return message;
    }

    @RequestMapping(value = "/changePassword", method = RequestMethod.GET)
    public ResponseEntity<?> showChangePasswordPage(Locale locale, Model model,
                                                    @RequestParam("id") long id,
                                                    @RequestParam("token") String token) {

        ResponseEntity<?> result = securityUserService.validatePasswordResetToken(id, token);
        if (result.getBody().equals("expired") || result.getBody().equals("invalidToken")) {

            return new ResponseEntity(new ApiResponse(false, "" +
                    "Please send one more password recovery. \n Your token has expired or is invalid."),
                    HttpStatus.BAD_REQUEST);
        }

        return result;
    }

    @PostMapping("/user/savePassword")
    public ResponseEntity<?> savePassword(@Valid @RequestBody SavePasswordRequest savePasswordRequest) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        UserPrincipal user = (UserPrincipal) authentication.getPrincipal();

        /* if (authentication == null ||
                !authentication.isAuthenticated() ||
                authentication instanceof AnonymousAuthenticationToken) {
            return Optional.empty();
        } */

        //UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();n

        userService.changeUserPassword(user, savePasswordRequest.getPassword());

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/login")
                .buildAndExpand().toUri();

        return ResponseEntity.created(location).
                body(new ApiResponse(true, "New password has been saved successfully"));
        //return new GenericResponse(
        //        messages.getMessage("message.resetPasswordSuc", null, locale));
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsernameOrEmail(),
                        loginRequest.getPassword()
                )
        );

        UserPrincipal user = (UserPrincipal) authentication.getPrincipal();

        if (user.isEnabledUser() == true) {

            SecurityContextHolder.getContext().setAuthentication(authentication);

            String jwt = tokenProvider.generate(authentication);
            return ResponseEntity.ok(new JwtAuthenticationResponse(jwt));
        } else
            return new ResponseEntity(new ApiResponse(false, "You did not confirm your link in email. " +
                    "Please check inbox or spam in your email account."),
                    HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {

        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return new ResponseEntity(new ApiResponse(false, "Username is already taken!"),
                    HttpStatus.BAD_REQUEST);
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return new ResponseEntity(new ApiResponse(false, "Email Address already in use!"),
                    HttpStatus.BAD_REQUEST);
        }

        // Creating user's account
        User user = new User(signUpRequest.getName(), signUpRequest.getUsername(),
                signUpRequest.getEmail(), signUpRequest.getPassword());

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        Role userRole;
        if (signUpRequest.getSifra().equals("1234")) {
            userRole = roleRepository.findByName(RoleName.ROLE_ADMIN)
                    .orElseThrow(() -> new AppException("User Role not set."));
        } else {
            userRole = roleRepository.findByName(RoleName.ROLE_USER)
                    .orElseThrow(() -> new AppException("User Role not set."));
        }

        user.setRoles(Collections.singleton(userRole));

        User result = userRepository.save(user);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/users/{username}")
                .buildAndExpand(result.getUsername()).toUri();

        registrationListener.confirmRegistration(user.getEmail(), user.getUsername());

        return ResponseEntity.created(location).body(new ApiResponse(true, "User registered successfully"));
    }

    @GetMapping("/signUpConfirm")
    public ResponseEntity<?> confirmRegistration(WebRequest request, Model model, @RequestParam("token") String token) {

        VerificationToken verificationToken = userService.getVerificationToken(token);
        if (verificationToken == null) {

            return new ResponseEntity(new ApiResponse(false, "" +
                    " Your link is invalid. \n We have send your one more link to your mail account. Please check your inbox or spam."),
                    HttpStatus.BAD_REQUEST);
        }

        User user = verificationToken.getUser();
        Calendar cal = Calendar.getInstance();

        if ((verificationToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {

            VerificationToken newToken = userService.generateNewVerificationToken(token);

            SimpleMailMessage email = registrationListener.constructEmailMessage(user, newToken.getToken());
            mailSender.send(email);

            return new ResponseEntity(new ApiResponse(false,
                    "We have send you new link to confirm your registration. \n" +
                    "Please check your inbox or spam in mail account."),
                    HttpStatus.BAD_REQUEST);
        }

        user.setEnabled(true);
        userService.saveRegisteredUser(user);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/login")
                .buildAndExpand().toUri();

        return ResponseEntity.created(location).
                body(new ApiResponse(true, "You have confirmed your link. \n" +
                        "Now you can login."));
    }


}
