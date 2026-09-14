package com.warlley.anotapedido_api.model;

import com.warlley.anotapedido_api.dto.UsuarioRequestDTO;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "usuarios")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String email;
    private String senha;
    private String nome;
    private String endereco;

    public Usuario(UsuarioRequestDTO usuarioRequestDto){
        this.email = usuarioRequestDto.email();
        this.nome  = usuarioRequestDto.nome();
        this.endereco = usuarioRequestDto.endereco();
        this.senha = usuarioRequestDto.senha();
    }
}
