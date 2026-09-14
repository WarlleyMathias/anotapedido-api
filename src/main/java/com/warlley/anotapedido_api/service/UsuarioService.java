package com.warlley.anotapedido_api.service;

import com.warlley.anotapedido_api.model.Usuario;
import com.warlley.anotapedido_api.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@RequiredArgsConstructor
@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public Usuario cadastrarUsuario(Usuario novoUsuario){
        return usuarioRepository.save(novoUsuario);
    }

    public Usuario buscaUsuario(Long idUsuario){
        return usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Usuário não encontrado."));
    }

    public void removeUsuario(Long idUsuario){
        if (usuarioRepository.existsById(idUsuario)){
            usuarioRepository.deleteById(idUsuario);
        }else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Usuário não encontrado, para ser deletado.");
        }
    }
}
