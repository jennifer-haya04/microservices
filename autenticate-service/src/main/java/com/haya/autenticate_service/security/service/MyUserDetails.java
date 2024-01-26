package com.haya.autenticate_service.security.service;

import com.haya.autenticate_service.model.entities.Role;
import com.haya.autenticate_service.model.entities.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class MyUserDetails implements UserDetails {

    private String username;
    private String password;
    private boolean active;
    private List<SimpleGrantedAuthority> authorities;


    public MyUserDetails(User user){
        this.authorities = new ArrayList<SimpleGrantedAuthority>();
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.active = user.isActivo();
        for(Role role : user.getRoles()){
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getRoleName()));
        }

    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.active;
    }
}
