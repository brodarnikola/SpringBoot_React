package com.example.polls.security.oauth2;

import com.example.polls.config.SecurityConfig;
import com.example.polls.exception.AppException;
import com.example.polls.model.Role;
import com.example.polls.model.RoleName;
import com.example.polls.model.User;
import com.example.polls.repository.RoleRepository;
import com.example.polls.repository.UserRepository;
import com.example.polls.security.UserPrincipal;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final List<OAuth2UserInfoExtractor> oAuth2UserInfoExtractors;

    public CustomOAuth2UserService(UserRepository userRepository, RoleRepository roleRepository, List<OAuth2UserInfoExtractor> oAuth2UserInfoExtractors) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.oAuth2UserInfoExtractors = oAuth2UserInfoExtractors;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        Optional<OAuth2UserInfoExtractor> oAuth2UserInfoExtractorOptional = oAuth2UserInfoExtractors.stream()
                .filter(oAuth2UserInfoExtractor -> oAuth2UserInfoExtractor.accepts(userRequest))
                .findFirst();
        if (oAuth2UserInfoExtractorOptional.isEmpty()) {
            throw new InternalAuthenticationServiceException("The OAuth2 provider is not supported yet");
        }

        UserPrincipal customUserDetails = oAuth2UserInfoExtractorOptional.get().extractUserInfo(oAuth2User);
        User user = upsertUser(customUserDetails);
        customUserDetails.setId(user.getId());
        return customUserDetails;
    }

    private User upsertUser(UserPrincipal customUserDetails) {
        Optional<User> userOptional = userRepository.findByUsername(customUserDetails.getUsername());
        User user;
        if (userOptional.isEmpty()) {
            user = new User();
            user.setUsername(customUserDetails.getUsername());
            user.setName(customUserDetails.getName());
            user.setEmail(customUserDetails.getEmail());
//            user.setImageUrl(customUserDetails.getAvatarUrl());
            user.setProvider(customUserDetails.getProvider());

            Role userRole = roleRepository.findByName(RoleName.ROLE_USER).orElseThrow(() -> new AppException("User Role not set."));
//            if (signUpRequest.getSifra().equals("1234")) {
//                userRole = roleRepository.findByName(RoleName.ROLE_ADMIN)
//                        .orElseThrow(() -> new AppException("User Role not set."));
//            } else {
//                userRole = roleRepository.findByName(RoleName.ROLE_USER)
//                        .orElseThrow(() -> new AppException("User Role not set."));
//            }

            user.setRoles(Collections.singleton(userRole));
        } else {
            user = userOptional.get();
            user.setEmail(customUserDetails.getEmail());
//            user.setImageUrl(customUserDetails.getAvatarUrl());
        }

        return userRepository.save(user);
    }
}
