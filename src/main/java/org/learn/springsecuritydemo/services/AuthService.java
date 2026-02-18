package org.learn.springsecuritydemo.services;

import lombok.RequiredArgsConstructor;
import org.learn.springsecuritydemo.dto.LoginDTO;
import org.learn.springsecuritydemo.dto.LoginResponseDTO;
import org.learn.springsecuritydemo.entities.User;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final MyUserServiceImpl userService;
    private final SessionService sessionService;

    public LoginResponseDTO login(LoginDTO loginDTO) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword())
        );

        User user = (User) authentication.getPrincipal();

        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        sessionService.generateNewSession(user,refreshToken);

        return new LoginResponseDTO(user.getId(), accessToken, refreshToken);
    }

    public LoginResponseDTO refreshToken(String refreshToken) {
        Long userId = jwtService.getUserIdInfoFromToken(refreshToken);
        sessionService.validateSession(refreshToken);
        User user = userService.getUserById(userId);
        String accessToken = jwtService.generateAccessToken(user);
        return new LoginResponseDTO(userId, accessToken, refreshToken);
    }

//
//    public String signUp(SignUpDTO signUpDTO){
//
//
//        User user = (user)
//        return jwtService.generateToken(user);
//    }


}
