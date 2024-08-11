package com.example.polls.config;

//import com.example.polls.security.CustomUserDetailsService;
//import com.example.polls.security.JwtAuthenticationEntryPoint;
//import com.example.polls.security.JwtAuthenticationFilter;
//import com.example.polls.security.UserPrincipal;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.HttpMethod;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.config.BeanIds;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//
//
//
//@Configuration
//@EnableWebSecurity
//@EnableGlobalMethodSecurity(
//        securedEnabled = true,
//        jsr250Enabled = true,
//        prePostEnabled = true
//)
//public class SecurityConfig extends WebSecurityConfigurerAdapter {
//
//    @Autowired
//    CustomUserDetailsService customUserDetailsService;
//
//    @Autowired
//    private JwtAuthenticationEntryPoint unauthorizedHandler;
//
//    @Bean
//    public JwtAuthenticationFilter jwtAuthenticationFilter() {
//        return new JwtAuthenticationFilter();
//    }
//
//    @Override
//    public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
//        authenticationManagerBuilder
//                .userDetailsService(customUserDetailsService)
//                .passwordEncoder(passwordEncoder());
//    }
//
////     @Autowired
////    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
////        auth.userDetailsService(customUserDetailsService).passwordEncoder(passwordEncoder());
////    }
//
//    @Bean(BeanIds.AUTHENTICATION_MANAGER)
//    @Override
//    public AuthenticationManager authenticationManagerBean() throws Exception {
//        return super.authenticationManagerBean();
//    }
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http
//                .cors()
//                .and()
//                .csrf()
//                .disable()
//                .exceptionHandling()
//                .authenticationEntryPoint(unauthorizedHandler)
//                .and()
//                .sessionManagement()
//                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                .and()
//                .authorizeRequests()
//                .antMatchers("/",
//                        "/favicon.ico",
//                        "/**/*.png",
//                        "/**/*.gif",
//                        "/**/*.svg",
//                        "/**/*.jpg",
//                        "/**/*.html",
//                        "/**/*.css",
//                        "/**/*.js")
//                .permitAll()
//                .antMatchers("/api/auth/**")
//                .permitAll()
//                .antMatchers("/api/user/checkUsernameAvailability", "/api/user/checkEmailAvailability")
//                .permitAll()
//                .antMatchers(HttpMethod.GET, "/api/polls/**", "/api/users/**")
//                .permitAll()
//                .antMatchers("/forgotPassword*", "/changePassword*", "/user/savePassword*")
//                .hasAuthority("CHANGE_PASSWORD_PRIVILEGE")
//                .anyRequest()
//                .authenticated();
//
//        // Add our custom JWT security filter
//        //http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
//        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
//
//    }
//}


import com.example.polls.security.CustomUserDetailsService;
import com.example.polls.security.JwtAuthenticationEntryPoint;
import com.example.polls.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final long MAX_AGE_SECS = 3600;

//    private final AuthenticationProvider authenticationProvider;

     private final CustomUserDetailsService customUserDetailsService;
    private final JwtAuthenticationEntryPoint unauthorizedHandler;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

//    public SecurityConfig(CustomUserDetailsService customUserDetailsService, JwtAuthenticationEntryPoint unauthorizedHandler,
//                          AuthenticationProvider authenticationProvider) {
//        this.customUserDetailsService = customUserDetailsService;
//        this.unauthorizedHandler = unauthorizedHandler;
//        this.authenticationProvider = authenticationProvider;
//    }

    public SecurityConfig(
            CustomUserDetailsService customUserDetailsService,
            JwtAuthenticationFilter jwtAuthenticationFilter,
            JwtAuthenticationEntryPoint unauthorizedHandler  ) {
//        this.authenticationProvider = authenticationProvider;
        this.customUserDetailsService = customUserDetailsService;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.unauthorizedHandler = unauthorizedHandler;
    }

    @Bean
    AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(customUserDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

//    @Bean
//    public JwtAuthenticationFilter jwtAuthenticationFilter() {
//        return new JwtAuthenticationFilter(customUserDetailsService);
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
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("http://localhost:5000")
                        .allowedOrigins("*")
//                       .allowCredentials(true)
                        .allowedHeaders("Authorization", "Content-Type")
                        .allowedMethods("HEAD",  "GET", "POST", "PUT", "PATCH", "DELETE")
                        .maxAge(MAX_AGE_SECS);
//                registry.addMapping("/**")
//                        .allowedOrigins("http://allowed-origin.com")
//                        .allowedMethods("GET", "POST", "PUT", "DELETE")
            }
        };
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:3000")); // Replace with your frontend URL
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
                .csrf(AbstractHttpConfigurer::disable)

                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // Use the CorsConfigurationSource bean

//                .cors(corsCustomizer -> {
//                    // Use the custom CORS configuration from your WebMvcConfigurer bean
//                    corsCustomizer.configurationSource(request -> {
//                        CorsRegistry registry = new CorsRegistry();
//                        corsConfigurer().addCorsMappings(registry);
//                        return registry.getCorsConfigurations().get(request.getRequestURI());
//                    });
//                })

                .authorizeHttpRequests(authorizeRequests ->
                                authorizeRequests
//                                .requestMatchers("/", "/favicon.ico", "/**/*.png", "/**/*.gif", "/**/*.svg", "/**/*.jpg", "/**/*.html", "/**/*.css", "/**/*.js").permitAll()
                                        .requestMatchers("/").permitAll()

                                        .requestMatchers("/api/auth/**").permitAll()
                                        .requestMatchers("/api/user/checkUsernameAvailability", "/api/user/checkEmailAvailability").permitAll()

//                                       .requestMatchers(HttpMethod.OPTIONS, "/api/polls/**", "/api/users/**").permitAll()
                                        .requestMatchers(HttpMethod.GET, "/api/polls/**", "/api/users/**").permitAll()

                                        .requestMatchers("/forgotPassword*", "/changePassword*", "/user/savePassword*").hasAuthority("CHANGE_PASSWORD_PRIVILEGE")
                                        .anyRequest().authenticated()
                )

//                .csrf(csrf -> csrf.disable())

//                .addFilterBefore(new CustomFilter(), UsernamePasswordAuthenticationFilter.class) // Example of adding a custom filter

//                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()

                .exceptionHandling(exceptionHandling ->
                        exceptionHandling.authenticationEntryPoint(unauthorizedHandler)
                )

//                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .sessionManagement(sessionManagement ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

//                .authenticationProvider(authenticationProvider)
        ;

        // Add our custom JWT security filter
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

//    @Bean
//    CorsConfigurationSource corsConfigurationSource() {
//        CorsConfiguration configuration = new CorsConfiguration();
//
//        // Replace "http://localhost:8005" with your frontend's URL
////        configuration.setAllowedOrigins(List.of("http://localhost:5000"));
//        configuration.setAllowedOrigins(List.of("*", "http://localhost:5000"));
//        configuration.setAllowedMethods(List.of("HEAD", "OPTIONS", "GET", "POST", "PUT", "PATCH", "DELETE"));
//        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));
//        configuration.setAllowCredentials(true); // If you're handling credentials
//
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", configuration);
//
//        return source;
//    }

    // Define your custom filter if needed
//    public static class CustomFilter extends BasicAuthenticationFilter {
//        public CustomFilter() {
//            super(null);
//        }
//        // Custom implementation here
//    }
}