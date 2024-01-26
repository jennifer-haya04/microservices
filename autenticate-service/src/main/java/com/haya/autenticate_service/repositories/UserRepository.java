package com.haya.autenticate_service.repositories;

import com.haya.autenticate_service.model.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    @Query("SELECT u FROM User u WHERE u.username = ?1")
    Optional<User> searchUserbyUsername(String username);

    @Query("SELECT u FROM User u WHERE u.correo = ?1")
    Optional<User> searchUserbyCorreo(String correo);

}
