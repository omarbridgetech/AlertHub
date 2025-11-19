package com.Alnsor.demo.service;

import com.Alnsor.demo.domain.entity.Role;
import com.Alnsor.demo.domain.entity.User;
import com.Alnsor.demo.domain.entity.UserRole;
import com.Alnsor.demo.domain.repository.RoleRepository;
import com.Alnsor.demo.domain.repository.UserRepository;
import com.Alnsor.demo.domain.repository.UserRoleRepository;
import com.Alnsor.demo.dto.CreateUserRequest;
import com.Alnsor.demo.dto.UserDto;
import com.Alnsor.demo.exception.BadRequestException;
import com.Alnsor.demo.exception.NotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, RoleRepository roleRepository, UserRoleRepository userRoleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userRoleRepository = userRoleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public UserDto createUser(CreateUserRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new BadRequestException("Username already exists");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email already exists");
        }
        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .phone(request.getPhone())
                .password(passwordEncoder.encode(request.getPassword()))
                .isAdmin(request.isAdmin())
                .build();
        User saved = userRepository.save(user);
        return mapToDto(saved);
    }

    @Transactional(readOnly = true)
    public List<UserDto> listUsers() {
        return userRepository.findAll().stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Transactional
    public void assignRole(Long userId, String roleName) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found"));
        Role role = roleRepository.findByRole(roleName).orElseThrow(() -> new NotFoundException("Role not found"));
        boolean exists = userRoleRepository.findByUserIdAndRoleId(userId, role.getId()).isPresent();
        if (!exists) {
            UserRole ur = UserRole.builder().user(user).role(role).build();
            userRoleRepository.save(ur);
        }
    }

    @Transactional
    public void removeRole(Long userId, Long roleId) {
        userRoleRepository.findByUserIdAndRoleId(userId, roleId)
                .ifPresentOrElse(userRoleRepository::delete, () -> { throw new NotFoundException("User role link not found");});
    }

    private UserDto mapToDto(User user) {
        List<String> perms = user.getUserRoles().stream()
                .map(UserRole::getRole)
                .filter(Objects::nonNull)
                .map(Role::getRole)
                .filter(Objects::nonNull)
                .distinct().collect(Collectors.toList());
        return UserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .phone(user.getPhone())
                .isAdmin(user.isAdmin())
                .permissions(perms)
                .build();
    }
}
