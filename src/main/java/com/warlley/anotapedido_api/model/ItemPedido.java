package com.warlley.anotapedido_api.model;

import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "itemPedidos")
public class ItemPedido {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "produto_id")
    private Produto produto;
    private int quantidade;
    private Float precoUnitario;
    private Float precoSubTotal;
    @ManyToOne
    @JoinColumn(name = "pedido_id")
    private Pedido pedido;

    public ItemPedido(Produto produto, int quantidade){
        this.produto       = produto;
        this.quantidade    = quantidade;
        this.precoUnitario = produto.getValor();
        this.precoSubTotal = precoUnitario*quantidade;
    }
}
