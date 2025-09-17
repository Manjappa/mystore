package com.manjappa.store.controllers;

import com.manjappa.store.dtos.JwtResponseDto;
import com.manjappa.store.dtos.LoginDto;
import com.manjappa.store.services.JwtService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@AllArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {

    AuthenticationManager authenticationManager;
    JwtService jwtService;

    //Authentication using BCrypt hashing algorithm/not encryption
    //Generate JWT tokens
    @PostMapping("/login")
    public ResponseEntity<JwtResponseDto> login(@Valid @RequestBody LoginDto loginDto) {
       authenticationManager.authenticate(
               new UsernamePasswordAuthenticationToken( loginDto.getEmail(), loginDto.getPassword() )
       );
        String token= jwtService.generateToken(loginDto.getEmail());
        return ResponseEntity.ok(new JwtResponseDto(token));
    }

    @PostMapping("/validate")
    public boolean validate(@RequestHeader("Authorization") String authHeader) {
        System.out.println("/validate called!!");
        String token = authHeader.replace("Bearer ", "");
        return jwtService.validateToken(token);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Map<String, String>> badCredentialsException() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid Email or Password!"));
    }
}
