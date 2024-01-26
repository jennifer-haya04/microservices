package com.haya.autenticate_service.controllers;


import com.haya.autenticate_service.model.dtos.AutenticationRequest;
import com.haya.autenticate_service.model.dtos.AutenticationResponse;
import com.haya.autenticate_service.model.dtos.ResponseValidate;
import com.haya.autenticate_service.model.entities.Role;
import com.haya.autenticate_service.model.entities.User;
import com.haya.autenticate_service.model.dtos.AutenticateLogin;
import com.haya.autenticate_service.security.service.MyUserDetailsService;
import com.haya.autenticate_service.security.utils.JwtUtil;
import com.haya.autenticate_service.services.RoleService;
import com.haya.autenticate_service.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class HomeController {

    private RoleService roleService;
    private UserService userService;
    private AuthenticationManager authenticationManager;
    private MyUserDetailsService miUserDetailsService;
    private JwtUtil jwtUtil;
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public HomeController(RoleService roleService, UserService userService, AuthenticationManager authenticationManager,
                          MyUserDetailsService miUserDetailsService, JwtUtil jwtUtil, BCryptPasswordEncoder passwordEncoder) {
        this.roleService = roleService;
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.miUserDetailsService = miUserDetailsService;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/public")
    public String homePublic(){

        return "Pagina de inicio para la autenticacion";
    }

    @PostMapping("/registry")
    public ResponseEntity<?> registry(@RequestBody User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Asignar role de user
        Role role = roleService.searchRolebyID(3);
        user.agregarRoleALista(role);
        user.setActivo(true);
        userService.saveUser(user);

        return ResponseEntity.ok("Usuario registrado correctamente");
    }

    @PostMapping("/login")
    public ResponseEntity<?> validateUser(@RequestBody AutenticateLogin autLogin) throws Exception{
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(autLogin.getUsername(), autLogin.getPassword())
            );
        }catch (BadCredentialsException ex){
            throw new Exception("Error en el username o contraseña" + ex.getMessage());
        }

        final UserDetails userDetails = miUserDetailsService.loadUserByUsername(autLogin.getUsername());
        final String token = jwtUtil.creatToken(userDetails);

        return ResponseEntity.ok(new AutenticationResponse(token));
    }

    @PostMapping("/validate")
    public ResponseValidate validateToken(@RequestBody AutenticationRequest request) {
        return jwtUtil.validarToken(request.getToken());
    }

    @GetMapping("/home")
    public String userAuthenticated(){
        return "Welcome";
    }
}
