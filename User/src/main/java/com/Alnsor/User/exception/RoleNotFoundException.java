package com.Alnsor.User.exception;

public class RoleNotFoundException extends RuntimeException {
    
    public RoleNotFoundException(String message) {
        super(message);
    }
    
    public RoleNotFoundException(Integer roleId) {
        super("Role not found with id: " + roleId);
    }
}
