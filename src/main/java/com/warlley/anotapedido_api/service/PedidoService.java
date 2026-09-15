package com.warlley.anotapedido_api.service;

import com.warlley.anotapedido_api.dto.PedidoRequestDTO;
import com.warlley.anotapedido_api.dto.PedidoResponseDTO;
import com.warlley.anotapedido_api.model.ItemPedido;
import com.warlley.anotapedido_api.model.Pedido;
import com.warlley.anotapedido_api.model.Produto;
import com.warlley.anotapedido_api.model.Usuario;
import com.warlley.anotapedido_api.repository.PedidoRepository;
import com.warlley.anotapedido_api.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

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
            pedidoNovo.setId(idPedido);
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
        // 1. Busca o usuário cadastrado no banco
        Usuario usuario = usuarioRepository.findById(pedidoRequestDTO.usuarioId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado."));

        // 2. Cria a instância principal do Pedido
        Pedido pedidoNovo = new Pedido();
        pedidoNovo.setUsuario(usuario);

        // 3. Mapeia e vincula a lista de itens ao pedido
        List<ItemPedido> itemPedidoList = pedidoRequestDTO.itemPedidoRequestDTOList().stream()
                .map(itemDto -> {
                    // Busca o produto real cadastrado no banco de dados
                    Produto produto = produtoSevice.findId(itemDto.produtoId());

                    // Instancia o ItemPedido com a assinatura adequada
                    ItemPedido itemPedido = new ItemPedido(produto,itemDto.quantidade());

                    // ⚠️ VÍNCULO OBRIGATÓRIO: Liga o item ao pedido que está sendo criado
                    itemPedido.setPedido(pedidoNovo);

                    return itemPedido;
                })
                .collect(Collectors.toList());

        pedidoNovo.setItemPedidoList(itemPedidoList);

        return pedidoNovo;
    }
}
