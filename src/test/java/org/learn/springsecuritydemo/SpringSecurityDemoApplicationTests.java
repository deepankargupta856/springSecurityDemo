package org.learn.springsecuritydemo;

import org.junit.jupiter.api.Test;
import org.learn.springsecuritydemo.entities.User;
import org.learn.springsecuritydemo.services.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SpringSecurityDemoApplicationTests {

    @Autowired
    private JwtService jwtService;

    @Test
    void contextLoads() {

        User user = new User(4L, "deep@sharam.radha","12345");

        String token  = jwtService.generateAccessToken(user);

        System.out.println(token);

        Long id = jwtService.getUserIdInfoFromToken(token);

        System.out.println(id);


    }

}
