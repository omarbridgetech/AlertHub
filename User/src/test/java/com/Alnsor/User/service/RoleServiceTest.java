package com.Alnsor.User.service;

import com.Alnsor.User.dto.RoleDto;
import com.Alnsor.User.entity.RoleEntity;
import com.Alnsor.User.exception.RoleNotFoundException;
import com.Alnsor.User.mapper.RoleMapper;
import com.Alnsor.User.repository.RoleRepository;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoleServiceTest {

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private RoleMapper roleMapper;

    @InjectMocks
    private RoleService roleService;

    private RoleEntity testRole;
    private RoleDto testRoleDto;

    @BeforeEach
    void setUp() {
        testRole = new RoleEntity();
        testRole.setId(1);
        testRole.setRole("createAction");
        testRole.setUserRoles(new ArrayList<>());

        testRoleDto = new RoleDto();
        testRoleDto.setId(1);
        testRoleDto.setRole("createAction");
    }

    @Test
    void getAllRoles_ShouldReturnListOfRoles() {
        // Arrange
        List<RoleEntity> roles = Arrays.asList(testRole);
        when(roleRepository.findAll()).thenReturn(roles);
        when(roleMapper.toDto(testRole)).thenReturn(testRoleDto);

        // Act
        List<RoleDto> result = roleService.getAllRoles();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testRoleDto, result.get(0));
        verify(roleRepository, times(1)).findAll();
        verify(roleMapper, times(1)).toDto(testRole);
    }

    @Test
    void getRoleById_WhenRoleExists_ShouldReturnRole() {
        // Arrange
        when(roleRepository.findById(1)).thenReturn(Optional.of(testRole));
        when(roleMapper.toDto(testRole)).thenReturn(testRoleDto);

        // Act
        RoleDto result = roleService.getRoleById(1);

        // Assert
        assertNotNull(result);
        assertEquals(testRoleDto, result);
        verify(roleRepository, times(1)).findById(1);
        verify(roleMapper, times(1)).toDto(testRole);
    }

    @Test
    void getRoleById_WhenRoleNotFound_ShouldThrowException() {
        // Arrange
        when(roleRepository.findById(1)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RoleNotFoundException.class, () -> roleService.getRoleById(1));
        verify(roleRepository, times(1)).findById(1);
        verify(roleMapper, never()).toDto(any());
    }

    @Test
    void getRoleByName_WhenRoleExists_ShouldReturnRole() {
        // Arrange
        when(roleRepository.findByRole("createAction")).thenReturn(Optional.of(testRole));
        when(roleMapper.toDto(testRole)).thenReturn(testRoleDto);

        // Act
        RoleDto result = roleService.getRoleByName("createAction");

        // Assert
        assertNotNull(result);
        assertEquals(testRoleDto, result);
        verify(roleRepository, times(1)).findByRole("createAction");
        verify(roleMapper, times(1)).toDto(testRole);
    }

    @Test
    void getRoleByName_WhenRoleNotFound_ShouldThrowException() {
        // Arrange
        when(roleRepository.findByRole("createAction")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RoleNotFoundException.class, () -> roleService.getRoleByName("createAction"));
        verify(roleRepository, times(1)).findByRole("createAction");
        verify(roleMapper, never()).toDto(any());
    }
}
