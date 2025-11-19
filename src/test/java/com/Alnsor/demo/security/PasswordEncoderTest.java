package com.Alnsor.demo.security;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

public class PasswordEncoderTest {

    @Test
    void bCryptEncoderHashesAndMatches() {
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        String raw = "MyStrongPass!";
        String hash = encoder.encode(raw);
        assertNotEquals(raw, hash);
        assertTrue(encoder.matches(raw, hash));
    }
}
