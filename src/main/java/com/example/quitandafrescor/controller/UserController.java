package com.example.quitandafrescor.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.quitandafrescor.dto.UserAuthenticationDTO;
import com.example.quitandafrescor.dto.UserLoginResponseDTO;
import com.example.quitandafrescor.dto.UserRequestDTO;
import com.example.quitandafrescor.dto.UserResponseDTO;
import com.example.quitandafrescor.dto.UserUpdateDTO;
import com.example.quitandafrescor.dto.UserUpdateDTOReturn;

import com.example.quitandafrescor.model.User;
import com.example.quitandafrescor.model.UserRole;
import com.example.quitandafrescor.repository.UserRepository;
import com.example.quitandafrescor.security.TokenService;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserRepository userRepository;

    private final AuthenticationManager authenticationManager;

    private final TokenService tokenService;

    public UserController(AuthenticationManager authenticationManager, UserRepository userRepository,
            TokenService tokenService) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.tokenService = tokenService;
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PostMapping("/login")
    public ResponseEntity<UserLoginResponseDTO> login(@RequestBody @Valid UserAuthenticationDTO data) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.email(), data.password());

        var auth = this.authenticationManager.authenticate(usernamePassword);

        var token = tokenService.generateToken((User) auth.getPrincipal());

        return ResponseEntity.ok(new UserLoginResponseDTO(token));
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PostMapping("/register")
    @Transactional
    public ResponseEntity<String> saveUser(@RequestBody @Valid UserRequestDTO data) {
        if (this.userRepository.findByEmail(data.email()) != null) {
            return ResponseEntity.badRequest().build();
        }

        String encryptedPassword = new BCryptPasswordEncoder().encode(data.password());

        UserRole role = data.role() != null ? data.role() : UserRole.CLIENT;

        User userData = new User(data.name(), data.phone(), data.email(), encryptedPassword, role);

        userRepository.save(userData);
        return ResponseEntity.ok().build();
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @GetMapping
    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll().stream().map(UserResponseDTO::new)
                .toList();
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            userRepository.delete(optionalUser.get());
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<UserUpdateDTOReturn> updateUser(@PathVariable Long id, @RequestBody UserUpdateDTO data) {
        Optional<User> optionalUser = userRepository.findById(id);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.updateUser(data);
            userRepository.save(user);
            return ResponseEntity.ok(new UserUpdateDTOReturn(user));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
