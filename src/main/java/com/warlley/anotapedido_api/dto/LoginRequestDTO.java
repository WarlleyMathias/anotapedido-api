package com.warlley.anotapedido_api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record LoginRequestDTO(

    @Email
    @NotBlank(message = "O campo email não pode está vazio.")
    String email,
    @Min(value = 8, message = "A senha tem que ter pelo menos 8 caracteres.")
    @Max(value = 8, message = "A senha tem que ter no maximo 8 caracteres.")
    @NotBlank(message = "O campo senha não pode está vazio.")
    String senha

){}
