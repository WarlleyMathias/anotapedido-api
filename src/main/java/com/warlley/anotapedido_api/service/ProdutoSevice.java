package com.warlley.anotapedido_api.service;

import com.warlley.anotapedido_api.dto.ProdutoRequestDTO;
import com.warlley.anotapedido_api.dto.ProdutoResponseDTO;
import com.warlley.anotapedido_api.model.Produto;
import com.warlley.anotapedido_api.repository.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@RequiredArgsConstructor
@Service
public class ProdutoSevice {

    private final ProdutoRepository produtoRepository;

    public ProdutoResponseDTO buscarProduto(Long idProduto){
        return new ProdutoResponseDTO(produtoRepository.findById(idProduto)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Produto não encontrado.")));
    }
    public ProdutoResponseDTO cadastrarProduto(ProdutoRequestDTO produtoRequestDTO){
        Produto produtoNovo = new Produto(produtoRequestDTO);
        if (produtoRepository.existsByNome(produtoRequestDTO.nome())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,"Já existe um Produto com esse nome.");
        }
        return new ProdutoResponseDTO(produtoRepository.save(produtoNovo));
    }
    public ProdutoResponseDTO editarProduto(ProdutoRequestDTO produtoRequestDTO, Long idProduto){
        Produto produtoNovo = new Produto(produtoRequestDTO);
        if (produtoRepository.existsById(idProduto)){
            if(produtoRepository.existsByNome(produtoRequestDTO.nome())) {
                if (produtoRequestDTO.nome().equals(buscarProduto(idProduto).nome())) {
                    return new ProdutoResponseDTO(produtoRepository.save(produtoNovo));
                }
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Já existe um Produto com esse nome.");
            }
            return new ProdutoResponseDTO(produtoRepository.save(produtoNovo));
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Produto não encontrado, para editar.");
    }
    public void removeProduto(Long idProduto){
        if (produtoRepository.existsById(idProduto)){
            produtoRepository.deleteById(idProduto);
        }
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Produto não encontrado, para ser deletado.");

    }
    public Produto findId(Long idProduto){
        return produtoRepository.findById(idProduto)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Produto não encontrado."));

    }
}
