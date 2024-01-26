package com.haya.autenticate_service.security.utils;

import com.haya.autenticate_service.model.dtos.ResponseValidate;
import com.haya.autenticate_service.model.entities.User;
import com.haya.autenticate_service.security.service.MyUserDetailsService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class JwtUtil {

    @Value("${token.palabra.secreta}")
    private String SECRETO;

    @Autowired
    private MyUserDetailsService myUserDetailsService;

    public Claims extraerContenidoClaims(String token){
       return Jwts.parser().setSigningKey(SECRETO).parseClaimsJws(token).getBody();
    }

    public String extraerUsername(String token){
        return extraerContenidoClaims(token).getSubject();
    }

    public Date extraerTiempoVencimiento(String token){
        return extraerContenidoClaims(token).getExpiration();
    }

    public boolean isTokenExpiration(String token){

        return extraerTiempoVencimiento(token).before(new Date());
    }

    public String prepararEstructuraToken(Map<String, Object> payload, String subject){
        return Jwts.builder()
                .setClaims(payload)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 *60))
                .signWith(SignatureAlgorithm.HS512, SECRETO)
                .compact();
    }

    public String creatToken(UserDetails userDetails){
        Map<String, Object> claims = new HashMap<>();
        return prepararEstructuraToken(claims, userDetails.getUsername());
    }


    public boolean validarToken(String token, UserDetails userDetails){

        String username = extraerUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpiration(token));
    }

    public ResponseValidate validarToken(String token){
        ResponseValidate response = new ResponseValidate();
        String username = extraerUsername(token);

        if(username != null) {
            Optional<User> user = myUserDetailsService.getByUsername(username);
            response.setUserId(user.get().getIdUser());
        }

        return response;
    }
}
