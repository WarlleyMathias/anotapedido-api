package com.warlley.anotapedido_api.model;

import com.warlley.anotapedido_api.dto.PedidoRequestDTO;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "pedidos")
public class Pedido {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;
    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL)
    private List<ItemPedido> itemPedidoList = new ArrayList<>();
    private Float total;

    public Pedido(PedidoRequestDTO pedidoRequestDTO){
        this.usuario = pedidoRequestDTO.usuario();
        this.itemPedidoList = pedidoRequestDTO.itemPedidoList();
        this.total = somaTotal();
    }

    public Float somaTotal(){
        Float total = 0f;
        for(ItemPedido itemPedido : itemPedidoList){
            total = itemPedido.getPrecoSubTotal();
        }
        return total;
    }
}
