package org.learn.springsecuritydemo.services;

import lombok.RequiredArgsConstructor;
import org.learn.springsecuritydemo.dto.SignUpDTO;
import org.learn.springsecuritydemo.dto.UserDTO;
import org.learn.springsecuritydemo.entities.User;
import org.learn.springsecuritydemo.repositories.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MyUserServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;
    private final ModelMapper mapper;
    private final PasswordEncoder passwordEncoder;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username)
                .orElseThrow(
                        () -> new BadCredentialsException("User with email "+ username + " not found")
                );
    }

    public User getUserById(Long userId){
        return userRepository.findById(userId).orElseThrow(() -> new BadCredentialsException("user not found with id " + userId));
    }

    public User getUserByEmail(String email){
        return userRepository.findByEmail(email).orElse(null);
    }

    public User saveUser(User user){
        return userRepository.save(user);
    }

    public UserDTO signIn(SignUpDTO signUpDTO){
        Optional<User> user = userRepository.findByEmail(signUpDTO.getEmail());

        if(user.isPresent()) {
            throw new BadCredentialsException("User with email already exists "+ signUpDTO.getEmail());
        }

        User toBeCreated = mapper.map(signUpDTO, User.class);
        toBeCreated.setPassword(passwordEncoder.encode(toBeCreated.getPassword()));

        User savedUser = userRepository.save(toBeCreated);
        return mapper.map(savedUser, UserDTO.class);
    }


}
