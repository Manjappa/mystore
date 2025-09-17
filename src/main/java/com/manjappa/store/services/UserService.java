package com.manjappa.store.services;

import com.manjappa.store.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        com.manjappa.store.entities.User userEntity=userRepository.findByEmail(email).orElseThrow(
                () -> new UsernameNotFoundException("User not found"));
        //providing the userEntity email and password to compare with Sping User
        // which is implementations of interface UserDetailsService
        return new User(userEntity.getEmail(), userEntity.getPassword(), Collections.emptyList());
    }
}
