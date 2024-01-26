package com.haya.autenticate_service.model.entities;


import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idUser;

    private String nombre;
    private String apellido;
    private int edad;
    private String dni;
    private String correo;
    @NonNull
    private String username;
    @NonNull
    private String password;
    private boolean activo;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinTable(
            name = "rolByUser",
            joinColumns = {@JoinColumn(name = "idUser")},
            inverseJoinColumns = {@JoinColumn(name = "idRole")}
    )
    private List<Role> roles;

    public User(){
        roles = new ArrayList<Role>();
    }

    public void agregarRoleALista(Role role){
        this.roles.add(role);
    }
}
