package com.warlley.anotapedido_api.controller;

import com.warlley.anotapedido_api.dto.LoginRequestDTO;
import com.warlley.anotapedido_api.dto.TokenResponseDTO;
import com.warlley.anotapedido_api.service.authenticatonService.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public TokenResponseDTO login(@RequestBody @Valid LoginRequestDTO loginRequestDTO) {
        return authenticationService.login(loginRequestDTO);
    }
}