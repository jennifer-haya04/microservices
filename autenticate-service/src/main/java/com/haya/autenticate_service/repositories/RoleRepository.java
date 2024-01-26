package com.haya.autenticate_service.repositories;

import com.haya.autenticate_service.model.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Integer> {
}
