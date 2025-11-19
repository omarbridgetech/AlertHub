package com.Alnsor.demo.security;

import com.Alnsor.demo.domain.entity.Role;
import com.Alnsor.demo.domain.entity.User;
import com.Alnsor.demo.domain.entity.UserRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class JwtServiceTest {

    @Test
    void generateAndParseToken_hasRequiredClaims() {
        JwtService jwt = new JwtService("testsecret-testsecret-testsecret-1234567890", 3600000);
        User user = User.builder()
                .id(1L)
                .username("alice")
                .isAdmin(true)
                .build();
        user.getUserRoles().add(UserRole.builder().user(user).role(Role.builder().role("read").build()).build());
        String token = jwt.generateToken(user);
        assertNotNull(token);
        assertTrue(jwt.validateToken(token));
        Jws<Claims> parsed = jwt.parseToken(token);
        assertEquals(1, parsed.getBody().get("user_id", Integer.class));
        assertEquals("alice", parsed.getBody().get("username", String.class));
        assertTrue(parsed.getBody().get("is_admin", Boolean.class));
        List<?> perms = parsed.getBody().get("permissions", List.class);
        assertNotNull(perms);
        assertTrue(perms.contains("read"));
    }
}
