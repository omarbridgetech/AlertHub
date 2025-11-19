package com.Alnsor.demo.config;

import com.Alnsor.demo.domain.entity.Role;
import com.Alnsor.demo.domain.entity.User;
import com.Alnsor.demo.domain.entity.UserRole;
import com.Alnsor.demo.domain.repository.RoleRepository;
import com.Alnsor.demo.domain.repository.UserRepository;
import com.Alnsor.demo.domain.repository.UserRoleRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;

@Configuration
public class DataSeeder {

    private static final List<String> DEFAULT_ROLES = Arrays.asList(
            "createAction", "updateAction", "deleteAction",
            "createMetric", "updateMetric", "deleteMetric",
            "triggerScan", "triggerProcess", "triggerEvaluation",
            "read"
    );

    @Bean
    CommandLineRunner seedData(RoleRepository roleRepository,
                               UserRepository userRepository,
                               UserRoleRepository userRoleRepository,
                               PasswordEncoder passwordEncoder,
                               @Value("${app.admin.username}") String adminUsername,
                               @Value("${app.admin.email}") String adminEmail,
                               @Value("${app.admin.phone:}") String adminPhone,
                               @Value("${app.admin.password}") String adminPassword) {
        return args -> {
            // Seed roles
            for (String rName : DEFAULT_ROLES) {
                roleRepository.findByRole(rName).orElseGet(() -> roleRepository.save(Role.builder().role(rName).build()));
            }
            // Seed admin
            User admin = userRepository.findByUsername(adminUsername).orElse(null);
            if (admin == null) {
                admin = User.builder()
                        .username(adminUsername)
                        .email(adminEmail)
                        .phone(adminPhone)
                        .password(passwordEncoder.encode(adminPassword))
                        .isAdmin(true)
                        .build();
                admin = userRepository.save(admin);
                // Assign all roles
                for (Role role : roleRepository.findAll()) {
                    UserRole ur = UserRole.builder().user(admin).role(role).build();
                    userRoleRepository.save(ur);
                }
            }
        };
    }
}
