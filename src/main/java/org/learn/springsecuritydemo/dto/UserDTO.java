package org.learn.springsecuritydemo.dto;

import lombok.Data;

@Data
public class UserDTO {

    private Long id;
    private String email;
    private String name;
}
