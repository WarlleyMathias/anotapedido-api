package com.warlley.anotapedido_api.repository;

import com.warlley.anotapedido_api.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdutoRepository extends JpaRepository<Produto,Long> {
      boolean existsByNomeFalse(String nome);
}
