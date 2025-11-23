package com.Alnsor.User.controller;

import com.Alnsor.User.dto.CreateUserRequest;
import com.Alnsor.User.dto.RoleAssignmentRequest;
import com.Alnsor.User.dto.UpdateUserRequest;
import com.Alnsor.User.dto.UserDto;
import com.Alnsor.User.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "User Management", description = "APIs for managing users and their roles")
public class UserController {

    private final UserService userService;

    @GetMapping
    @Operation(summary = "Get all users", description = "Returns a list of all users with their roles")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<UserDto> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get user by ID", description = "Returns a single user with their roles")
    public ResponseEntity<UserDto> getUserById(
            @Parameter(description = "User ID") @PathVariable Integer id) {
        UserDto user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @PostMapping
    @Operation(summary = "Create a new user", description = "Creates a new user and returns the created user")
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody CreateUserRequest request) {
        UserDto createdUser = userService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update user", description = "Updates an existing user's information")
    public ResponseEntity<UserDto> updateUser(
            @Parameter(description = "User ID") @PathVariable Integer id,
            @Valid @RequestBody UpdateUserRequest request) {
        UserDto updatedUser = userService.updateUser(id, request);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete user", description = "Deletes a user by ID")
    public ResponseEntity<Void> deleteUser(
            @Parameter(description = "User ID") @PathVariable Integer id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/roles/add")
    @Operation(summary = "Assign role to user", description = "Assigns a role to a user")
    public ResponseEntity<UserDto> assignRoleToUser(
            @Parameter(description = "User ID") @PathVariable Integer id,
            @Valid @RequestBody RoleAssignmentRequest request) {
        UserDto user = userService.assignRoleToUser(id, request.getRoleId());
        return ResponseEntity.ok(user);
    }

    @PostMapping("/{id}/roles/remove")
    @Operation(summary = "Revoke role from user", description = "Removes a role from a user")
    public ResponseEntity<UserDto> revokeRoleFromUser(
            @Parameter(description = "User ID") @PathVariable Integer id,
            @Valid @RequestBody RoleAssignmentRequest request) {
        UserDto user = userService.revokeRoleFromUser(id, request.getRoleId());
        return ResponseEntity.ok(user);
    }
}
