package com.warlley.anotapedido_api.controller;

import com.warlley.anotapedido_api.dto.PedidoRequestDTO;
import com.warlley.anotapedido_api.dto.PedidoResponseDTO;
import com.warlley.anotapedido_api.service.PedidoService;
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
@Tag(name = "Pedidos", description = "Endpoints para gerenciar os Pedidos.")
public class PedidoController {

    private final PedidoService pedidoService;

    @Operation(summary = "Busca um Pedido.", description = "Recebe um id de um Pedido como parametro para buscar um Pedido.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pedido encontrado com sucesso."),
            @ApiResponse(responseCode = "404", description = "O ID informado não existe no banco de dados..")
    })
    @GetMapping("/Pedidos/{idPedido}")
    public PedidoResponseDTO buscarPedido(@PathVariable Long idPedido){
        return pedidoService.buscarPedido(idPedido);
    }

    @Operation(summary = "Cria um Pedido.", description = "Recebe um RequestPedido como parametro para criar um Pedido.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Pedido criado com sucesso."),
            @ApiResponse(responseCode = "400", description = "parametros passado são inválidos.")
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/Pedidos")
    public PedidoResponseDTO salvarPedido(@Valid @RequestBody PedidoRequestDTO pedidoRequestDTO){
        return pedidoService.salvarPedido(pedidoRequestDTO);
    }

    @Operation(summary = "Edita um Pedido.", description = "Recebe um RequestPedido e um idPedido como parametro para editar um Pedido.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pedido atualizado com sucesso."),
            @ApiResponse(responseCode = "400", description = "parametros para atualizar o Pedido inválido."),
            @ApiResponse(responseCode = "404", description = "Id passado como parametro não encontrado no banco de dados")})
    @PutMapping("/Pedidos/{idPedido}")
    public PedidoResponseDTO editarPedido(@Valid @RequestBody PedidoRequestDTO pedidoRequestDTO, @PathVariable Long idPedido){
        return pedidoService.editarPedido(pedidoRequestDTO, idPedido);
    }

    @Operation(summary = "Deleta um Pedido.", description = "Recebe um Pedido como parametro para deletar um Pedido.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Deletado com sucesso."),
            @ApiResponse(responseCode = "404", description = "Pedido não existe para ser deletado."),
            @ApiResponse(responseCode = "409", description = "conflito: bloqueou porque o Pedido está sendo usado por outra tabela.")
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/Pedidos/{idPedido}")
    public void removePedido(@PathVariable Long idPedido){
        pedidoService.removePedido(idPedido);
    }
}
