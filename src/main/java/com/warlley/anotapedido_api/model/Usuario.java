package com.warlley.anotapedido_api.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@RequiredArgsConstructor
@Entity(name = "usuarios")
public class Usuario {
    @Id
    private Long id;
    private String email;
    private String senha;
    private String nome;
    private String endereco;
}
