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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private UserRoleRepository userRoleRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    private UserEntity testUser;
    private UserDto testUserDto;
    private CreateUserRequest createUserRequest;
    private UpdateUserRequest updateUserRequest;
    private RoleEntity testRole;

    @BeforeEach
    void setUp() {
        testUser = new UserEntity();
        testUser.setId(1);
        testUser.setUsername("testuser");
        testUser.setEmail("test@example.com");
        testUser.setPhone("1234567890");
        testUser.setPassword("password123");
        testUser.setUserRoles(new ArrayList<>());

        testUserDto = new UserDto();
        testUserDto.setId(1);
        testUserDto.setUsername("testuser");
        testUserDto.setEmail("test@example.com");
        testUserDto.setPhone("1234567890");
        testUserDto.setRoles(new ArrayList<>());

        createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername("testuser");
        createUserRequest.setEmail("test@example.com");
        createUserRequest.setPhone("1234567890");
        createUserRequest.setPassword("password123");

        updateUserRequest = new UpdateUserRequest();
        updateUserRequest.setUsername("updateduser");
        updateUserRequest.setEmail("updated@example.com");

        testRole = new RoleEntity();
        testRole.setId(1);
        testRole.setRole("createAction");
        testRole.setUserRoles(new ArrayList<>());
    }

    @Test
    void getAllUsers_ShouldReturnListOfUsers() {
        // Arrange
        List<UserEntity> users = Arrays.asList(testUser);
        when(userRepository.findAll()).thenReturn(users);
        when(userMapper.toDto(testUser)).thenReturn(testUserDto);

        // Act
        List<UserDto> result = userService.getAllUsers();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testUserDto, result.get(0));
        verify(userRepository, times(1)).findAll();
        verify(userMapper, times(1)).toDto(testUser);
    }

    @Test
    void getUserById_WhenUserExists_ShouldReturnUser() {
        // Arrange
        when(userRepository.findById(1)).thenReturn(Optional.of(testUser));
        when(userMapper.toDto(testUser)).thenReturn(testUserDto);

        // Act
        UserDto result = userService.getUserById(1);

        // Assert
        assertNotNull(result);
        assertEquals(testUserDto, result);
        verify(userRepository, times(1)).findById(1);
        verify(userMapper, times(1)).toDto(testUser);
    }

    @Test
    void getUserById_WhenUserNotFound_ShouldThrowException() {
        // Arrange
        when(userRepository.findById(1)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UserNotFoundException.class, () -> userService.getUserById(1));
        verify(userRepository, times(1)).findById(1);
        verify(userMapper, never()).toDto(any());
    }

    @Test
    void createUser_WhenValidRequest_ShouldCreateUser() {
        // Arrange
        when(userRepository.existsByUsername(createUserRequest.getUsername())).thenReturn(false);
        when(userRepository.existsByEmail(createUserRequest.getEmail())).thenReturn(false);
        when(userRepository.save(any(UserEntity.class))).thenReturn(testUser);
        when(userMapper.toDto(testUser)).thenReturn(testUserDto);

        // Act
        UserDto result = userService.createUser(createUserRequest);

        // Assert
        assertNotNull(result);
        assertEquals(testUserDto, result);
        verify(userRepository, times(1)).existsByUsername(createUserRequest.getUsername());
        verify(userRepository, times(1)).existsByEmail(createUserRequest.getEmail());
        verify(userRepository, times(1)).save(any(UserEntity.class));
        verify(userMapper, times(1)).toDto(testUser);
    }

    @Test
    void createUser_WhenDuplicateUsername_ShouldThrowException() {
        // Arrange
        when(userRepository.existsByUsername(createUserRequest.getUsername())).thenReturn(true);

        // Act & Assert
        assertThrows(DuplicateUserException.class, () -> userService.createUser(createUserRequest));
        verify(userRepository, times(1)).existsByUsername(createUserRequest.getUsername());
        verify(userRepository, never()).save(any());
    }

    @Test
    void createUser_WhenDuplicateEmail_ShouldThrowException() {
        // Arrange
        when(userRepository.existsByUsername(createUserRequest.getUsername())).thenReturn(false);
        when(userRepository.existsByEmail(createUserRequest.getEmail())).thenReturn(true);

        // Act & Assert
        assertThrows(DuplicateUserException.class, () -> userService.createUser(createUserRequest));
        verify(userRepository, times(1)).existsByEmail(createUserRequest.getEmail());
        verify(userRepository, never()).save(any());
    }

    @Test
    void updateUser_WhenValidRequest_ShouldUpdateUser() {
        // Arrange
        when(userRepository.findById(1)).thenReturn(Optional.of(testUser));
        when(userRepository.existsByUsername(updateUserRequest.getUsername())).thenReturn(false);
        when(userRepository.existsByEmail(updateUserRequest.getEmail())).thenReturn(false);
        when(userRepository.save(testUser)).thenReturn(testUser);
        when(userMapper.toDto(testUser)).thenReturn(testUserDto);

        // Act
        UserDto result = userService.updateUser(1, updateUserRequest);

        // Assert
        assertNotNull(result);
        assertEquals(testUserDto, result);
        verify(userRepository, times(1)).findById(1);
        verify(userRepository, times(1)).save(testUser);
        verify(userMapper, times(1)).toDto(testUser);
    }

    @Test
    void updateUser_WhenUserNotFound_ShouldThrowException() {
        // Arrange
        when(userRepository.findById(1)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UserNotFoundException.class, () -> userService.updateUser(1, updateUserRequest));
        verify(userRepository, times(1)).findById(1);
        verify(userRepository, never()).save(any());
    }

    @Test
    void deleteUser_WhenUserExists_ShouldDeleteUser() {
        // Arrange
        when(userRepository.existsById(1)).thenReturn(true);
        doNothing().when(userRepository).deleteById(1);

        // Act
        userService.deleteUser(1);

        // Assert
        verify(userRepository, times(1)).existsById(1);
        verify(userRepository, times(1)).deleteById(1);
    }

    @Test
    void deleteUser_WhenUserNotFound_ShouldThrowException() {
        // Arrange
        when(userRepository.existsById(1)).thenReturn(false);

        // Act & Assert
        assertThrows(UserNotFoundException.class, () -> userService.deleteUser(1));
        verify(userRepository, times(1)).existsById(1);
        verify(userRepository, never()).deleteById(any());
    }

    @Test
    void assignRoleToUser_WhenValidRequest_ShouldAssignRole() {
        // Arrange
        when(userRepository.findById(1)).thenReturn(Optional.of(testUser));
        when(roleRepository.findById(1)).thenReturn(Optional.of(testRole));
        when(userRepository.save(testUser)).thenReturn(testUser);
        when(userMapper.toDto(testUser)).thenReturn(testUserDto);

        // Act
        UserDto result = userService.assignRoleToUser(1, 1);

        // Assert
        assertNotNull(result);
        assertEquals(testUserDto, result);
        verify(userRepository, times(1)).findById(1);
        verify(roleRepository, times(1)).findById(1);
        verify(userRepository, times(1)).save(testUser);
        verify(userMapper, times(1)).toDto(testUser);
    }

    @Test
    void assignRoleToUser_WhenUserNotFound_ShouldThrowException() {
        // Arrange
        when(userRepository.findById(1)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UserNotFoundException.class, () -> userService.assignRoleToUser(1, 1));
        verify(userRepository, times(1)).findById(1);
        verify(roleRepository, never()).findById(any());
    }

    @Test
    void assignRoleToUser_WhenRoleNotFound_ShouldThrowException() {
        // Arrange
        when(userRepository.findById(1)).thenReturn(Optional.of(testUser));
        when(roleRepository.findById(1)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RoleNotFoundException.class, () -> userService.assignRoleToUser(1, 1));
        verify(userRepository, times(1)).findById(1);
        verify(roleRepository, times(1)).findById(1);
        verify(userRepository, never()).save(any());
    }

    @Test
    void revokeRoleFromUser_WhenValidRequest_ShouldRevokeRole() {
        // Arrange
        UserRoleEntity userRole = new UserRoleEntity();
        userRole.setId(1);
        userRole.setUser(testUser);
        userRole.setRole(testRole);
        testUser.getUserRoles().add(userRole);

        when(userRepository.findById(1)).thenReturn(Optional.of(testUser));
        when(roleRepository.existsById(1)).thenReturn(true);
        doNothing().when(userRoleRepository).delete(userRole);
        when(userMapper.toDto(testUser)).thenReturn(testUserDto);

        // Act
        UserDto result = userService.revokeRoleFromUser(1, 1);

        // Assert
        assertNotNull(result);
        assertEquals(testUserDto, result);
        verify(userRepository, times(1)).findById(1);
        verify(roleRepository, times(1)).existsById(1);
        verify(userRoleRepository, times(1)).delete(userRole);
        verify(userMapper, times(1)).toDto(testUser);
    }

    @Test
    void revokeRoleFromUser_WhenUserNotFound_ShouldThrowException() {
        // Arrange
        when(userRepository.findById(1)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UserNotFoundException.class, () -> userService.revokeRoleFromUser(1, 1));
        verify(userRepository, times(1)).findById(1);
        verify(roleRepository, never()).existsById(any());
    }

    @Test
    void getUserByUsername_WhenUserExists_ShouldReturnUser() {
        // Arrange
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        when(userMapper.toDto(testUser)).thenReturn(testUserDto);

        // Act
        UserDto result = userService.getUserByUsername("testuser");

        // Assert
        assertNotNull(result);
        assertEquals(testUserDto, result);
        verify(userRepository, times(1)).findByUsername("testuser");
        verify(userMapper, times(1)).toDto(testUser);
    }

    @Test
    void getUserByUsername_WhenUserNotFound_ShouldThrowException() {
        // Arrange
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UserNotFoundException.class, () -> userService.getUserByUsername("testuser"));
        verify(userRepository, times(1)).findByUsername("testuser");
        verify(userMapper, never()).toDto(any());
    }
}
