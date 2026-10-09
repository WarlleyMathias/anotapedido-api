package com.warlley.anotapedido_api.repository;

import com.warlley.anotapedido_api.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
}
