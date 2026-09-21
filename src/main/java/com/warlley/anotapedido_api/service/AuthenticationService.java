package com.warlley.anotapedido_api.service;

import com.warlley.anotapedido_api.dto.LoginRequestDTO;
import com.warlley.anotapedido_api.dto.TokenResponseDTO;
import com.warlley.anotapedido_api.model.Usuario;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.Objects;

@RequiredArgsConstructor
@Service
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    public ResponseEntity<TokenResponseDTO> login(LoginRequestDTO loginRequestDTO){
        var usernamePassword = new UsernamePasswordAuthenticationToken(loginRequestDTO.email(), loginRequestDTO.senha());
        var auth = authenticationManager.authenticate(usernamePassword);
        var token = tokenService.gerarToken((Usuario) Objects.requireNonNull(auth.getPrincipal()));
        return ResponseEntity.ok(new TokenResponseDTO(token));
    }
}
