package com.delivery.delivery_app.service;

import com.delivery.delivery_app.dto.JwtResponse;
import com.delivery.delivery_app.dto.LoginRequest;
import com.delivery.delivery_app.dto.MessageResponse;
import com.delivery.delivery_app.dto.RegisterRequest;
import com.delivery.delivery_app.model.*;
import com.delivery.delivery_app.repository.UsuarioRepository;
import com.delivery.delivery_app.security.jwt.JwtUtils;
import com.delivery.delivery_app.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AuthService {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    PasswordEncoder encoder;

    public JwtResponse authenticateUser(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        return new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                roles);
    }

    public MessageResponse registerUser(RegisterRequest signUpRequest) {
        if (usuarioRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new RuntimeException("Error: El email ya está en uso!");
        }

        // Crear nuevo usuario según el rol
        Usuario user;
        String strRol = signUpRequest.getRol();
        String id = UUID.randomUUID().toString();
        String password = encoder.encode(signUpRequest.getPassword());

        if (strRol == null) {
            user = new Cliente(id, signUpRequest.getNombre(), signUpRequest.getEmail(), 
                               password, signUpRequest.getTelefono(), signUpRequest.getDireccion());
        } else {
            switch (strRol.toUpperCase()) {
                case "REPARTIDOR":
                    user = new Repartidor(id, signUpRequest.getNombre(), signUpRequest.getEmail(), 
                                          password, signUpRequest.getTelefono(), signUpRequest.getDireccion(), true, "Moto");
                    break;
                case "TIENDA":
                    user = new Tienda(id, signUpRequest.getNombre(), signUpRequest.getEmail(), 
                                      password, signUpRequest.getTelefono(), signUpRequest.getDireccion());
                    break;
                default:
                    user = new Cliente(id, signUpRequest.getNombre(), signUpRequest.getEmail(), 
                                       password, signUpRequest.getTelefono(), signUpRequest.getDireccion());
            }
        }

        usuarioRepository.save(user);

        return new MessageResponse("Usuario registrado exitosamente!");
    }
}
