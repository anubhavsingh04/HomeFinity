package com.secure.homefinitybackend.repositories;

import com.secure.homefinitybackend.models.AppRole;
import com.secure.homefinitybackend.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByRoleName(AppRole roleName);
}
