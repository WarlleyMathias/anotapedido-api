package com.warlley.anotapedido_api.controller;

import com.warlley.anotapedido_api.dto.ItemPedidoRequestDTO;
import com.warlley.anotapedido_api.dto.PedidoRequestDTO;
import com.warlley.anotapedido_api.dto.PedidoResponseDTO;
import com.warlley.anotapedido_api.model.ItemPedido;
import com.warlley.anotapedido_api.model.Usuario;
import com.warlley.anotapedido_api.service.PedidoService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;
import tools.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PedidoController.class)
public class PedidoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PedidoService pedidoService;

    @Autowired
    private ObjectMapper objectMapper;

    @Nested
    @DisplayName("GET /pedidos/{idPedido) - buscar pedido")
    class BuscarProduto{

        @Test
        @DisplayName("Deve Retornar status 200 OK e pedido.")
        @WithMockUser(username = "cliente@email.com")
        void deveBuscarPedido() throws Exception{
            Long idPedido = 1L;
            Usuario usuario = new Usuario();
            List<ItemPedido> itemPedidoList = new ArrayList<>();
            PedidoResponseDTO pedidoResponseDTO = new PedidoResponseDTO(1L,usuario,itemPedidoList);
            when(pedidoService.buscarPedido(idPedido)).thenReturn(pedidoResponseDTO);

            mockMvc.perform(get("/pedidos/{idPedido}", idPedido)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(1))
                    .andExpect(jsonPath("$.usuario").value(usuario))
                    .andExpect(jsonPath("$.itemPedidoList").value(itemPedidoList));

            verify(pedidoService, times(1)).buscarPedido(idPedido);
        }
        @Test
        @DisplayName("Deve Retornar status 404 quando pedido não existir.")
        @WithMockUser(username = "cliente@email.com")
        void deveLancarExcecaoBuscarPedido() throws Exception {
            Long idPedido = 1L;

            when(pedidoService.buscarPedido(idPedido)).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Não existe pedido com esse Id"));

            mockMvc.perform(get("/pedidos/{idPedido}",idPedido)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound());

            verify(pedidoService, times(1)).buscarPedido(idPedido);
        }
        @Test
        @DisplayName("Deve bloquear a busca de pedido e retornar 401 quando não for enviado o token")
        void buscarPedido_semToken_deveRetornarUnauthorized() throws Exception {
            Long idPedido = 1L;
            mockMvc.perform(get("/pedidos/{idPedido}",idPedido)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isUnauthorized()); // Exceção 401
        }
    }
    @Nested
    @DisplayName("POST /pedidos - criar pedido")
    class CriarUsuario{

        @Test
        @DisplayName("Deve Retornar status 201 Created e pedido.")
        @WithMockUser(username = "cliente@email.com")
        void deveCadastrarPedido() throws Exception{
            Usuario usuario = new Usuario();
            List<ItemPedido> itemPedidoList = new ArrayList<>();
            List<ItemPedidoRequestDTO> itemPedidoRequestDTOList = new ArrayList<>();
            PedidoRequestDTO pedidoRequestDTO = new PedidoRequestDTO(usuario.getId(),itemPedidoRequestDTOList);
            PedidoResponseDTO pedidoResponseDTO = new PedidoResponseDTO(1L,usuario,itemPedidoList);
            when(pedidoService.salvarPedido(pedidoRequestDTO)).thenReturn(pedidoResponseDTO);

            mockMvc.perform(post("/pedidos")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.usuarioId").value(usuario.getId()))
                    .andExpect(jsonPath("$.itemPedidoRequestDTOList").value(itemPedidoRequestDTOList));

            verify(pedidoService, times(1)).salvarPedido(pedidoRequestDTO);
        }
        @Test
        @DisplayName("Deve retornar status 400 Bad Request quando parâmetros for inválido.")
        @WithMockUser(username = "cliente@email.com")
        void deveLancarExcecaoEntradaInvalidaCadastraPedido() throws Exception {
            List<ItemPedidoRequestDTO> itemPedidoRequestDTOList = new ArrayList<>();
            PedidoRequestDTO pedidoRequestDTO = new PedidoRequestDTO(null,itemPedidoRequestDTOList);

            mockMvc.perform(post("/pedidos")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(pedidoRequestDTO)))
                    .andExpect(status().isBadRequest());

            verify(pedidoService, never()).salvarPedido(any(PedidoRequestDTO.class));

        }
        @Test
        @DisplayName("Deve bloquear a criação de pedido e retornar 401 quando não for enviado o token")
        void cadastrarPedido_semToken_deveRetornarUnauthorized() throws Exception {
            mockMvc.perform(post("/pedidos")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isUnauthorized()); // Exceção 401
        }
    }
    @Nested
    @DisplayName("PUT /pedidos/{idPedido} - editar pedido")
    class EditarUsuario{

        @Test
        @DisplayName("Deve Retornar status 200 OK quando pedido atualizado com sucesso.")
        @WithMockUser(username = "cliente@email.com")
        void deveEditarPedido() throws Exception {
            Long idPedido = 1L;
            Usuario usuario = new Usuario();
            List<ItemPedido> itemPedidoList = new ArrayList<>();
            List<ItemPedidoRequestDTO> itemPedidoRequestDTOList = new ArrayList<>();
            PedidoRequestDTO pedidoRequestDTO = new PedidoRequestDTO(usuario.getId(),itemPedidoRequestDTOList);
            PedidoResponseDTO pedidoResponseDTO = new PedidoResponseDTO(1L,usuario,itemPedidoList);

            when(pedidoService.editarPedido(pedidoRequestDTO, idPedido)).thenReturn(pedidoResponseDTO);

            mockMvc.perform(put("/pedidos/{idPedido}", idPedido)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(pedidoRequestDTO)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.nome").value("lanche"))
                    .andExpect(jsonPath("$.senha").value(10.0f));

            verify(pedidoService, times(1)).editarPedido(pedidoRequestDTO, idPedido);
        }
        @Test
        @DisplayName("Deve retornar status 400 Bad Request quando parâmetros for inválido.")
        @WithMockUser(username = "cliente@email.com")
        void deveLancarExcecaoEntradaInvalidaEditarPedido() throws Exception {
            Long idPedido = 1L;
            List<ItemPedidoRequestDTO> itemPedidoRequestDTOList = new ArrayList<>();
            PedidoRequestDTO pedidoRequestDTO = new PedidoRequestDTO(null,itemPedidoRequestDTOList);

            mockMvc.perform(put("/pedidos/{idPedido}", idPedido)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(pedidoRequestDTO)))
                    .andExpect(status().isBadRequest());

            verify(pedidoService, never()).editarPedido(pedidoRequestDTO, idPedido);
        }
        @Test
        @DisplayName("Deve Retornar status 404 Not Found quando pedido não existir.")
        @WithMockUser(username = "cliente@email.com")
        void deveLancarExcecaoEditarPedido() throws Exception {
            Long idPedido = 1L;
            Usuario usuario = new Usuario();
            List<ItemPedidoRequestDTO> itemPedidoRequestDTOList = new ArrayList<>();
            PedidoRequestDTO pedidoRequestDTO = new PedidoRequestDTO(usuario.getId(), itemPedidoRequestDTOList);

            when(pedidoService.editarPedido(pedidoRequestDTO, idPedido)).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "pedido não existe para sem atualizado."));

            mockMvc.perform(put("/pedidos/{idPedido}", idPedido)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(pedidoRequestDTO)))
                    .andExpect(status().isNotFound());

            verify(pedidoService, times(1)).editarPedido(pedidoRequestDTO, idPedido);
        }
        @Test
        @DisplayName("Deve bloquear a edição de pedido e retornar 401 quando não for enviado o token")
        void editarPedido_semToken_deveRetornarUnauthorized() throws Exception {
            Long idPedido = 1L;
            mockMvc.perform(put("/pedidos/{idPedido}",idPedido)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isUnauthorized()); // Exceção 401
        }

    }
    @Nested
    @DisplayName("DELETE /pedidos/{idPedido} - deletar pedido")
    @WithMockUser(username = "cliente@email.com")
    class DeletarUsuario{
        @Test
        @DisplayName("Deve Retornar status 204 No Content quando pedido deletado com sucesso.")
        void deveDeletarPedido() throws Exception {
            Long idPedido = 1L;

            doNothing().when(pedidoService).removePedido(idPedido);

            mockMvc.perform(delete("/pedidos/{idPedido}",idPedido)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNoContent());

            verify(pedidoService, times(1)).removePedido(idPedido);
        }

        @Test
        @DisplayName("Deve Retornar status 404 Not Found quando pedido não existe, para ser deletado.")
        @WithMockUser(username = "cliente@email.com")
        void deveLancarExcecaoEntradaInvalidaDeletarPedido() throws Exception {
            Long idPedido = 1L;

            doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Não existe pedido com esse Id."))
                    .when(pedidoService).removePedido(idPedido);

            mockMvc.perform(delete("/pedidos/{idPedido}",idPedido)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound());

            verify(pedidoService, times(1)).removePedido(idPedido);
        }

        @Test
        @DisplayName("Deve Retornar status 409 Conflict quando pedido está sendo usado por outra tabela.")
        @WithMockUser(username = "cliente@email.com")
        void deveLancarExcecaoConflitoDeletarPedido() throws Exception {
            Long idPedido = 1L;

            doThrow(new ResponseStatusException(HttpStatus.CONFLICT, "Não existe pedido com esse Id."))
                    .when(pedidoService).removePedido(idPedido);

            mockMvc.perform(delete("/pedidos/{idPedido}",idPedido)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isConflict());

            verify(pedidoService, times(1)).removePedido(idPedido);
        }
        @Test
        @DisplayName("Deve bloquear a remoção de pedido e retornar 401 quando não for enviado o token")
        void deletarPedido_semToken_deveRetornarUnauthorized() throws Exception {
            Long idPedido = 1L;
            mockMvc.perform(delete("/pedidos/{idPedido}",idPedido)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isUnauthorized()); // Exceção 401
        }
    }

}