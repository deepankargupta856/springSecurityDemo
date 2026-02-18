package org.learn.springsecuritydemo.config;

import lombok.RequiredArgsConstructor;
import org.learn.springsecuritydemo.entities.enums.Role;
import org.learn.springsecuritydemo.filters.JwtAuthFilter;
import org.learn.springsecuritydemo.filters.LogginFilter;
import org.learn.springsecuritydemo.handlers.Oauth2SuccessHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.learn.springsecuritydemo.entities.enums.Role.ADMIN;
import static org.learn.springsecuritydemo.entities.enums.Role.CREATOR;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity(securedEnabled = true)
public class WebSecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final Oauth2SuccessHandler successHandler;
    private final LogginFilter logginFilter;

    private static final String[] publicRoutes = {
             "/public/**","/auth/**","/home.html"
    };

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{

        httpSecurity
                .authorizeHttpRequests(
                        auth -> auth
                                .requestMatchers(publicRoutes).permitAll()
                                .requestMatchers("/posts/**").authenticated()
                                .anyRequest().authenticated())
                .csrf(csrfConfig ->
                        csrfConfig.disable())
                .sessionManagement(sessionConfig ->
                        sessionConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .oauth2Login(oauth2Config -> oauth2Config
                        .failureUrl("/login?error=true")
                        .successHandler(successHandler));
                //.formLogin(Customizer.withDefaults());

        return httpSecurity.build();
    }

    //for testing purposes
//    @Bean
//    UserDetailsService inMemoryUserDetailService(){
//        UserDetails normalUser = User
//                .withUsername("deepankar")
//                .password(passwordEncoder().encode("deep123"))
//                .roles("USER")
//                .build();
//
//        UserDetails adminUser = User
//                .withUsername("Admin")
//                .password(passwordEncoder().encode("admin@123*"))
//                .roles("ADMIN")
//                .build();
//
//        UserDetails guidingUser = User
//                .withUsername("maharaj")
//                .password(passwordEncoder().encode("naamJap"))
//                .roles("GURU")
//                .build();
//
//        return new InMemoryUserDetailsManager(normalUser, adminUser, guidingUser);
//
//    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception{
        return config.getAuthenticationManager();
    }
}
