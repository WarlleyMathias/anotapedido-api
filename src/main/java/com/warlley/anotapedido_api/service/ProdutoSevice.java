package com.warlley.anotapedido_api.service;

import com.warlley.anotapedido_api.dto.ProdutoRequestDTO;
import com.warlley.anotapedido_api.dto.ProdutoResponseDTO;
import com.warlley.anotapedido_api.model.Produto;
import com.warlley.anotapedido_api.repository.ProdutoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ProdutoSevice {

    private final ProdutoRepository produtoRepository;
    private final CategoriaService categoriaService;

    public List<ProdutoResponseDTO> buscarTodosProduto(){
        List<Produto> produtosList = produtoRepository.findAll();
        List<ProdutoResponseDTO> produtoResponseDTOList = new ArrayList<>();
        for(Produto produto : produtosList){
            produtoResponseDTOList.add(new ProdutoResponseDTO(produto));
        }
        return produtoResponseDTOList;
    }

    public ProdutoResponseDTO buscarProduto(Long idProduto){
        return new ProdutoResponseDTO(produtoRepository.findById(idProduto)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Produto não encontrado.")));
    }
    @Transactional
    public ProdutoResponseDTO cadastrarProduto(ProdutoRequestDTO produtoRequestDTO){
        Produto produtoNovo = new Produto(produtoRequestDTO);
        produtoNovo.setCategoria(categoriaService.buscarCategoria(produtoRequestDTO.categoriaId()));
        if (produtoRepository.existsByNome(produtoRequestDTO.nome())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,"Já existe um Produto com esse nome.");
        }
        return new ProdutoResponseDTO(produtoRepository.save(produtoNovo));
    }
    @Transactional
    public ProdutoResponseDTO editarProduto(ProdutoRequestDTO produtoRequestDTO, Long idProduto){
        Produto produtoNovo = new Produto(produtoRequestDTO);
        produtoNovo.setCategoria(categoriaService.buscarCategoria(produtoRequestDTO.categoriaId()));
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
