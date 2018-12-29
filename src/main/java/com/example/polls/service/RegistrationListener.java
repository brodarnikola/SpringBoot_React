package com.example.polls.service;


import java.util.UUID;

import com.example.polls.model.OnRegistrationCompleteEvent;
import com.example.polls.model.User;
import com.example.polls.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import javax.swing.text.html.Option;

@Component
public class RegistrationListener  {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private IUserService service;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private Environment env;

    // API

    //@Override
    //public void onApplicationEvent(final OnRegistrationCompleteEvent event) {
    //    this.confirmRegistration();
    //}

    public void confirmRegistration( String email, String username) {

        User user = userRepository.findByUsernameOrEmail( username, email)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found with username or email : " + email)
        );

        final String token = UUID.randomUUID().toString();
        service.createVerificationTokenForUser(user, token);


        final SimpleMailMessage sendEmail = constructEmailMessage( user, token);
        mailSender.send(sendEmail);
    }

    public final SimpleMailMessage constructEmailMessage(
            final User user, final String token) {

        final String recipientAddress = user.getEmail();
        final String subject = "Registration Confirmation";
        final String confirmationUrl = "http://localhost:3000/signUpConfirm?token=" + token;
        final SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(recipientAddress);
        email.setSubject(subject);
        email.setText( "Please confirm your registraition with click on this link \r\n" + confirmationUrl);
        email.setFrom(env.getProperty("support.email"));
        return email;
    }

}
