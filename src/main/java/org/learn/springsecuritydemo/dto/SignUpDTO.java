package org.learn.springsecuritydemo.dto;

import lombok.Data;
import org.learn.springsecuritydemo.entities.enums.Permissions;
import org.learn.springsecuritydemo.entities.enums.Role;

import java.util.Set;

@Data
public class SignUpDTO {

    private String email;

    private String password;

    private String name;

    private Set<Role> roles;

    private Set<Permissions> permissions;
}
