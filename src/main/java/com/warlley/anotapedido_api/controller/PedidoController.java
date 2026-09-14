package com.warlley.anotapedido_api.controller;

import com.warlley.anotapedido_api.dto.PedidoRequestDTO;
import com.warlley.anotapedido_api.dto.PedidoResponseDTO;
import com.warlley.anotapedido_api.service.PedidoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class PedidoController {

    private final PedidoService pedidoService;

    @GetMapping("/Pedidos/{idPedido}")
    public PedidoResponseDTO buscarPedido(@PathVariable Long idPedido){
        return pedidoService.buscarPedido(idPedido);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/Pedidos")
    public PedidoResponseDTO salvarPedido(@Valid @RequestBody PedidoRequestDTO pedidoRequestDTO){
        return pedidoService.salvarPedido(pedidoRequestDTO);
    }

    @PutMapping("/Pedidos/{idPedido}")
    public PedidoResponseDTO editarPedido(@Valid @RequestBody PedidoRequestDTO pedidoRequestDTO, @PathVariable Long idPedido){
        return pedidoService.editarPedido(pedidoRequestDTO, idPedido);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/Pedidos/{idPedido}")
    public void removePedido(@PathVariable Long idPedido){
        pedidoService.removePedido(idPedido);
    }
}
