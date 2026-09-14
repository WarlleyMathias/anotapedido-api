package com.warlley.anotapedido_api.service;

import com.warlley.anotapedido_api.model.Produto;
import com.warlley.anotapedido_api.repository.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@RequiredArgsConstructor
@Service
public class ProdutoSevice {

    private final ProdutoRepository produtoRepository;

    public Produto cadastrarProduto(Produto novoProduto){
        return produtoRepository.save(novoProduto);
    }
    public Produto buscarProduto(Long idProduto){
        return produtoRepository.findById(idProduto)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Produto não encontrado."));
    }
    public Produto editarProduto(Produto novoProduto){
        if (produtoRepository.existsById(novoProduto.getId())){
            return produtoRepository.save(novoProduto);
        }
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Produto não encontrado, para editar.");
    }
    public void removeProduto(Long idProduto){
        if (produtoRepository.existsById(idProduto)){
            produtoRepository.deleteById(idProduto);
        }
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Produto não encontrado, para ser deletado.");

    }
}
