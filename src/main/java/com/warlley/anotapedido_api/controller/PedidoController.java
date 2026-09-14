package com.warlley.anotapedido_api.controller;

import com.warlley.anotapedido_api.model.Pedido;
import com.warlley.anotapedido_api.service.PedidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class PedidoController {

    private final PedidoService pedidoService;

    public Pedido buscarPedido(Long idpedido){
        return pedidoService.buscarPedido(idpedido);
    }

    public Pedido salvarPedido(Pedido itemPedido){
        return pedidoService.salvarPedido(itemPedido);
    }

    public Pedido editarPedido(Pedido itemPedido){
        return pedidoService.editarPedido(itemPedido);
    }

    public void removePedido(Long idPedido){
        pedidoService.removePedido(idPedido);
    }
}
