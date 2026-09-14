package com.warlley.anotapedido_api.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@RequiredArgsConstructor
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

    public ItemPedido(Produto produto, int quantidade){
        this.produto       = produto;
        this.quantidade    = quantidade;
        this.precoUnitario = produto.getValor();
        this.precoSubTotal = precoUnitario*quantidade;
    }
}
