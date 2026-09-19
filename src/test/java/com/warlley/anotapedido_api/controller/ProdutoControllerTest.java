package com.warlley.anotapedido_api.controller;

import com.warlley.anotapedido_api.dto.ProdutoRequestDTO;
import com.warlley.anotapedido_api.dto.ProdutoResponseDTO;
import com.warlley.anotapedido_api.service.ProdutoSevice;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;
import tools.jackson.databind.ObjectMapper;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProdutoController.class)
public class ProdutoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProdutoSevice produtoService;

    @Autowired
    private ObjectMapper objectMapper;

    @Nested
    @DisplayName("GET /produtos/{idProduto) - buscar produto")
    class BuscarProduto{

        @Test
        @DisplayName("Deve Retornar status 200 OK e produto.")
        void deveBuscarProduto() throws Exception{
            Long idProduto = 1L;
            ProdutoResponseDTO usuarioResponseDTO = new ProdutoResponseDTO(1L,"lanche",10.0f);
            when(produtoService.buscarProduto(idProduto)).thenReturn(usuarioResponseDTO);

            mockMvc.perform(get("/produtos/{idProduto}", idProduto)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.idProduto").value(1))
                    .andExpect(jsonPath("$.nome").value("lanche"))
                    .andExpect(jsonPath("$.senha").value(10.0f));

            verify(produtoService, times(1)).buscarProduto(idProduto);
        }
        @Test
        @DisplayName("Deve Retornar status 404 quando produto não existir.")
        void deveLancarExcecaoBuscarProduto() throws Exception {
            Long idProduto = 1L;

            when(produtoService.buscarProduto(idProduto)).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Não existe Produto com esse Id"));

            mockMvc.perform(get("/produtos/{idProduto}",idProduto)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound());

            verify(produtoService, times(1)).buscarProduto(idProduto);
        }
    }
    @Nested
    @DisplayName("POST /produtos - criar produto")
    class CriarUsuario{

        @Test
        @DisplayName("Deve Retornar status 201 Created e produto.")
        void deveCadastrarProduto() throws Exception{
            ProdutoRequestDTO produtoRequestDTO = new ProdutoRequestDTO("lanche",10.0f);
            ProdutoResponseDTO usuarioResponseDTO = new ProdutoResponseDTO(1L,"lanche",10.0f);
            when(produtoService.cadastrarProduto(produtoRequestDTO)).thenReturn(usuarioResponseDTO);

            mockMvc.perform(post("/produtos")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.nome").value("lanche"))
                    .andExpect(jsonPath("$.senha").value(10.0f));

            verify(produtoService, times(1)).cadastrarProduto(produtoRequestDTO);
        }
        @Test
        @DisplayName("Deve retornar status 400 Bad Request quando parâmetros for inválido.")
        void deveLancarExcecaoEntradaInvalidaCadastraProduto() throws Exception {

            ProdutoRequestDTO produtoRequestDTO = new ProdutoRequestDTO("",10.0f);

            mockMvc.perform(post("/produtos")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(produtoRequestDTO)))
                    .andExpect(status().isBadRequest());

            verify(produtoService, never()).cadastrarProduto(any(ProdutoRequestDTO.class));

        }
    }
    @Test
    @DisplayName("Deve Retornar status 409 Conflict quando produto já existe.")
    void deveLancarExcecaoConflitoCadastrarProduto() throws Exception {
        ProdutoRequestDTO produtoRequestDTO = new ProdutoRequestDTO("lanche",10.0f);

        when(produtoService.cadastrarProduto(produtoRequestDTO)).thenThrow(new ResponseStatusException(HttpStatus.CONFLICT, "Já existe um produto com esse nome."));

        mockMvc.perform(post("/produtos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(produtoRequestDTO)))
                .andExpect(status().isConflict());

        verify(produtoService, times(1)).cadastrarProduto(produtoRequestDTO);
    }
    @Nested
    @DisplayName("PUT /produtos/{idProduto} - editar produto")
    class EditarUsuario{

        @Test
        @DisplayName("Deve Retornar status 200 OK quando produto atualizado com sucesso.")
        void deveEditarProduto() throws Exception {
            Long idProduto = 1L;
            ProdutoRequestDTO produtoRequestDTO = new ProdutoRequestDTO("lanche",10.0f);
            ProdutoResponseDTO produtoAtualizado = new ProdutoResponseDTO(1L,"lanche",10.0f);

            when(produtoService.editarProduto(produtoRequestDTO, idProduto)).thenReturn(produtoAtualizado);

            mockMvc.perform(put("/produtos/{idProduto}", idProduto)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(produtoRequestDTO)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.nome").value("lanche"))
                    .andExpect(jsonPath("$.senha").value(10.0f));

            verify(produtoService, times(1)).editarProduto(produtoRequestDTO, idProduto);
        }
        @Test
        @DisplayName("Deve retornar status 400 Bad Request quando parâmetros for inválido.")
        void deveLancarExcecaoEntradaInvalidaEditarProduto() throws Exception {
            Long idProduto = 1L;
            ProdutoRequestDTO produtoRequestDTO = new ProdutoRequestDTO("",10.0f);

            mockMvc.perform(put("/produtos/{idProduto}", idProduto)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(produtoRequestDTO)))
                    .andExpect(status().isBadRequest());

            verify(produtoService, never()).editarProduto(produtoRequestDTO, idProduto);
        }
        @Test
        @DisplayName("Deve Retornar status 404 Not Found quando produto não existir.")
        void deveLancarExcecaoEditarProduto() throws Exception {
            Long idProduto = 1L;
            ProdutoRequestDTO produtoRequestDTO = new ProdutoRequestDTO("lanche",10.0f);

            when(produtoService.editarProduto(produtoRequestDTO, idProduto)).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto não existe para sem atualizado."));

            mockMvc.perform(put("/produtos/{idProduto}", idProduto)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(produtoRequestDTO)))
                    .andExpect(status().isNotFound());

            verify(produtoService, times(1)).editarProduto(produtoRequestDTO, idProduto);
        }
        @Test
        @DisplayName("Deve Retornar status 409 Conflict quando produto já existe com esse nome.")
        void deveLancarExcecaoConflitoEditarProduto() throws Exception {
            Long idProduto = 1L;
            ProdutoRequestDTO produtoRequestDTO = new ProdutoRequestDTO("lanche",10.0f);

            when(produtoService.editarProduto(produtoRequestDTO, idProduto)).thenThrow(new ResponseStatusException(HttpStatus.CONFLICT, "Já existe um produto com esse nome."));

            mockMvc.perform(put("/produtos/{idProduto}", idProduto)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(produtoRequestDTO)))
                    .andExpect(status().isConflict());

            verify(produtoService, times(1)).editarProduto(produtoRequestDTO, idProduto);
        }
    }
    @Nested
    @DisplayName("DELETE /produtos/{idProduto} - deletar produto")
    class DeletarUsuario{
        @Test
        @DisplayName("Deve Retornar status 204 No Content quando produto deletado com sucesso.")
        void deveDeletarProduto() throws Exception {
            Long idProduto = 1L;

            doNothing().when(produtoService).removeProduto(idProduto);

            mockMvc.perform(delete("/produtos/{idProduto}",idProduto)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNoContent());

            verify(produtoService, times(1)).removeProduto(idProduto);
        }

        @Test
        @DisplayName("Deve Retornar status 404 Not Found quando produto não existe, para ser deletado.")
        void deveLancarExcecaoEntradaInvalidaDeletarProduto() throws Exception {
            Long idProduto = 1L;

            doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Não existe Produto com esse Id."))
                    .when(produtoService).removeProduto(idProduto);

            mockMvc.perform(delete("/produtos/{idProduto}",idProduto)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound());

            verify(produtoService, times(1)).removeProduto(idProduto);
        }

        @Test
        @DisplayName("Deve Retornar status 409 Conflict quando produto está sendo usado por outra tabela.")
        void deveLancarExcecaoConflitoDeletarProduto() throws Exception {
            Long idProduto = 1L;

            doThrow(new ResponseStatusException(HttpStatus.CONFLICT, "Não existe Produto com esse Id."))
                    .when(produtoService).removeProduto(idProduto);

            mockMvc.perform(delete("/produtos/{idProduto}",idProduto)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isConflict());

            verify(produtoService, times(1)).removeProduto(idProduto);
        }
    }

}