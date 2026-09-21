package com.warlley.anotapedido_api.controller;

import com.warlley.anotapedido_api.dto.ProdutoRequestDTO;
import com.warlley.anotapedido_api.dto.ProdutoResponseDTO;
import com.warlley.anotapedido_api.service.ProdutoSevice;
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
@Tag(name = "Produtos", description = "Endpoints para gerenciar os Produtos.")
public class ProdutoController {

    private final ProdutoSevice produtoSevice;

    @Operation(summary = "Busca um produto.", description = "Recebe um id de um produto como parametro para buscar um produto.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Produto encontrado com sucesso."),
            @ApiResponse(responseCode = "404", description = "O ID informado não existe no banco de dados.."),
            @ApiResponse(responseCode = "401", description = "Unauthorized ao tentar cadastrar produto sem estar autenticado")
    })
    @GetMapping("/Produtos/{idProduto}")
    public ProdutoResponseDTO buscarProduto(@PathVariable Long idProduto){
        return produtoSevice.buscarProduto(idProduto);
    }

    @Operation(summary = "Cria um produto.", description = "Recebe um RequestProduto como parametro para criar um produto.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Produto criado com sucesso."),
            @ApiResponse(responseCode = "400", description = "parametros passado são inválidos."),
            @ApiResponse(responseCode = "409", description = "conflito: já existe um Produto cadastrado com esse nome."),
            @ApiResponse(responseCode = "401", description = "Unauthorized ao tentar cadastrar produto sem estar autenticado"),
            @ApiResponse(responseCode = "403", description = "Deve retornar 403 Forbidden ao tentar cadastrar produto sendo apenas USER")
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/Produtos")
    public ProdutoResponseDTO cadastrarProduto(@Valid @RequestBody ProdutoRequestDTO produtoRequestDTO){
        return produtoSevice.cadastrarProduto(produtoRequestDTO);
    }

    @Operation(summary = "Edita um produto.", description = "Recebe um RequestProduto um idProduto como parametro para criar um produto.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Produto atualizado com sucesso."),
            @ApiResponse(responseCode = "400", description = "parametros para atualizar o Produto invalido."),
            @ApiResponse(responseCode = "404", description = "Id passado como parametro não encontrado no banco de dados"),
            @ApiResponse(responseCode = "409", description = "conflito: já existe um Produto cadastrado com esse nome."),
            @ApiResponse(responseCode = "401", description = "Unauthorized ao tentar cadastrar produto sem estar autenticado"),
            @ApiResponse(responseCode = "403", description = "Deve retornar 403 Forbidden ao tentar cadastrar produto sendo apenas USER")})
    @PutMapping("/Produtos/{idProduto}")
    public ProdutoResponseDTO editarProduto(@Valid @RequestBody ProdutoRequestDTO produtoRequestDTO, @PathVariable Long idProduto){
        return produtoSevice.editarProduto(produtoRequestDTO, idProduto);
    }


    @Operation(summary = "Deleta um produto.", description = "Recebe um idProduto como parametro para deletar um produto.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Deletado com sucesso."),
            @ApiResponse(responseCode = "404", description = "Produto não existe para ser deletado."),
            @ApiResponse(responseCode = "409", description = "conflito: bloqueou porque o Produto está sendo usado por outra tabela."),
            @ApiResponse(responseCode = "401", description = "Unauthorized ao tentar cadastrar produto sem estar autenticado"),
            @ApiResponse(responseCode = "403", description = "Deve retornar 403 Forbidden ao tentar cadastrar produto sendo apenas USER")
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/Produtos/{idProduto}")
    public void removeProduto(@PathVariable Long idProduto){
        produtoSevice.removeProduto(idProduto);
    }
}
