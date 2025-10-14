package com.manjappa.store.controllers;

import com.manjappa.store.dtos.*;
import com.manjappa.store.entities.Role;
import com.manjappa.store.entities.User;
import com.manjappa.store.mapper.UserMapper;
import com.manjappa.store.repositories.UserRepository;
import com.manjappa.store.services.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/users")
public class UserController {
    //private final List<UserDto> listUserDto = new ArrayList<>();
    //private final UserDto userDto =  new UserDto();
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;

    //    UserController(UserRepository userRepository, UserMapper userMapper) {
//        this.userRepository = userRepository;
//        this.userMapper = userMapper;
//    }
    @GetMapping
    //Http methods GET, PUT, POST, DELETE
    public Iterable<UserDto> getAllUSers(@RequestParam(required = false, defaultValue = "") String sort) {
        if(!Set.of("name", "email").contains(sort)) {
            sort = "name";
        }
        return userRepository.findAll(
                Sort.by(sort)
        ).stream().map(userMapper::toDto).toList();

//       return userRepository.findAll().stream().map(user ->
//       new UserDto(user.getId(), user.getName(), user.getEmail()) ).toList();

    }

/*    @GetMapping("/myusers")
    public List<UserDto> getMyUsers() {
        List<User> users = userRepository.findAll();

        for (User user : users) {
            userDto.setId(user.getId());
            userDto.setName(user.getName());
            userDto.setEmail(user.getEmail());
            listUserDto.add(userDto);
        }
       return listUserDto;
    } */

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable Long id) {
        var user= userRepository.findById(id).orElse(null);
        if (user == null) {
            //return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            return ResponseEntity.notFound().build();
        }
        //UserDto userDto = new UserDto(user.getId(), user.getName(), user.getEmail());
        return  ResponseEntity.ok(userMapper.toDto(user));
    }

    @PostMapping
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserRegisterDto userRegisterDto) {
        if (userRepository.existsByEmail(userRegisterDto.getEmail())) {
            return ResponseEntity.badRequest().body(
                    Map.of("email", "Email is already registered.")
            );
        }
        User user = userMapper.toUserEntity(userRegisterDto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.USER);
        userRepository.save(user);
        return ResponseEntity.ok(userMapper.toDto(user));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long id,
                             @RequestBody(required = true) UpdateUserDto updateUserDto) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        userMapper.update(updateUserDto, user);
        userRepository.save(user);
        return ResponseEntity.ok(userMapper.toDto(user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") Long id) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        userRepository.delete(user);
        return ResponseEntity.noContent().build();
    }

    //Change password
    @PostMapping("{id}/change-password")
    public ResponseEntity<Void> changePassword(@PathVariable Long id,
                               @RequestBody ChangePasswordDto userDto) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
           return ResponseEntity.notFound().build();
        }
        if (!user.getPassword().equals(userDto.getOldPassword())) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        user.setPassword(userDto.getNewPassword());
        userRepository.save(user);
        return ResponseEntity.noContent().build();
    }

}
