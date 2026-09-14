package com.warlley.anotapedido_api.repository;

import com.warlley.anotapedido_api.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PedidoRepository extends JpaRepository<Pedido,Long> {
}
