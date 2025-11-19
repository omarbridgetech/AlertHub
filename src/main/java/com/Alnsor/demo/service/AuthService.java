package com.Alnsor.demo.service;

import com.Alnsor.demo.domain.entity.Role;
import com.Alnsor.demo.domain.entity.User;
import com.Alnsor.demo.domain.entity.UserRole;
import com.Alnsor.demo.domain.repository.UserRepository;
import com.Alnsor.demo.dto.LoginRequest;
import com.Alnsor.demo.dto.LoginResponse;
import com.Alnsor.demo.dto.UserDto;
import com.Alnsor.demo.security.JwtService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;

    @Value("${jwt.expiration-ms}")
    private long expirationMs;

    @Value("${app.ai.enabledModels:}")
    private String enabledModels;

    public AuthService(AuthenticationManager authenticationManager, JwtService jwtService, UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    public LoginResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        User user = userRepository.findByUsername(request.getUsername()).orElseThrow();
        String token = jwtService.generateToken(user);
        List<String> perms = user.getUserRoles().stream()
                .map(UserRole::getRole)
                .filter(Objects::nonNull)
                .map(Role::getRole)
                .filter(Objects::nonNull)
                .distinct().collect(Collectors.toList());
        return new LoginResponse(token, "Bearer", expirationMs, user.getId(), user.getUsername(), user.isAdmin(), perms);
    }

    public UserDto me() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username).orElseThrow();
        List<String> perms = user.getUserRoles().stream()
                .map(UserRole::getRole)
                .filter(Objects::nonNull)
                .map(Role::getRole)
                .filter(Objects::nonNull)
                .distinct().collect(Collectors.toList());
        UserDto dto = UserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .phone(user.getPhone())
                .isAdmin(user.isAdmin())
                .permissions(perms)
                .build();
        return dto;
    }
}
