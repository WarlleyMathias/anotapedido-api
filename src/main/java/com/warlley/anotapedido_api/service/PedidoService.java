package com.warlley.anotapedido_api.service;

import com.warlley.anotapedido_api.dto.PedidoRequestDTO;
import com.warlley.anotapedido_api.dto.PedidoResponseDTO;
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

    public PedidoResponseDTO buscarPedido(Long idPedido){
        return new PedidoResponseDTO(pedidoRepository.findById(idPedido).
                orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Pedido não encotrado.")));
    }
    public PedidoResponseDTO salvarPedido(PedidoRequestDTO pedidoRequestDTO){
        Pedido pedidoNovo = new Pedido(pedidoRequestDTO);
        return new PedidoResponseDTO(pedidoRepository.save(pedidoNovo));
    }
    public PedidoResponseDTO editarPedido(PedidoRequestDTO pedidoRequestDTO, Long idPedido){
        Pedido pedidoNovo = new Pedido(pedidoRequestDTO);
        if(pedidoRepository.existsById(idPedido)){
            return new PedidoResponseDTO(pedidoRepository.save(pedidoNovo));
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
