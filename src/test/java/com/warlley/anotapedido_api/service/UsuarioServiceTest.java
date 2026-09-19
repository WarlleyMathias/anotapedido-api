package com.warlley.anotapedido_api.service;

import com.warlley.anotapedido_api.dto.UsuarioRequestDTO;
import com.warlley.anotapedido_api.dto.UsuarioResponseDTO;
import com.warlley.anotapedido_api.model.Usuario;
import com.warlley.anotapedido_api.repository.UsuarioRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UsuarioServiceTest {

    @Mock
    UsuarioRepository usuarioRepository;

    @InjectMocks
    UsuarioService usuarioService;

    @Nested
    @DisplayName("Test de metodo buscar Usuário.")
    class buscarUsuario{

        @Test
        @DisplayName("Deve retornar 200 OK e retornar um usuário.")
        void deveBuscarUsuario(){
            Long idUsusario = 1L;
            Usuario usuarioSave = new Usuario(1L,"marcos@gmail.com" ,"12345678", "Marcos", "Rua 10 - RJ");
            when(usuarioRepository.existsById(idUsusario)).thenReturn(true);
            when(usuarioRepository.findById(idUsusario)).thenReturn(Optional.of(usuarioSave));

            UsuarioResponseDTO resultado = usuarioService.buscaUsuario(idUsusario);

            assertNotNull(resultado);
            assertEquals(1L,resultado.id());
            assertEquals("marcos@gmail.com",resultado.email());
            assertEquals("12345678",resultado.senha());
            assertEquals("Marcos",resultado.nome());
            assertEquals("Rua 10 - RJ",resultado.endereco());

            verify(usuarioRepository,times(1)).existsById(idUsusario);
            verify(usuarioRepository, times(1)).findById(idUsusario);

        }
        @Test
        @DisplayName("Deve Retornar status 404 quando usuário não existir.")
        void deveLancarUmaExcecaoBuscarUsuario(){

            Long idUsusario = 1L;
            when(usuarioRepository.existsById(idUsusario)).thenReturn(false);

            ResponseStatusException ex =
                    assertThrows(ResponseStatusException.class, () -> usuarioService.buscaUsuario(idUsusario));

            assertEquals(404, ex.getStatusCode().value());

            verify(usuarioRepository,times(1)).existsById(idUsusario);
            verify(usuarioRepository, never()).findById(idUsusario);

        }

    }
    @Nested
    @DisplayName("Test de metodo cadastrar Usuário.")
    class cadastrarUsuario {
        @Test
        @DisplayName("Deve Retornar status 201 Created e Usuário.")
        void deveCadastrarUsuario() {
            UsuarioRequestDTO usuarioRequestDTO = new UsuarioRequestDTO("marcos@gmail.com", "12345678", "Marcos", "Rua 10 - RJ");
            Usuario usuarioSave = new Usuario(1L, "marcos@gmail.com", "12345678", "Marcos", "Rua 10 - RJ");
            when(usuarioRepository.existsByEmailFalse(usuarioRequestDTO.email())).thenReturn(true);
            when(usuarioRepository.save(usuarioSave)).thenReturn(usuarioSave);

            UsuarioResponseDTO resultado = usuarioService.cadastrarUsuario(usuarioRequestDTO);

            assertNotNull(resultado);
            assertEquals(1L, resultado.id());
            assertEquals("marcos@gmail.com", resultado.email());
            assertEquals("12345678", resultado.senha());
            assertEquals("Marcos", resultado.nome());
            assertEquals("Rua 10 - RJ", resultado.endereco());

            verify(usuarioRepository, times(1)).existsByEmailFalse(usuarioRequestDTO.email());
            verify(usuarioRepository, times(1)).save(usuarioSave);

        }

        @Test
        @DisplayName("Deve Retornar status 409 Conflict quando usuario já existe.")
        void deveLancarExcecaoConflitoCadastrarUsuario() {

            UsuarioRequestDTO usuarioRequestDTO = new UsuarioRequestDTO("marcos@gmail.com", "12345678", "Marcos", "Rua 10 - RJ");
            Usuario usuarioSave = new Usuario(1L, "marcos@gmail.com", "12345678", "Marcos", "Rua 10 - RJ");
            when(usuarioRepository.existsByEmailFalse(usuarioRequestDTO.email())).thenReturn(false);

            ResponseStatusException ex =
                    assertThrows(ResponseStatusException.class, () -> usuarioService.cadastrarUsuario(usuarioRequestDTO));

            assertEquals(409, ex.getStatusCode().value());

            verify(usuarioRepository, times(1)).existsByEmailFalse(usuarioRequestDTO.email());
            verify(usuarioRepository, never()).save(usuarioSave);
        }
    }
    @Nested
    @DisplayName("Test de metodo editar Usuário.")
    class editarUsuario{

        @Test
        @DisplayName("Deve Retornar status 200 OK quando usuário atualizado com sucesso.")
        void deveEditarUsuario(){
            Long idUsusario = 1L;
            UsuarioRequestDTO usuarioRequestDTO = new UsuarioRequestDTO("marcos@gmail.com", "12345678", "Marcos", "Rua 10 - RJ");
            Usuario usuarioSave = new Usuario(1L, "marcos@gmail.com", "12345678", "Marcos", "Rua 10 - RJ");
            when(usuarioRepository.existsById(idUsusario)).thenReturn(true);
            when(usuarioRepository.existsByEmailFalse(usuarioRequestDTO.email())).thenReturn(true);
            when(usuarioRepository.save(usuarioSave)).thenReturn(usuarioSave);

            UsuarioResponseDTO resultado = usuarioService.editarUsuario(usuarioRequestDTO, idUsusario);

            assertNotNull(resultado);
            assertEquals(1L, resultado.id());
            assertEquals("marcos@gmail.com", resultado.email());
            assertEquals("12345678", resultado.senha());
            assertEquals("Marcos", resultado.nome());
            assertEquals("Rua 10 - RJ", resultado.endereco());

            verify(usuarioRepository, times(1)).existsById(idUsusario);
            verify(usuarioRepository, times(1)).existsByEmailFalse(usuarioRequestDTO.email());
            verify(usuarioRepository, times(1)).save(usuarioSave);

        }
        @Test
        @DisplayName("Deve Retornar status 404 Not Found quando usuário não existir.")
        void deveLancarExcecaoEditarUsuario(){
            Long idUsusario = 1L;
            UsuarioRequestDTO usuarioRequestDTO = new UsuarioRequestDTO("marcos@gmail.com", "12345678", "Marcos", "Rua 10 - RJ");

            when(usuarioRepository.existsById(idUsusario)).thenReturn(false);

            ResponseStatusException ex =
                    assertThrows(ResponseStatusException.class, () -> usuarioService.editarUsuario(usuarioRequestDTO, idUsusario));

            assertEquals(404, ex.getStatusCode().value());

            verify(usuarioRepository, times(1)).existsById(idUsusario);
            verify(usuarioRepository, never()).existsByEmailFalse(usuarioRequestDTO.email());
            verify(usuarioRepository, never()).save(any(Usuario.class));
        }
        @Test
        @DisplayName("Deve Retornar status 409 Conflict quando usuário já existe com esse email.")
        void deveLancarExcecaoConflitoEditarUsuario(){
            Long idUsusario = 1L;
            UsuarioRequestDTO usuarioRequestDTO = new UsuarioRequestDTO("marcos@gmail.com", "12345678", "Marcos", "Rua 10 - RJ");

            when(usuarioRepository.existsById(idUsusario)).thenReturn(true);
            when(usuarioRepository.existsByEmailFalse(usuarioRequestDTO.email())).thenReturn(false);
            when(usuarioService.buscaUsuario(idUsusario).email().equals(usuarioRequestDTO.email())).thenReturn(false);

            ResponseStatusException ex =
                    assertThrows(ResponseStatusException.class, () -> usuarioService.editarUsuario(usuarioRequestDTO, idUsusario));

            assertEquals(409, ex.getStatusCode().value());

            verify(usuarioRepository, times(1)).existsById(idUsusario);
            verify(usuarioRepository, times(1)).existsByEmailFalse(usuarioRequestDTO.email());
            verify(usuarioService, times(1)).buscaUsuario(idUsusario);
            verify(usuarioRepository, never()).save(any(Usuario.class));
        }
    }
    @Nested
    @DisplayName("Test de metodo deletar usuario")
    class DeletarUsuario {
        @Test
        @DisplayName("Deve Retornar status 204 No Content quando usuário deletado com sucesso.")
        void deveDeletarUsuario(){
            Long idUsusario = 1L;
            when(usuarioRepository.existsById(idUsusario)).thenReturn(true);
            doNothing().when(usuarioRepository).deleteById(idUsusario);

            usuarioService.removeUsuario(idUsusario);

            verify(usuarioRepository, times(1)).findById(idUsusario);
            verify(usuarioRepository, times(1)).deleteById(idUsusario);
        }
        @Test
        @DisplayName("Deve Retornar status 404 Not Found quando usuário não existe, para ser deletado.")
        void deveLancarExcecaoEntradaInvalidaDeletarUsuario(){
            Long idUsusario = 1L;
            when(usuarioRepository.existsById(idUsusario)).thenReturn(false);

            ResponseStatusException ex =
                    assertThrows(ResponseStatusException.class, () -> usuarioService.removeUsuario(idUsusario));

            assertEquals(404,ex.getStatusCode().value());

            verify(usuarioRepository, times(1)).findById(idUsusario);
            verify(usuarioRepository, never()).deleteById(idUsusario);
        }

    }
}
