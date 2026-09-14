package com.warlley.anotapedido_api.service;

import com.warlley.anotapedido_api.model.Pedido;
import com.warlley.anotapedido_api.repository.PedidoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@RequiredArgsConstructor
@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;

    public Pedido buscarPedido(Long idPedido){
        return pedidoRepository.findById(idPedido).
                orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Pedido não encotrado."));
    }
    public Pedido salvarPedido(Pedido pedido){
        return pedidoRepository.save(pedido);
    }
    public Pedido editarPedido(Pedido pedido){
        if(pedidoRepository.existsById(pedido.getId())){
            return pedidoRepository.save(pedido);
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Pedido não encontrado, para ser editado.");
    }

    public void removePedido(Long idPedido){
        if (pedidoRepository.existsById(idPedido)){
            pedidoRepository.deleteById(idPedido);
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Pedido não encontrado, para ser deletado");
    }
}
