package com.warlley.anotapedido_api.model;


import com.warlley.anotapedido_api.dto.ItemPedidoRequestDTO;
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

    public ItemPedido(Produto produto, ItemPedidoRequestDTO itemPedidoRequestDTO){
        this.produto       = produto;
        this.quantidade    = itemPedidoRequestDTO.quantidade();
        this.precoUnitario = produto.getValor();
        this.precoSubTotal = precoUnitario*quantidade;
    }
}
