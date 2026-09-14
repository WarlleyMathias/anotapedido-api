package com.warlley.anotapedido_api.controller;

import com.warlley.anotapedido_api.dto.ProdutoRequestDTO;
import com.warlley.anotapedido_api.dto.ProdutoResponseDTO;
import com.warlley.anotapedido_api.service.ProdutoSevice;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class ProdutoController {

    private final ProdutoSevice produtoSevice;

    @GetMapping("/Produtos/{idProduto}")
    public ProdutoResponseDTO buscarProduto(@PathVariable Long idProduto){
        return produtoSevice.buscarProduto(idProduto);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/Produtos")
    public ProdutoResponseDTO cadastrarProduto(@Valid @RequestBody ProdutoRequestDTO produtoRequestDTO){
        return produtoSevice.cadastrarProduto(produtoRequestDTO);
    }

    @PutMapping("/Produtos/{idProduto}")
    public ProdutoResponseDTO editarProduto(@Valid @RequestBody ProdutoRequestDTO produtoRequestDTO, @PathVariable Long idProduto){
        return produtoSevice.editarProduto(produtoRequestDTO, idProduto);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/Produtos/{idProduto}")
    public void removeProduto(@PathVariable Long idProduto){
        produtoSevice.removeProduto(idProduto);
    }
}
