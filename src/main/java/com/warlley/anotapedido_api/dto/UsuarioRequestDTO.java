package com.warlley.anotapedido_api.dto;

import jakarta.validation.constraints.*;

public record UsuarioRequestDTO(
        @Email
        @NotBlank(message = "O campo email não pode está vazio.")
        String email,
        @Min(value = 8, message = "A senha tem que ter pelo menos 8 caracteres.")
        @Max(value = 8, message = "A senha tem que ter no maximo 8 caracteres.")
        @NotBlank(message = "O campo senha não pode está vazio.")
        String senha,
        @Min(value = 2, message = "O nome tem que ter pelo menos 2 caracteres.")
        @NotBlank(message = "O campo nome não pode está vazio.")
        String nome,
        @NotBlank(message = "O campo endereco não pode está vazio.")
        @Pattern(regexp = "^[^@]*$", message = "O endereço não pode conter o caractere '@'")
        String endereco

) {}
