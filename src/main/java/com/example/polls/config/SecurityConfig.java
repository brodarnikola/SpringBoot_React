package com.example.polls.config;

import com.example.polls.security.CustomUserDetailsService;
import com.example.polls.security.JwtAuthenticationEntryPoint;
import com.example.polls.security.JwtAuthenticationFilter;
import com.example.polls.security.TokenProvider;
import com.example.polls.security.oauth2.CustomAuthenticationSuccessHandler;
import com.example.polls.security.oauth2.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

import static org.springframework.security.config.Customizer.withDefaults;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final long MAX_AGE_SECS = 3600;

    private final CustomOAuth2UserService customOauth2UserService;
    private final CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;

    private final JwtAuthenticationEntryPoint unauthorizedHandler;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public static final String ADMIN = "ADMIN";
    public static final String USER = "USER";

    @Value("${app.cors.allowed-origins}")
    private String allowedOrigin;

    // Another way of creating constructors
    public SecurityConfig(CustomOAuth2UserService customOauth2UserService,
                          CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler,
                          JwtAuthenticationEntryPoint unauthorizedHandler,
                          JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.customOauth2UserService = customOauth2UserService;
        this.customAuthenticationSuccessHandler = customAuthenticationSuccessHandler;
        this.unauthorizedHandler = unauthorizedHandler;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

//    @Bean
//    AuthenticationProvider authenticationProvider() {
//        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
//
//        authProvider.setUserDetailsService(customUserDetailsService);
//        authProvider.setPasswordEncoder(passwordEncoder());
//
//        return authProvider;
//    }

//    @Bean
//    public JwtAuthenticationFilter jwtAuthenticationFilter() {
//        return new JwtAuthenticationFilter(tokenProvider, customUserDetailsService);
//    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration
                .getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
//        configuration.setAllowedOrigins(List.of("http://localhost:3000")); // Replace with your frontend URL
        configuration.setAllowedOrigins(List.of(allowedOrigin)); // Replace with your frontend URL
        configuration.setAllowedMethods(List.of("HEAD", "GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(MAX_AGE_SECS);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // if we are using token-based authentication, such as JWT, then we can disable this CSRF, because JWT is protecting us
                .csrf(AbstractHttpConfigurer::disable)

//              one way how we can handle, protect us from csrf attacks if we have session based authentication
//              this needs to be handled on backend side and also on frontend size .. FE side then needs to use X-XSRF token
//
//                .csrf((csrf) -> csrf
//                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
//                        // https://stackoverflow.com/a/74521360/65681
//                        .csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler())
//                )
////
//                .addFilterAfter(new CookieCsrfFilter(), BasicAuthenticationFilter.class)
//                .addFilterAfter(new SpaWebFilter(), BasicAuthenticationFilter.class)

                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // Use the CorsConfigurationSource bean

                .authorizeHttpRequests(authorizeRequests ->
                                authorizeRequests
//                                .requestMatchers("/", "/favicon.ico", "/**/*.png", "/**/*.gif", "/**/*.svg", "/**/*.jpg", "/**/*.html", "/**/*.css", "/**/*.js").permitAll()
                                        .requestMatchers("/").permitAll()

                                        .requestMatchers("/api/auth/**").permitAll()

                                        .requestMatchers("/oauth2/**").permitAll()

                                        .requestMatchers("/api/user/checkUsernameAvailability", "/api/user/checkEmailAvailability").permitAll()

//                                       .requestMatchers(HttpMethod.OPTIONS, "/api/polls/**", "/api/users/**").permitAll()
                                        .requestMatchers(HttpMethod.GET, "/api/polls/**", "/api/users/**").permitAll()

//                                        .requestMatchers("/forgotPassword*").permitAll()

//                                        .requestMatchers("/changePassword*", "/user/savePassword*").hasAuthority("CHANGE_PASSWORD_PRIVILEGE")

                                        .anyRequest().authenticated()
                )
                .exceptionHandling(exceptionHandling ->
                        exceptionHandling.authenticationEntryPoint(unauthorizedHandler)
                )

                .sessionManagement(sessionManagement ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                .oauth2Login(oauth2Login -> oauth2Login
                        .userInfoEndpoint(userInfoEndpoint -> userInfoEndpoint.userService(customOauth2UserService))
                        .successHandler(customAuthenticationSuccessHandler))


                .logout(l -> l.logoutSuccessUrl("/").permitAll())
        ;

        ;

        // Add our custom JWT security filter
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }


}