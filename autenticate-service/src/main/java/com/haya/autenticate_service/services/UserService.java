package com.haya.autenticate_service.services;

import com.haya.autenticate_service.model.entities.User;
import com.haya.autenticate_service.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public List<User> searchAllUsers(){
        return userRepository.findAll();
    }

    public User searchUserbyId(Integer idUser){
        return userRepository.findById(idUser).orElse(null);
    }

    public User searchUserbyUsername(String username){
        return userRepository.searchUserbyUsername(username).orElse(null);
    }

    public User searchUserbyEmail(String email){
        return userRepository.searchUserbyCorreo(email).orElse(null);
    }

    public void saveUser(User user){
        userRepository.save(user);
    }

    public void deleteUser(Integer idUser){
        userRepository.deleteById(idUser);
    }
}
