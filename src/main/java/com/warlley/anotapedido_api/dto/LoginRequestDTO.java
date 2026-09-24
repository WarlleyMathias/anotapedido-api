package com.warlley.anotapedido_api.dto;

import jakarta.validation.constraints.*;

public record LoginRequestDTO(

    @Email
    @NotBlank(message = "O campo email não pode está vazio.")
    String email,
    @Size(min = 8, message = "A senha tem que ter no minimo 8 caracteres.")
    @NotBlank(message = "O campo senha não pode está vazio.")
    String senha

){}
