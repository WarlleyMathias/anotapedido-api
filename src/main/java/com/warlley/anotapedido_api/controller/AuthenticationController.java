package com.warlley.anotapedido_api.controller;

import com.warlley.anotapedido_api.dto.LoginRequestDTO;
import com.warlley.anotapedido_api.dto.TokenResponseDTO;
import com.warlley.anotapedido_api.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
public class AuthenticationController {

    AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<TokenResponseDTO> login(@RequestBody @Valid LoginRequestDTO loginRequestDTO) {
        return authenticationService.login(loginRequestDTO);
    }
}