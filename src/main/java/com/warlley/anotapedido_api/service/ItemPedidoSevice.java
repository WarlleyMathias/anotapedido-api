package com.warlley.anotapedido_api.service;

import com.warlley.anotapedido_api.dto.ItemPedidoRequestDTO;
import com.warlley.anotapedido_api.dto.ItemPedidoResponseDTO;
import com.warlley.anotapedido_api.model.ItemPedido;
import com.warlley.anotapedido_api.model.Produto;
import com.warlley.anotapedido_api.repository.ItemPedidoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@RequiredArgsConstructor
@Service
public class ItemPedidoSevice {

    private final ItemPedidoRepository itemPedidoRepository;

    private final ProdutoSevice produtoSevice;

    public ItemPedidoResponseDTO buscarItemPedido(Long idItemPedido){
        return new ItemPedidoResponseDTO(itemPedidoRepository.findById(idItemPedido).
                orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"ItemPedido não encotrado.")));
    }
    @Transactional
    public ItemPedidoResponseDTO salvarItemPedido(ItemPedidoRequestDTO itemPedidoRequestDTO){
        ItemPedido itemPedidoNovo = new ItemPedido(new Produto(
                produtoSevice.buscarProduto(itemPedidoRequestDTO.produtoId())),itemPedidoRequestDTO);
        return new ItemPedidoResponseDTO(itemPedidoRepository.save(itemPedidoNovo));
    }
    @Transactional
    public ItemPedidoResponseDTO editarItemPedido(ItemPedidoRequestDTO itemPedidoRequestDTO, Long idItemPedido){
        ItemPedido itemPedidoNovo = new ItemPedido(new Produto(
                produtoSevice.buscarProduto(itemPedidoRequestDTO.produtoId())),itemPedidoRequestDTO);
        if(itemPedidoRepository.existsById(idItemPedido)){
            return new ItemPedidoResponseDTO(itemPedidoRepository.save(itemPedidoNovo));
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
