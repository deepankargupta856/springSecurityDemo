package org.learn.springsecuritydemo.handlers;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.learn.springsecuritydemo.entities.User;
import org.learn.springsecuritydemo.repositories.UserRepository;
import org.learn.springsecuritydemo.services.JwtService;
import org.learn.springsecuritydemo.services.MyUserServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class Oauth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final MyUserServiceImpl userService;
    private final JwtService jwtService;

    @Value("${deploy.env}")
    private String deployEnv;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;
        DefaultOAuth2User oAuth2User = (DefaultOAuth2User) token.getPrincipal();

        String email = oAuth2User.getAttribute("email");

        User user = userService.getUserByEmail(email);

        if(user == null){
            User newUser = User.builder()
                    .email(email)
                    .name(oAuth2User.getAttribute("name"))
                    .build();

            user = userService.saveUser(newUser);
        }

        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        Cookie cookie = new Cookie("refreshToken",refreshToken);
        cookie.setHttpOnly(true);
        cookie.setSecure("production".equals(deployEnv));
        response.addCookie(cookie);

        String frontEndUrl = "http://localhost:8080/home.html?token="+accessToken;

        getRedirectStrategy().sendRedirect(request, response, frontEndUrl);


    }
}
