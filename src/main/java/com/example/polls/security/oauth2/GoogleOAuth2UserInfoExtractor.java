package com.example.polls.security.oauth2;

import com.example.polls.config.SecurityConfig;
import com.example.polls.security.UserPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class GoogleOAuth2UserInfoExtractor implements OAuth2UserInfoExtractor {

    @Override
    public UserPrincipal extractUserInfo(OAuth2User oAuth2User) {
        UserPrincipal customUserDetails = new UserPrincipal();
        customUserDetails.setUsername(retrieveAttr("email", oAuth2User));
        customUserDetails.setName(retrieveAttr("name", oAuth2User));
        customUserDetails.setEmail(retrieveAttr("email", oAuth2User));
//        customUserDetails.setAvatarUrl(retrieveAttr("picture", oAuth2User));
        customUserDetails.setProvider(OAuth2Provider.GOOGLE);
         customUserDetails.setAttributes(oAuth2User.getAttributes());
         customUserDetails.setAuthorities(Collections.singletonList(new SimpleGrantedAuthority(SecurityConfig.USER)));
        return customUserDetails;
    }

    @Override
    public boolean accepts(OAuth2UserRequest userRequest) {
        return OAuth2Provider.GOOGLE.name().equalsIgnoreCase(userRequest.getClientRegistration().getRegistrationId());
    }

    private String retrieveAttr(String attr, OAuth2User oAuth2User) {
        Object attribute = oAuth2User.getAttributes().get(attr);
        return attribute == null ? "" : attribute.toString();
    }
}
