package org.learn.springsecuritydemo.controllers;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.learn.springsecuritydemo.dto.LoginDTO;
import org.learn.springsecuritydemo.dto.LoginResponseDTO;
import org.learn.springsecuritydemo.dto.SignUpDTO;
import org.learn.springsecuritydemo.dto.UserDTO;
import org.learn.springsecuritydemo.services.AuthService;
import org.learn.springsecuritydemo.services.MyUserServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final MyUserServiceImpl userService;
    private final AuthService authService;

    @Value("${deploy.env}")
    private String deployEnv;

    @PostMapping("/signup")
    public ResponseEntity<UserDTO> signUp(@RequestBody SignUpDTO signUpDTO){
        UserDTO userDTO = userService.signIn(signUpDTO);
        return ResponseEntity.ok(userDTO);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginDTO loginDTO,
                                        HttpServletRequest httpServletRequest,
                                        HttpServletResponse httpServletResponse){

        LoginResponseDTO loginResponseDTO = authService.login(loginDTO);

        Cookie cookie = new Cookie("refreshToken",loginResponseDTO.getRefreshToken());
        cookie.setHttpOnly(true);
        cookie.setSecure("production".equals(deployEnv));
        httpServletResponse.addCookie(cookie);

        return ResponseEntity.ok(loginResponseDTO);
    }

    @PostMapping("/refresh")
    public ResponseEntity<LoginResponseDTO> refresh(HttpServletRequest request){
        String refreshToken = Arrays.stream(request.getCookies())
                .filter(cookie -> "refreshToken".equals(cookie.getName()))
                .findFirst()
                .map(Cookie::getValue)
                .orElseThrow(() -> new AuthenticationServiceException("Refresh token not found inside cookies"));

        LoginResponseDTO loginResponseDTO = authService.refreshToken(refreshToken);

        return ResponseEntity.ok(loginResponseDTO);
    }

}
