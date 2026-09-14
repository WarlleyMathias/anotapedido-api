package com.warlley.anotapedido_api.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@RequiredArgsConstructor
@Entity(name = "produtos")
public class Produto {
    @Id
    private Long id;
    private String nome;
    private Float valor;
}
