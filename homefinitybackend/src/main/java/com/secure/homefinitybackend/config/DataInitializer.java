package com.secure.homefinitybackend.config;

import com.secure.homefinitybackend.models.AppRole;
import com.secure.homefinitybackend.models.Role;
import com.secure.homefinitybackend.repositories.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;

    @Override
    public void run(String... args) {
        for (AppRole appRole : AppRole.values()) {
            if (roleRepository.findByRoleName(appRole).isEmpty()) {
                roleRepository.save(new Role(appRole));
                log.info("Created role: {}", appRole.name());
            }
        }
        log.info("Role initialization complete");
    }
}
