package com.warlley.anotapedido_api.controller;

import com.warlley.anotapedido_api.model.Produto;
import com.warlley.anotapedido_api.service.ProdutoSevice;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class ProdutoController {

    private final ProdutoSevice produtoSevice;

    public Produto buscarProduto(Long idProduto){
        return produtoSevice.buscarProduto(idProduto);
    }
    public Produto cadastrarProduto(Produto produto){
        return produtoSevice.cadastrarProduto(produto);
    }
    public Produto editarProduto(Produto produto){
        return produtoSevice.editarProduto(produto);
    }
    public void removeProduto(Long idProduto){
        produtoSevice.removeProduto(idProduto);
    }
}
