package com.haya.autenticate_service.security.service;

import com.haya.autenticate_service.model.entities.User;
import com.haya.autenticate_service.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@AllArgsConstructor
@Service
public class MyUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    public Optional<User> getByUsername(String username) throws UsernameNotFoundException {
        return userRepository.searchUserbyUsername(username);
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> usuario = userRepository.searchUserbyUsername(username);
        usuario.orElseThrow(() -> new UsernameNotFoundException("No se encontro el usuario "+ username
                +" en la BD"));

        return usuario.map(MyUserDetails::new).get();

    }
}
