package com.warlley.anotapedido_api.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@RequiredArgsConstructor
@Entity(name = "pedidos")
public class Pedido {
    @Id
    private Long id;
    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;
    @OneToMany(mappedBy = "pedidos", cascade = CascadeType.ALL)
    private List<ItemPedido> itemPedidoList = new ArrayList<>();
}
