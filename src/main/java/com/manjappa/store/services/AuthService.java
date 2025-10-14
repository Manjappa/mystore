package com.manjappa.store.services;

import com.manjappa.store.entities.User;
import com.manjappa.store.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class AuthService {
    private final UserRepository userRepository;

    public User getCurrentUser() {
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        Long userId= (Long) authentication.getPrincipal();

        return userRepository.findById(userId).orElse(null);
    }
}
