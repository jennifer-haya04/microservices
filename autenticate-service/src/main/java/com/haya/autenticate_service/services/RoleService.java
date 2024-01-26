package com.haya.autenticate_service.services;

import com.haya.autenticate_service.model.entities.Role;
import com.haya.autenticate_service.model.entities.User;
import com.haya.autenticate_service.repositories.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;

    public List<Role> getAllRole(){
        return roleRepository.findAll();
    }

    public Role searchRolebyID(Integer idRole){
        return roleRepository.findById(idRole).orElse(null);
    }

}
