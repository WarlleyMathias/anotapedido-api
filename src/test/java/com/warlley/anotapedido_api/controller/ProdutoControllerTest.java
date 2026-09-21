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
import org.springframework.security.test.context.support.WithMockUser;
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
        @WithMockUser(username = "cliente@email.com")
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
        @WithMockUser(username = "cliente@email.com")
        void deveLancarExcecaoBuscarProduto() throws Exception {
            Long idProduto = 1L;

            when(produtoService.buscarProduto(idProduto)).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Não existe Produto com esse Id"));

            mockMvc.perform(get("/produtos/{idProduto}",idProduto)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound());

            verify(produtoService, times(1)).buscarProduto(idProduto);
        }
        @Test
        @DisplayName("Deve bloquear a busca de produto e retornar 401 quando não for enviado o token")
        void buscarProduto_semToken_deveRetornarUnauthorized() throws Exception {
            Long idProduto = 1L;
            mockMvc.perform(get("/produtos/{idProduto}",idProduto)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isUnauthorized()); // Exceção 401
        }
    }
    @Nested
    @DisplayName("POST /produtos - criar produto")
    class CriarProduto {

        @Test
        @DisplayName("Deve Retornar status 201 Created e produto.")
        @WithMockUser(username = "cliente@email.com", roles = {"ADMIN"})
        void deveCadastrarProduto() throws Exception {
            ProdutoRequestDTO produtoRequestDTO = new ProdutoRequestDTO("lanche", 10.0f);
            ProdutoResponseDTO usuarioResponseDTO = new ProdutoResponseDTO(1L, "lanche", 10.0f);
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
        @WithMockUser(username = "cliente@email.com", roles = {"ADMIN"})
        void deveLancarExcecaoEntradaInvalidaCadastraProduto() throws Exception {

            ProdutoRequestDTO produtoRequestDTO = new ProdutoRequestDTO("", 10.0f);

            mockMvc.perform(post("/produtos")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(produtoRequestDTO)))
                    .andExpect(status().isBadRequest());

            verify(produtoService, never()).cadastrarProduto(any(ProdutoRequestDTO.class));
        }
        @Test
        @DisplayName("Deve Retornar status 409 Conflict quando produto já existe.")
        @WithMockUser(username = "cliente@email.com", roles = {"ADMIN"})
        void deveLancarExcecaoConflitoCadastrarProduto() throws Exception {
            ProdutoRequestDTO produtoRequestDTO = new ProdutoRequestDTO("lanche", 10.0f);

            when(produtoService.cadastrarProduto(produtoRequestDTO)).thenThrow(new ResponseStatusException(HttpStatus.CONFLICT, "Já existe um produto com esse nome."));

            mockMvc.perform(post("/produtos")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(produtoRequestDTO)))
                    .andExpect(status().isConflict());

            verify(produtoService, times(1)).cadastrarProduto(produtoRequestDTO);
        }
        @Test
        @DisplayName("Deve bloquear a criação de produto e retornar 401 quando não for enviado o token")
        void cadastrarProduto_semToken_deveRetornarUnauthorized() throws Exception {
            mockMvc.perform(post("/produto")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isUnauthorized()); // Exceção 401
        }
        @Test
        @DisplayName("Deve retornar 403 Forbidden ao tentar cadastrar produto sendo apenas USER")
        @WithMockUser(username = "cliente@email.com")
        void cadastrarProduto_comRoleUser_deveRetornarForbidden() throws Exception {
            ProdutoRequestDTO produtoRequestDTO = new ProdutoRequestDTO("lanche", 10.0f);
            mockMvc.perform(post("/produtos")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(produtoRequestDTO)))
                    .andExpect(status().isForbidden()); // Exceção 403
        }
    }
    @Nested
    @DisplayName("PUT /produtos/{idProduto} - editar produto")
    class EditarProduto{

        @Test
        @DisplayName("Deve Retornar status 200 OK quando produto atualizado com sucesso.")
        @WithMockUser(username = "cliente@email.com", roles = {"ADMIN"})
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
        @WithMockUser(username = "cliente@email.com", roles = {"ADMIN"})
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
        @WithMockUser(username = "cliente@email.com", roles = {"ADMIN"})
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
        @WithMockUser(username = "cliente@email.com", roles = {"ADMIN"})
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
        @Test
        @DisplayName("Deve bloquear a edição de produto e retornar 401 quando não for enviado o token")
        void editarProduto_semToken_deveRetornarUnauthorized() throws Exception {
            Long idProduto = 1L;
            mockMvc.perform(put("/produtos/{idProduto}",idProduto)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isUnauthorized()); // Exceção 401
        }
        @Test
        @DisplayName("Deve retornar 403 Forbidden ao tentar editar produto sendo apenas USER")
        @WithMockUser(username = "cliente@email.com")
        void editarProduto_comRoleUser_deveRetornarForbidden() throws Exception {
            Long idProduto = 1L;
            ProdutoRequestDTO produtoRequestDTO = new ProdutoRequestDTO("lanche", 10.0f);
            mockMvc.perform(put("/produtos/{idProduto}",idProduto)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(produtoRequestDTO)))
                    .andExpect(status().isForbidden()); // Exceção 403
        }
    }
    @Nested
    @DisplayName("DELETE /produtos/{idProduto} - deletar produto")
    @WithMockUser(username = "cliente@email.com", roles = {"ADMIN"})
    class DeletarProduto{
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
        @WithMockUser(username = "cliente@email.com", roles = {"ADMIN"})
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
        @WithMockUser(username = "cliente@email.com", roles = {"ADMIN"})
        void deveLancarExcecaoConflitoDeletarProduto() throws Exception {
            Long idProduto = 1L;

            doThrow(new ResponseStatusException(HttpStatus.CONFLICT, "Não existe Produto com esse Id."))
                    .when(produtoService).removeProduto(idProduto);

            mockMvc.perform(delete("/produtos/{idProduto}",idProduto)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isConflict());

            verify(produtoService, times(1)).removeProduto(idProduto);
        }
        @Test
        @DisplayName("Deve bloquear a remoção de produto e retornar 401 quando não for enviado o token")
        void deletarProduto_semToken_deveRetornarUnauthorized() throws Exception {
            Long idProduto = 1L;
            mockMvc.perform(delete("/produtos/{idProduto}",idProduto)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isUnauthorized()); // Exceção 401
        }
        @Test
        @DisplayName("Deve retornar 403 Forbidden ao tentar deletar produto sendo apenas USER")
        @WithMockUser(username = "cliente@email.com")
        void deletarProduto_comRoleUser_deveRetornarForbidden() throws Exception {
            Long idProduto = 1L;
            ProdutoRequestDTO produtoRequestDTO = new ProdutoRequestDTO("lanche", 10.0f);
            mockMvc.perform(delete("/produtos/{idProduto}",idProduto)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(produtoRequestDTO)))
                    .andExpect(status().isForbidden()); // Exceção 403
        }
    }
}