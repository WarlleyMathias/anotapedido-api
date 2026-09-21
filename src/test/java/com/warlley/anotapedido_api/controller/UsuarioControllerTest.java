package com.warlley.anotapedido_api.controller;

import com.warlley.anotapedido_api.dto.UsuarioRequestDTO;
import com.warlley.anotapedido_api.dto.UsuarioResponseDTO;
import com.warlley.anotapedido_api.service.UsuarioService;
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

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UsuarioController.class)
public class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UsuarioService usuarioService;

    @Autowired
    private ObjectMapper objectMapper;

    @Nested
    @DisplayName("GET /usuarios/{idUsuario) - buscar usuario")
    class BuscarUsuario{

        @Test
        @DisplayName("Deve Retornar status 200 OK e Usuário.")
        @WithMockUser(username = "cliente@email.com")
        void deveBuscarUsuario() throws Exception{
            Long idUsuario = 1L;
            UsuarioResponseDTO usuarioResponseDTO = new UsuarioResponseDTO(1L,"marcos@gmail.com","12345678","Marcos","Rua 10 - RJ");
            when(usuarioService.buscaUsuario(idUsuario)).thenReturn(usuarioResponseDTO);

            mockMvc.perform(get("/usuarios/{idUsuario}", idUsuario)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.idUsuario").value(1))
                    .andExpect(jsonPath("$.email").value("marcos@gmail.com"))
                    .andExpect(jsonPath("$.senha").value("12345678"))
                    .andExpect(jsonPath("$.nome").value("Marcos"))
                    .andExpect(jsonPath("$.endereco").value("Rua 10 - RJ"));

            verify(usuarioService, times(1)).buscaUsuario(idUsuario);
        }
        @Test
        @DisplayName("Deve Retornar status 404 quando usuário não existir.")
        @WithMockUser(username = "cliente@email.com")
        void deveLancarExcecaoBuscarUsuario() throws Exception {
            Long idUsuario = 1L;

            when(usuarioService.buscaUsuario(idUsuario)).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Não existe produto com esse Id"));

            mockMvc.perform(get("/usuarios/{idUsuario}",idUsuario)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound());

            verify(usuarioService, times(1)).buscaUsuario(idUsuario);
        }
        @Test
        @DisplayName("Deve bloquear a busca de usuário e retornar 401 quando não for enviado o token")
        void buscarUsuario_semToken_deveRetornarUnauthorized() throws Exception {
            Long idUsuario = 1L;
            mockMvc.perform(get("/usuarios/{idUsuario}",idUsuario)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isUnauthorized()); // Exceção 401
        }
    }
    @Nested
    @DisplayName("POST /usuarios - criar usuario")
    class CriarUsuario{

        @Test
        @DisplayName("Deve Retornar status 201 Created e Usuário.")
        @WithMockUser(username = "cliente@email.com")
        void deveCadastrarUsuario() throws Exception{
            UsuarioRequestDTO usuarioRequestDTO = new UsuarioRequestDTO("marcos@gmail.com","12345678","Marcos","Rua 10 - RJ");
            UsuarioResponseDTO usuarioResponseDTO = new UsuarioResponseDTO(1L,"marcos@gmail.com","12345678","Marcos","Rua 10 - RJ");
            when(usuarioService.cadastrarUsuario(usuarioRequestDTO)).thenReturn(usuarioResponseDTO);

            mockMvc.perform(post("/usuarios")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.email").value("marcos@gmail.com"))
                    .andExpect(jsonPath("$.senha").value("12345678"))
                    .andExpect(jsonPath("$.nome").value("Marcos"))
                    .andExpect(jsonPath("$.endereco").value("Rua 10 - RJ"));

            verify(usuarioService, times(1)).cadastrarUsuario(usuarioRequestDTO);
        }
        @Test
        @DisplayName("Deve retornar status 400 Bad Request quando parâmetros for inválido.")
        @WithMockUser(username = "cliente@email.com")
        void deveLancarExcecaoEntradaInvalidaCadastraUsuario() throws Exception {

            UsuarioRequestDTO usuarioRequestDTO = new UsuarioRequestDTO("","12345678","Marcos","Rua 10 - RJ");

            mockMvc.perform(post("/usuarios")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(usuarioRequestDTO)))
                    .andExpect(status().isBadRequest());

            verify(usuarioService, never()).cadastrarUsuario(any(UsuarioRequestDTO.class));

        }
        @Test
        @DisplayName("Deve Retornar status 409 Conflict quando usuario já existe.")
        @WithMockUser(username = "cliente@email.com")
        void deveLancarExcecaoConflitoCadastrarUsuario() throws Exception {
            UsuarioRequestDTO usuarioRequestDTO = new UsuarioRequestDTO("marcos@gmail.com","12345678","Marcos","Rua 10 - RJ");

            when(usuarioService.cadastrarUsuario(usuarioRequestDTO)).thenThrow(new ResponseStatusException(HttpStatus.CONFLICT, "Já existe um usuário com esse email."));

            mockMvc.perform(post("/usuarios")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(usuarioRequestDTO)))
                    .andExpect(status().isConflict());

            verify(usuarioService, times(1)).cadastrarUsuario(usuarioRequestDTO);
        }
        @Test
        @DisplayName("Deve bloquear a criação de usuário e retornar 401 quando não for enviado o token")
        void criarUsuario_semToken_deveRetornarUnauthorized() throws Exception {
            mockMvc.perform(post("/usuarios")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isUnauthorized()); // Exceção 401
        }
    }

    @Nested
    @DisplayName("PUT /usuarios/{idUsuario} - editar usuario")
    class EditarUsuario{

        @Test
        @DisplayName("Deve Retornar status 200 OK quando usuário atualizado com sucesso.")
        @WithMockUser(username = "cliente@email.com")
        void deveEditarUsuario() throws Exception {
            Long idUsuario = 1L;
            UsuarioRequestDTO usuarioRequestDTO = new UsuarioRequestDTO("marcos@gmail.com","12345678","Marcos","Rua 10 - RJ");
            UsuarioResponseDTO usuarioAtualizado = new UsuarioResponseDTO(1L,"marcos@gmail.com","12345678","Marcos","Rua 10 - RJ");

            when(usuarioService.editarUsuario(usuarioRequestDTO, idUsuario)).thenReturn(usuarioAtualizado);

            mockMvc.perform(put("/usuarios/{idUsuario}", idUsuario)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(usuarioRequestDTO)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.email").value("marcos@gmail.com"))
                    .andExpect(jsonPath("$.senha").value("12345678"))
                    .andExpect(jsonPath("$.nome").value("Marcos"))
                    .andExpect(jsonPath("$.endereco").value("Rua 10 - RJ"));

            verify(usuarioService, times(1)).editarUsuario(usuarioRequestDTO, idUsuario);
        }
        @Test
        @DisplayName("Deve retornar status 400 Bad Request quando parâmetros for inválido.")
        @WithMockUser(username = "cliente@email.com")
        void deveLancarExcecaoEntradaInvalidaEditarUsuario() throws Exception {
            Long idUsuario = 1L;
            UsuarioRequestDTO usuarioRequestDTO = new UsuarioRequestDTO("","12345678","Marcos","Rua 10 - RJ");

            mockMvc.perform(put("/usuarios/{idUsuario}", idUsuario)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(usuarioRequestDTO)))
                    .andExpect(status().isBadRequest());

            verify(usuarioService, never()).editarUsuario(usuarioRequestDTO, idUsuario);
        }
        @Test
        @DisplayName("Deve Retornar status 404 Not Found quando usuário não existir.")
        @WithMockUser(username = "cliente@email.com")
        void deveLancarExcecaoEditarUsuario() throws Exception {
            Long idUsuario = 1L;
            UsuarioRequestDTO usuarioRequestDTO = new UsuarioRequestDTO("Marcos","12345678","Marcos","Rua 10 - RJ");

            when(usuarioService.editarUsuario(usuarioRequestDTO, idUsuario)).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "produto não existe para sem atualizado."));

            mockMvc.perform(put("/usuarios/{idUsuario}", idUsuario)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(usuarioRequestDTO)))
                    .andExpect(status().isNotFound());

            verify(usuarioService, times(1)).editarUsuario(usuarioRequestDTO, idUsuario);
        }
        @Test
        @DisplayName("Deve Retornar status 409 Conflict quando usuário já existe com esse email.")
        @WithMockUser(username = "cliente@email.com")
        void deveLancarExcecaoConflitoEditarUsuario() throws Exception {
            Long idUsuario = 1L;
            UsuarioRequestDTO usuarioRequestDTO = new UsuarioRequestDTO("Marcos","12345678","Marcos","Rua 10 - RJ");

            when(usuarioService.editarUsuario(usuarioRequestDTO, idUsuario)).thenThrow(new ResponseStatusException(HttpStatus.CONFLICT, "Já existe um usuário com esse nome."));

            mockMvc.perform(put("/usuarios/{idUsuario}", idUsuario)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(usuarioRequestDTO)))
                    .andExpect(status().isConflict());

            verify(usuarioService, times(1)).editarUsuario(usuarioRequestDTO, idUsuario);
        }
        @Test
        @DisplayName("Deve bloquear a edição de usuário e retornar 401 quando não for enviado o token")
        void editarUsuario_semToken_deveRetornarUnauthorized() throws Exception {
            Long idUsuario = 1L;
            mockMvc.perform(put("/usuarios/{idUsuario}",idUsuario)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isUnauthorized()); // Exceção 401
        }
    }
    @Nested
    @DisplayName("DELETE /usuarios/{idUsuario} - deletar usuario")
    class DeletarUsuario{
        @Test
        @DisplayName("Deve Retornar status 204 No Content quando usuário deletado com sucesso.")
        @WithMockUser(username = "cliente@email.com")
        void deveDeletarUsuario() throws Exception {
            Long idUsuario = 1L;

            doNothing().when(usuarioService).removeUsuario(idUsuario);

            mockMvc.perform(delete("/usuarios/{idUsuario}",idUsuario)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNoContent());

            verify(usuarioService, times(1)).removeUsuario(idUsuario);
        }

        @Test
        @DisplayName("Deve Retornar status 404 Not Found quando usuário não existe, para ser deletado.")
        @WithMockUser(username = "cliente@email.com")
        void deveLancarExcecaoEntradaInvalidaDeletarUsuario() throws Exception {
            Long idUsuario = 1L;

            doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Não existe produto com esse Id."))
                    .when(usuarioService).removeUsuario(idUsuario);

            mockMvc.perform(delete("/usuarios/{idUsuario}",idUsuario)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound());

            verify(usuarioService, times(1)).removeUsuario(idUsuario);
        }

        @Test
        @DisplayName("Deve Retornar status 409 Conflict quando usuário está sendo usado por outra tabela.")
        @WithMockUser(username = "cliente@email.com")
        void deveLancarExcecaoConflitoDeletarUsuario() throws Exception {
            Long idUsuario = 1L;

            doThrow(new ResponseStatusException(HttpStatus.CONFLICT, "Não existe produto com esse Id."))
                    .when(usuarioService).removeUsuario(idUsuario);

            mockMvc.perform(delete("/usuarios/{idUsuario}",idUsuario)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isConflict());

            verify(usuarioService, times(1)).removeUsuario(idUsuario);
        }
        @Test
        @DisplayName("Deve deletar a busca de usuário e retornar 401 quando não for enviado o token")
        void deletarUsuario_semToken_deveRetornarUnauthorized() throws Exception {
            Long idUsuario = 1L;
            mockMvc.perform(delete("/usuarios/{idUsuario}",idUsuario)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isUnauthorized()); // Exceção 401
        }
    }

}
