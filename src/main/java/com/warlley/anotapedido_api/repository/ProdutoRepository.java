package com.warlley.anotapedido_api.repository;

import com.warlley.anotapedido_api.model.Produto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdutoRepository extends JpaRepository<Produto,Long> {
      boolean existsByNomeFalse(String nome);

      boolean existsByNome(@Size(min = 2, message = "nome não pode ser menor que 2 letras.") @NotBlank(message = "campo nome não pode ser vazio.") String nome);
}
