package com.warlley.anotapedido_api.service;

import com.warlley.anotapedido_api.model.ItemPedido;
import com.warlley.anotapedido_api.repository.ItemPedidoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@RequiredArgsConstructor
@Service
public class ItemPedidoSevice {

    private final ItemPedidoRepository itemPedidoRepository;

    public ItemPedido buscarItemPedido(Long idItemPedido){
        return (ItemPedido) itemPedidoRepository.findById(idItemPedido).
                orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"ItemPedido não encotrado."));
    }
}
