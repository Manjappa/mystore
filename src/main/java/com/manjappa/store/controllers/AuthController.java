package com.manjappa.store.controllers;

import com.manjappa.store.config.JwtConfig;
import com.manjappa.store.dtos.JwtResponseDto;
import com.manjappa.store.dtos.LoginDto;
import com.manjappa.store.dtos.UserDto;
import com.manjappa.store.entities.User;
import com.manjappa.store.mapper.UserMapper;
import com.manjappa.store.repositories.UserRepository;
import com.manjappa.store.services.Jwt;
import com.manjappa.store.services.JwtService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@AllArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final JwtConfig jwtConfig;
    private final UserMapper userMapper;

    //Authentication using BCrypt hashing algorithm/not encryption
    //Generate JWT tokens
    @PostMapping("/login")
    public ResponseEntity<JwtResponseDto> login(@Valid @RequestBody LoginDto loginDto,
                                                HttpServletResponse response) {
       authenticationManager.authenticate(
               new UsernamePasswordAuthenticationToken( loginDto.getEmail(), loginDto.getPassword() )
       );
       User user = userRepository.findByEmail(loginDto.getEmail()).orElseThrow();
        Jwt accessToken = jwtService.generateAccessToken(user);
        Jwt refreshToken =jwtService.generateRefreshToken(user);
        System.out.println("refreshToken::::::::::::"+ refreshToken);
        //var cookie = new Cookie("refreshToken", refreshToken.toString());
        var cookie = new Cookie("refreshToken", refreshToken.toString());
        cookie.setHttpOnly(true);
        //cookie.setPath("/auth/refresh");
        cookie.setPath("/");
        cookie.setSecure(false);
        cookie.setMaxAge(jwtConfig.getRefreshTokenExpiration());
        response.addCookie(cookie);
        return ResponseEntity.ok(new JwtResponseDto(accessToken.toString()));
    }

    @PostMapping("/refresh")
    public ResponseEntity<JwtResponseDto> refresh( @CookieValue(value = "refreshToken") String refreshToken) {
        var jwt=jwtService.parseToken(refreshToken);
        if(jwt ==null || jwt.isExpired()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        User user =  userRepository.findById(jwt.getUserId()).orElseThrow();
        Jwt accessToken= jwtService.generateAccessToken(user);
        return ResponseEntity.ok(new JwtResponseDto(accessToken.toString()));
    }

    @GetMapping("/me")
    public ResponseEntity<UserDto> me() {
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
         Long userId= (Long) authentication.getPrincipal();

         User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        UserDto userDto = userMapper.toDto(user);
        return ResponseEntity.ok(userDto);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Map<String, String>> badCredentialsException() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid Email or Password!"));
    }
}
