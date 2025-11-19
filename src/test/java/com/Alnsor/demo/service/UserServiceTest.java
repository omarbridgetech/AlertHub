package com.Alnsor.demo.service;

import com.Alnsor.demo.domain.entity.Role;
import com.Alnsor.demo.domain.entity.User;
import com.Alnsor.demo.domain.repository.RoleRepository;
import com.Alnsor.demo.domain.repository.UserRepository;
import com.Alnsor.demo.domain.repository.UserRoleRepository;
import com.Alnsor.demo.dto.CreateUserRequest;
import com.Alnsor.demo.dto.UserDto;
import com.Alnsor.demo.exception.BadRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private UserRoleRepository userRoleRepository;
    private PasswordEncoder passwordEncoder;
    private UserService userService;

    @BeforeEach
    void setup() {
        userRepository = mock(UserRepository.class);
        roleRepository = mock(RoleRepository.class);
        userRoleRepository = mock(UserRoleRepository.class);
        passwordEncoder = new BCryptPasswordEncoder();
        userService = new UserService(userRepository, roleRepository, userRoleRepository, passwordEncoder);
    }

    @Test
    void createUser_success() {
        CreateUserRequest req = new CreateUserRequest();
        req.setUsername("bob");
        req.setEmail("bob@example.com");
        req.setPassword("Password123");
        when(userRepository.existsByUsername("bob")).thenReturn(false);
        when(userRepository.existsByEmail("bob@example.com")).thenReturn(false);
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User u = invocation.getArgument(0);
            u.setId(10L);
            return u;
        });
        UserDto dto = userService.createUser(req);
        assertNotNull(dto.getId());
        assertEquals("bob", dto.getUsername());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void createUser_duplicateUsernameThrows() {
        CreateUserRequest req = new CreateUserRequest();
        req.setUsername("bob");
        req.setEmail("bob@example.com");
        req.setPassword("Password123");
        when(userRepository.existsByUsername("bob")).thenReturn(true);
        assertThrows(BadRequestException.class, () -> userService.createUser(req));
    }
}
