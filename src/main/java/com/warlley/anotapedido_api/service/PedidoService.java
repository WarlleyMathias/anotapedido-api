package com.warlley.anotapedido_api.service;

import com.warlley.anotapedido_api.dto.ItemPedidoRequestDTO;
import com.warlley.anotapedido_api.dto.PedidoRequestDTO;
import com.warlley.anotapedido_api.dto.PedidoResponseDTO;
import com.warlley.anotapedido_api.model.ItemPedido;
import com.warlley.anotapedido_api.model.Pedido;
import com.warlley.anotapedido_api.model.Produto;
import com.warlley.anotapedido_api.repository.PedidoRepository;
import com.warlley.anotapedido_api.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;

    private final UsuarioRepository usuarioRepository;

    private final ProdutoSevice produtoSevice;

    public PedidoResponseDTO buscarPedido(Long idPedido){
        return new PedidoResponseDTO(pedidoRepository.findById(idPedido).
                orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Pedido não encotrado.")));
    }
    @Transactional
    public PedidoResponseDTO salvarPedido(PedidoRequestDTO pedidoRequestDTO){
        Pedido pedidoNovo = montarPedido(pedidoRequestDTO);
        return new PedidoResponseDTO(pedidoRepository.save(pedidoNovo));
    }
    @Transactional
    public PedidoResponseDTO editarPedido(PedidoRequestDTO pedidoRequestDTO, Long idPedido){
        Pedido pedidoNovo = montarPedido(pedidoRequestDTO);
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

    private Pedido montarPedido(PedidoRequestDTO pedidoRequestDTO){
        Pedido pedidoNovo = new Pedido();
        List<ItemPedido> itemPedidoList = new ArrayList<>();
        for(ItemPedidoRequestDTO itemPedidoRequestDTO : pedidoRequestDTO.itens()){
            Produto produto = new Produto(produtoSevice.buscarProduto(itemPedidoRequestDTO.produtoId()));
            itemPedidoList.add(new ItemPedido(produto,itemPedidoRequestDTO));
        }
        pedidoNovo.setItemPedidoList(itemPedidoList);
        pedidoNovo.setUsuario(usuarioRepository.findById(pedidoRequestDTO.usuarioId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Usuário não encontrado.")));
        return pedidoNovo;
    }
}
