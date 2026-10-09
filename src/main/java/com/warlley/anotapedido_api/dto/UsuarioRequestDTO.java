package com.warlley.anotapedido_api.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.util.List;

public record UsuarioRequestDTO(
        @Email
        @NotBlank(message = "O campo email não pode está vazio.")
        String email,
        @Size(min = 8, message = "A senha deve ter no mínimo 8")
        @NotBlank(message = "O campo senha não pode está vazio.")
        String senha,
        @Size(min = 2, message = "A nome deve ter no mínimo 2")
        @NotBlank(message = "O campo nome não pode está vazio.")
        String nome,
        @Valid
        List<EnderecoRequestDTO> enderecos

) {}
