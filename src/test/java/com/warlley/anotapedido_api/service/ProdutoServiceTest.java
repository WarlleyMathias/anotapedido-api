package com.warlley.anotapedido_api.service;

import com.warlley.anotapedido_api.dto.ProdutoRequestDTO;
import com.warlley.anotapedido_api.dto.ProdutoResponseDTO;
import com.warlley.anotapedido_api.model.Produto;
import com.warlley.anotapedido_api.repository.ProdutoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProdutoServiceTest {

    @Mock
    ProdutoRepository produtoRepository;

    @InjectMocks
    ProdutoSevice produtoSevice;

    @Nested
    @DisplayName("Test de metodo buscar Produto.")
    class buscarProduto{
        @Test
        @DisplayName("Deve Retornar status 200 OK e produto.")
        void deveBuscarProduto(){
            Long idProduto = 1L;
            Produto produtoBuscado = new Produto(1L,"Pizza",10.0f);
            when(produtoRepository.existsById(idProduto)).thenReturn(true);
            when(produtoRepository.findById(idProduto)).thenReturn(Optional.of(produtoBuscado));

            ProdutoResponseDTO resultado = produtoSevice.buscarProduto(idProduto);

            assertNotNull(resultado);
            assertEquals(1L,resultado.id());
            assertEquals("Pizza",resultado.nome());
            assertEquals(10.0f,resultado.valor());

            verify(produtoRepository,times(1)).existsById(idProduto);
            verify(produtoRepository,times(1)).findById(idProduto);
        }
        @Test
        @DisplayName("Deve Retornar status 404 quando produto não existir.")
        void deveLancarExcecaoBuscarProduto(){
            Long idProduto = 1L;
            when(produtoRepository.existsById(idProduto)).thenReturn(false);

            ResponseStatusException ex =
                    assertThrows(ResponseStatusException.class, () -> produtoSevice.buscarProduto(idProduto));

            assertEquals(404,ex.getStatusCode().value());

            verify(produtoRepository,times(1)).existsById(idProduto);
            verify(produtoRepository,never()).findById(idProduto);
        }

    }
    @Nested
    @DisplayName("Test de metodo criar Produto.")
    class criarProduto{
        @Test
        @DisplayName("Deve Retornar status 201 Created e produto.")
        void deveCadastrarProduto(){
            ProdutoRequestDTO produtoRequestDTO = new ProdutoRequestDTO("Pizza",10.0f);
            Produto produtoSalvo = new Produto(1L,"Pizza",10.0f);
            when(produtoRepository.existsByNomeFalse(produtoRequestDTO.nome())).thenReturn(true);
            when(produtoRepository.save(produtoSalvo));

            ProdutoResponseDTO resultado = produtoSevice.cadastrarProduto(produtoRequestDTO);

            assertNotNull(resultado);
            assertEquals(1L,resultado.id());
            assertEquals("Pizza",resultado.nome());
            assertEquals(10.0f,resultado.valor());

            verify(produtoRepository,times(1)).existsByNomeFalse(produtoRequestDTO.nome());
            verify(produtoRepository,times(1)).save(produtoSalvo);
        }
        @Test
        @DisplayName("Deve Retornar status 409 Conflict quando produto já existe.")
        void deveLancarExcecaoConflitoCadastrarProduto(){
            ProdutoRequestDTO produtoRequestDTO = new ProdutoRequestDTO("Pizza",10.0f);
            Produto produtoSalvo = new Produto(1L,"Pizza",10.0f);
            when(produtoRepository.existsByNomeFalse(produtoRequestDTO.nome())).thenReturn(false);

            ResponseStatusException ex =
                    assertThrows(ResponseStatusException.class, () -> produtoSevice.cadastrarProduto(produtoRequestDTO));

            assertEquals(409,ex.getStatusCode().value());

            verify(produtoRepository,times(1)).existsByNomeFalse(produtoRequestDTO.nome());
            verify(produtoRepository,never()).save(produtoSalvo);
        }

    }
    @Nested
    @DisplayName("Test de metodo editar Produto.")
    class editarProduto{
        @Test
        @DisplayName("Deve Retornar status 200 OK quando produto atualizado com sucesso.")
        void deveEditarProduto(){
            Long idProduto = 1L;
            ProdutoRequestDTO produtoRequestDTO = new ProdutoRequestDTO("Pizza",10.0f);
            Produto produtoNovo = new Produto(1L,"Pizza",10.0f);
            when(produtoRepository.existsById(idProduto)).thenReturn(true);
            when(produtoRepository.existsByNomeFalse(produtoRequestDTO.nome())).thenReturn(true);
            when(produtoRepository.save(produtoNovo));

            ProdutoResponseDTO resultado = produtoSevice.editarProduto(produtoRequestDTO,idProduto);

            assertNotNull(resultado);
            assertEquals(1L,resultado.id());
            assertEquals("Pizza",resultado.nome());
            assertEquals(10.0f,resultado.valor());

            verify(produtoRepository,times(1)).existsById(idProduto);
            verify(produtoRepository,times(1)).existsByNomeFalse(produtoRequestDTO.nome());
            verify(produtoRepository,times(1)).save(produtoNovo);
        }
        @Test
        @DisplayName("Deve Retornar status 404 Not Found quando produto não existir.")
        void deveLancarExcecaoEditarProduto(){
            Long idProduto = 1L;
            when(produtoRepository.existsById(idProduto)).thenReturn(false);

            ResponseStatusException ex =
                    assertThrows(ResponseStatusException.class, ()-> produtoSevice.editarProduto(any(ProdutoRequestDTO.class),idProduto));

            assertEquals(404,ex.getStatusCode().value());

            verify(produtoRepository,times(1)).existsById(idProduto);
            verify(produtoRepository,never()).existsByNomeFalse(any(ProdutoRequestDTO.class).nome());
            verify(produtoRepository,never()).save(any(Produto.class));
        }
        @Test
        @DisplayName("Deve Retornar status 409 Conflict quando produto já existe com esse nome.")
        void deveLancarExcecaoConflitoEditarProduto(){
            Long idProduto = 1L;
            ProdutoRequestDTO produtoRequestDTO = new ProdutoRequestDTO("Pizza",10.0f);
            when(produtoRepository.existsById(idProduto)).thenReturn(true);
            when(produtoSevice.buscarProduto(idProduto).nome().equals(produtoRequestDTO.nome())).thenReturn(false);
            when(produtoRepository.existsByNomeFalse(produtoRequestDTO.nome())).thenReturn(false);


            ResponseStatusException ex =
                    assertThrows(ResponseStatusException.class, ()-> produtoSevice.editarProduto(any(ProdutoRequestDTO.class),idProduto));

            assertEquals(409,ex.getStatusCode().value());

            verify(produtoRepository,times(1)).existsById(idProduto);
            verify(produtoRepository,times(1)).existsByNomeFalse(produtoRequestDTO.nome());
            verify(produtoRepository,never()).save(any(Produto.class));
        }

    }
    @Nested
    @DisplayName("Test de metodo deletar Produto.")
    class deletarProduto{
        @Test
        @DisplayName("Deve Retornar status 204 No Content quando produto deletado com sucesso.")
        void deveDeletarProduto(){
            Long idProduto = 1L;
            when(produtoRepository.existsById(idProduto)).thenReturn(true);
            doNothing().when(produtoRepository).deleteById(idProduto);

            produtoSevice.removeProduto(idProduto);

            verify(produtoRepository,times(1)).existsById(idProduto);
            verify(produtoRepository,times(1)).deleteById(idProduto);
        }
        @Test
        @DisplayName("Deve Retornar status 404 Not Found quando produto não existe, para ser deletado.")
        void deveLancarExcecaoEntradaInvalidaDeletarProduto(){
            Long idProduto = 1L;
            when(produtoRepository.existsById(idProduto)).thenReturn(false);

            ResponseStatusException ex =
                    assertThrows(ResponseStatusException.class, ()-> produtoSevice.removeProduto(idProduto));

            assertEquals(404,ex.getStatusCode().value());

            verify(produtoRepository,times(1)).existsById(idProduto);
            verify(produtoRepository,never()).deleteById(idProduto);
        }
    }
}
