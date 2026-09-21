package com.warlley.anotapedido_api.controller;

import com.warlley.anotapedido_api.dto.UsuarioRequestDTO;
import com.warlley.anotapedido_api.dto.UsuarioResponseDTO;
import com.warlley.anotapedido_api.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@Tag(name = "Usuarios", description = "Endpoints para gerenciar os Usuários.")
public class UsuarioController {

    private final UsuarioService usuarioService;

    @Operation(summary = "Busca um Usuario.", description = "Recebe um id de um Ususário como parametro para buscar um Usuário.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuário encontrado com sucesso."),
            @ApiResponse(responseCode = "404", description = "O ID informado não existe no banco de dados.."),
            @ApiResponse(responseCode = "401", description = "Unauthorized ao tentar cadastrar produto sem estar autenticado")
    })
    @GetMapping("/Usuarios/{idUsuario}")
    public UsuarioResponseDTO buscarUsuario(@PathVariable Long idUsuario){
        return usuarioService.buscaUsuario(idUsuario);
    }

    @Operation(summary = "Cria um Usuário.", description = "Recebe um RequestUsuario como parametro para criar um Usuário.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso."),
            @ApiResponse(responseCode = "400", description = "parametros passado são inválidos."),
            @ApiResponse(responseCode = "409", description = "conflito: já existe um Usuário cadastrado com esse email."),
            @ApiResponse(responseCode = "401", description = "Unauthorized ao tentar cadastrar produto sem estar autenticado")
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/Usuarios")
    public UsuarioResponseDTO cadastrarUsuario(@Valid @RequestBody UsuarioRequestDTO usuarioRequestDTO){
        return usuarioService.cadastrarUsuario(usuarioRequestDTO);
    }

    @Operation(summary = "Edita um Usuário.", description = "Recebe um RequestUsuario e um idUsuario como parametro para editar um Usuário.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuário atualizado com sucesso."),
            @ApiResponse(responseCode = "400", description = "parametros para atualizar o Usuário inválido."),
            @ApiResponse(responseCode = "404", description = "Id passado como parametro não encontrado no banco de dados"),
            @ApiResponse(responseCode = "409", description = "conflito: já existe um Usuário cadastrado com esse nome."),
            @ApiResponse(responseCode = "401", description = "Unauthorized ao tentar cadastrar produto sem estar autenticado")})
    @PutMapping("/Usuarios/{idUsuario}")
    public UsuarioResponseDTO editarUsuario(@Valid @RequestBody UsuarioRequestDTO usuarioRequestDTO, @PathVariable Long idUsuario){
        return usuarioService.editarUsuario(usuarioRequestDTO, idUsuario);
    }

    @Operation(summary = "Deleta um Usuário.", description = "Recebe um Usuário como parametro para deletar um Usuário.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Deletado com sucesso."),
            @ApiResponse(responseCode = "404", description = "Usuário não existe para ser deletado."),
            @ApiResponse(responseCode = "409", description = "conflito: bloqueou porque o Usuário está sendo usado por outra tabela."),
            @ApiResponse(responseCode = "401", description = "Unauthorized ao tentar cadastrar produto sem estar autenticado")
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/Usuarios/{idUsuario}")
    public void removeUsuario(@PathVariable Long idUsuario){
        usuarioService.removeUsuario(idUsuario);
    }
}
