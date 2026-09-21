package com.warlley.anotapedido_api.model;

import com.warlley.anotapedido_api.dto.UsuarioRequestDTO;
import com.warlley.anotapedido_api.model.enums.UserRole;
import jakarta.persistence.*;
import lombok.*;
import org.jspecify.annotations.NullMarked;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "usuarios")
public class Usuario implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(unique = true, nullable = false)
    private String email;
    private String senha;
    private String nome;
    private String endereco;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role; // Define se é ADMIN ou USER

    public Usuario(UsuarioRequestDTO usuarioRequestDTO){
        this.email = usuarioRequestDTO.email();
        this.nome  = usuarioRequestDTO.nome();
        this.endereco = usuarioRequestDTO.endereco();
        this.senha = usuarioRequestDTO.senha();
    }

    @Override
    public @NullMarked Collection<? extends GrantedAuthority> getAuthorities() {
        if (this.role == UserRole.ADMIN) {
            // O ADMIN herda as permissões de ADMIN e USER
            return List.of(
                    new SimpleGrantedAuthority("ROLE_ADMIN"),
                    new SimpleGrantedAuthority("ROLE_USER")
            );
        }
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override public String getPassword() { return this.senha; }
    @Override public @NullMarked String getUsername() { return this.email; }
}
