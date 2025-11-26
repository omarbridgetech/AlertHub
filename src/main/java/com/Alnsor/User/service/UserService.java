package com.Alnsor.User.service;

import com.Alnsor.User.dto.CreateUserRequest;
import com.Alnsor.User.dto.UpdateUserRequest;
import com.Alnsor.User.dto.UserDto;
import com.Alnsor.User.entity.RoleEntity;
import com.Alnsor.User.entity.UserEntity;
import com.Alnsor.User.entity.UserRoleEntity;
import com.Alnsor.User.exception.DuplicateUserException;
import com.Alnsor.User.exception.RoleNotFoundException;
import com.Alnsor.User.exception.UserNotFoundException;
import com.Alnsor.User.mapper.UserMapper;
import com.Alnsor.User.repository.RoleRepository;
import com.Alnsor.User.repository.UserRepository;
import com.Alnsor.User.repository.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;
    private final UserMapper userMapper;

    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }

    public UserDto getUserById(Integer id) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        return userMapper.toDto(user);
    }

    public UserDto createUser(CreateUserRequest request) {
        // Check for duplicate username
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new DuplicateUserException("Username already exists: " + request.getUsername());
        }

        // Check for duplicate email
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateUserException("Email already exists: " + request.getEmail());
        }

        UserEntity user = new UserEntity();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setPassword(request.getPassword());

        UserEntity savedUser = userRepository.save(user);
        return userMapper.toDto(savedUser);
    }

    public UserDto updateUser(Integer id, UpdateUserRequest request) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        // Check for duplicate username if it's being changed
        if (request.getUsername() != null && !request.getUsername().equals(user.getUsername())) {
            if (userRepository.existsByUsername(request.getUsername())) {
                throw new DuplicateUserException("Username already exists: " + request.getUsername());
            }
            user.setUsername(request.getUsername());
        }

        // Check for duplicate email if it's being changed
        if (request.getEmail() != null && !request.getEmail().equals(user.getEmail())) {
            if (userRepository.existsByEmail(request.getEmail())) {
                throw new DuplicateUserException("Email already exists: " + request.getEmail());
            }
            user.setEmail(request.getEmail());
        }

        if (request.getPhone() != null) {
            user.setPhone(request.getPhone());
        }

        if (request.getPassword() != null) {
            user.setPassword(request.getPassword());
        }

        UserEntity updatedUser = userRepository.save(user);
        return userMapper.toDto(updatedUser);
    }

    public void deleteUser(Integer id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException(id);
        }
        userRepository.deleteById(id);
    }

    public UserDto assignRoleToUser(Integer userId, Integer roleId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        RoleEntity role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RoleNotFoundException(roleId));

        // Check if user already has this role
        boolean alreadyHasRole = user.getUserRoles().stream()
                .anyMatch(ur -> ur.getRole().getId().equals(roleId));

        if (!alreadyHasRole) {
            UserRoleEntity userRole = new UserRoleEntity();
            userRole.setUser(user);
            userRole.setRole(role);
            user.getUserRoles().add(userRole);
            userRepository.save(user);
        }

        return userMapper.toDto(user);
    }

    public UserDto revokeRoleFromUser(Integer userId, Integer roleId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        if (!roleRepository.existsById(roleId)) {
            throw new RoleNotFoundException(roleId);
        }

        UserRoleEntity userRoleToRemove = user.getUserRoles().stream()
                .filter(ur -> ur.getRole().getId().equals(roleId))
                .findFirst()
                .orElse(null);

        if (userRoleToRemove != null) {
            user.getUserRoles().remove(userRoleToRemove);
            userRoleRepository.delete(userRoleToRemove);
        }

        return userMapper.toDto(user);
    }

    public UserDto getUserByUsername(String username) {
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found with username: " + username));
        return userMapper.toDto(user);
    }

    public UserDto getUserByEmail(String email) {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));
        return userMapper.toDto(user);
    }
}
