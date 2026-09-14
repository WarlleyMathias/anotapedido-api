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
        return itemPedidoRepository.findById(idItemPedido).
                orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"ItemPedido não encotrado."));
    }
    public ItemPedido salvarItemPedido(ItemPedido itemPedido){
        return itemPedidoRepository.save(itemPedido);
    }
    public ItemPedido editarItemPedido(ItemPedido itemPedido){
        if(itemPedidoRepository.existsById(itemPedido.getId())){
            return itemPedidoRepository.save(itemPedido);
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "ItemPedido não encontrado, para ser editado.");
    }

    public void removeItemPedido(Long idItemPedido){
        if (itemPedidoRepository.existsById(idItemPedido)){
            itemPedidoRepository.deleteById(idItemPedido);
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND,"ItemPedido não encontrado, para ser deletado");
    }
}
