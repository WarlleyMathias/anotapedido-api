package com.warlley.anotapedido_api.service.authenticatonService;

import com.warlley.anotapedido_api.dto.LoginRequestDTO;
import com.warlley.anotapedido_api.dto.TokenResponseDTO;
import com.warlley.anotapedido_api.model.Usuario;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthenticationService {
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    public TokenResponseDTO login(LoginRequestDTO dto) {
        // 1. Autentica as credenciais (email e senha)
        var authToken = new UsernamePasswordAuthenticationToken(dto.email(), dto.senha());
        var authentication = authenticationManager.authenticate(authToken);

        // 2. Obtém o usuário autenticado do contexto
        Usuario usuario = (Usuario) authentication.getPrincipal();

        // 3. Gera o token JWT
        assert usuario != null;
        String token = tokenService.gerarToken(usuario);

        // 4. Retorna o DTO com o token e os dados básicos
        return new TokenResponseDTO(
                token,
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getRole().name() // Ex: "ROLE_USER" ou "ROLE_ADMIN"
        );
    }
}
