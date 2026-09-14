package com.warlley.anotapedido_api.controller;

import com.warlley.anotapedido_api.dto.ItemPedidoRequestDTO;
import com.warlley.anotapedido_api.dto.ItemPedidoResponseDTO;
import com.warlley.anotapedido_api.service.ItemPedidoSevice;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class ItemPedidoController {

    private final ItemPedidoSevice itemPedidoSevice;

    @GetMapping("/ItemPedidos/{idItemPedido}")
    public ItemPedidoResponseDTO buscarItemPedido(@PathVariable Long idItemPedido){
        return itemPedidoSevice.buscarItemPedido(idItemPedido);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/ItemPedidos")
    public ItemPedidoResponseDTO salvarItemPedido(@Valid @RequestBody ItemPedidoRequestDTO itemPedidoRequestDTO){
        return itemPedidoSevice.salvarItemPedido(itemPedidoRequestDTO);
    }

    @PutMapping("/ItemPedidos/{idItemPedido}")
    public ItemPedidoResponseDTO editarItemPedido(@Valid @RequestBody ItemPedidoRequestDTO itemPedidoRequestDTO, @PathVariable Long idItemPedido){
        return itemPedidoSevice.editarItemPedido(itemPedidoRequestDTO, idItemPedido);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/ItemPedidos/{idItemPedido}")
    public void removeItemPedido(@PathVariable Long idItemPedido){
        itemPedidoSevice.removeItemPedido(idItemPedido);
    }
}
