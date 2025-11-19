package com.Alnsor.demo.controller;

import com.Alnsor.demo.dto.AssignRoleRequest;
import com.Alnsor.demo.dto.CreateUserRequest;
import com.Alnsor.demo.dto.UserDto;
import com.Alnsor.demo.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody CreateUserRequest request) {
        return ResponseEntity.ok(userService.createUser(request));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserDto>> listUsers() {
        return ResponseEntity.ok(userService.listUsers());
    }

    @PostMapping("/{id}/roles")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> assignRole(@PathVariable Long id, @Valid @RequestBody AssignRoleRequest request) {
        userService.assignRole(id, request.getRole());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}/roles/{roleId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> removeRole(@PathVariable Long id, @PathVariable Long roleId) {
        userService.removeRole(id, roleId);
        return ResponseEntity.noContent().build();
    }
}
