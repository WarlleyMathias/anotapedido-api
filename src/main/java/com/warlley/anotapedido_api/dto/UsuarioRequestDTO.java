package com.warlley.anotapedido_api.dto;

import jakarta.validation.constraints.*;

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
        @NotBlank(message = "O campo endereco não pode está vazio.")
        @Pattern(regexp = "^[^@]*$", message = "O endereço não pode conter o caractere '@'")
        String endereco

) {}
