package com.warlley.anotapedido_api.service;

import com.warlley.anotapedido_api.dto.PedidoRequestDTO;
import com.warlley.anotapedido_api.dto.PedidoResponseDTO;
import com.warlley.anotapedido_api.model.ItemPedido;
import com.warlley.anotapedido_api.model.Pedido;
import com.warlley.anotapedido_api.model.Usuario;
import com.warlley.anotapedido_api.repository.PedidoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PedidoServiceTest {

    @Mock
    PedidoRepository pedidoRepository;

    @InjectMocks
    PedidoService pedidoService;

    @Nested
    @DisplayName("Test de metodo buscar Pedido")
    class buscarPedido{
        @Test
        @DisplayName("Deve Retornar status 200 OK e pedido.")
        void deveBuscarPedido(){
            Long idPedido = 1L;
            Usuario usuario = new Usuario();
            List<ItemPedido> itemPedidoList = new ArrayList<>();
            Pedido pedidoBuscado = new Pedido(1L,usuario,itemPedidoList);
            when(pedidoRepository.existsById(idPedido)).thenReturn(true);
            when(pedidoRepository.findById(idPedido)).thenReturn(Optional.of(pedidoBuscado));

            PedidoResponseDTO resultado = pedidoService.buscarPedido(idPedido);

            assertNotNull(resultado);
            assertEquals(1L,resultado.id());
            assertEquals(itemPedidoList,resultado.itemPedidoList());
            assertEquals(usuario,resultado.usuario());

            verify(pedidoRepository,times(1)).existsById(idPedido);
            verify(pedidoRepository,times(1)).findById(idPedido);
        }
        @Test
        @DisplayName("Deve Retornar status 404 quando pedido não existir.")
        void deveLancarExcecaoBuscarPedido(){
            Long idPedido = 1L;
            when(pedidoRepository.existsById(idPedido)).thenReturn(false);

            ResponseStatusException ex =
                    assertThrows(ResponseStatusException.class, ()-> pedidoService.buscarPedido(idPedido));

            assertEquals(404,ex.getStatusCode().value());

            verify(pedidoRepository,times(1)).existsById(idPedido);
            verify(pedidoRepository,never()).findById(idPedido);
        }
    }
    @Nested
    @DisplayName("Test de metodo criar Pedido")
    class criarPedido{
        @Test
        @DisplayName("Deve Retornar status 201 Created e pedido.")
        void deveCadastrarPedido(){
            Long idPedido = 1L;
            Usuario usuario = new Usuario();
            List<ItemPedido> itemPedidoList = new ArrayList<>();
            Pedido pedidoSalvo = new Pedido(1L,usuario,itemPedidoList);
            when(pedidoRepository.save(pedidoSalvo)).thenReturn(pedidoSalvo);

            PedidoResponseDTO resultado = pedidoService.buscarPedido(idPedido);

            assertNotNull(resultado);
            assertEquals(1L,resultado.id());
            assertEquals(itemPedidoList,resultado.itemPedidoList());
            assertEquals(usuario,resultado.usuario());

            verify(pedidoRepository,times(1)).save(pedidoSalvo);
        }

    }
    @Nested
    @DisplayName("Test de metodo editar Pedido")
    class editarPedido{
        @Test
        @DisplayName("Deve Retornar status 200 OK quando pedido atualizado com sucesso.")
        void deveEditarPedido(){
            Long idPedido = 1L;
            Usuario usuario = new Usuario();
            List<ItemPedido> itemPedidoList = new ArrayList<>();
            Pedido pedidoSalvo = new Pedido(1L,usuario,itemPedidoList);
            when(pedidoRepository.existsById(idPedido)).thenReturn(true);
            when(pedidoRepository.save(pedidoSalvo)).thenReturn(pedidoSalvo);

            PedidoResponseDTO resultado = pedidoService.buscarPedido(idPedido);

            assertNotNull(resultado);
            assertEquals(1L,resultado.id());
            assertEquals(itemPedidoList,resultado.itemPedidoList());
            assertEquals(usuario,resultado.usuario());

            verify(pedidoRepository,times(1)).existsById(idPedido);
            verify(pedidoRepository,times(1)).save(pedidoSalvo);
        }
        @Test
        @DisplayName("Deve Retornar status 404 Not Found quando pedido não existir.")
        void deveLancarExcecaoEditarPedido(){
            Long idPedido = 1L;
            when(pedidoRepository.existsById(idPedido)).thenReturn(false);

            ResponseStatusException ex =
                    assertThrows(ResponseStatusException.class, ()-> pedidoService.editarPedido(any(PedidoRequestDTO.class),idPedido));

            assertEquals(404,ex.getStatusCode().value());

            verify(pedidoRepository,times(1)).existsById(idPedido);
            verify(pedidoRepository,never()).save(any(Pedido.class));
        }

    }
    @Nested
    @DisplayName("Test de metodo deletar Pedido")
    class deletarPedido{
        @Test
        @DisplayName("Deve Retornar status 204 No Content quando pedido deletado com sucesso.")
        void deveDeletarPedido(){
            Long idPedido = 1L;
            when(pedidoRepository.existsById(idPedido)).thenReturn(true);
            doNothing().when(pedidoRepository).deleteById(idPedido);

            pedidoService.removePedido(idPedido);

            verify(pedidoRepository,times(1)).existsById(idPedido);
            verify(pedidoRepository,times(1)).deleteById(idPedido);
        }
        @Test
        @DisplayName("Deve Retornar status 404 Not Found quando pedido não existe, para ser deletado.")
        void deveLancarExcecaoEntradaInvalidaDeletarPedido(){
            Long idPedido = 1L;
            when(pedidoRepository.existsById(idPedido)).thenReturn(false);

            ResponseStatusException ex =
                    assertThrows(ResponseStatusException.class, ()-> pedidoService.removePedido(idPedido));

            assertEquals(404,ex.getStatusCode().value());

            verify(pedidoRepository,times(1)).existsById(idPedido);
            verify(pedidoRepository,never()).deleteById(idPedido);
        }

    }

}
