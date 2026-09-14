package com.warlley.anotapedido_api.controller;

import com.warlley.anotapedido_api.model.Usuario;
import com.warlley.anotapedido_api.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class UsuarioController {

    private final UsuarioService usuarioService;

    public Usuario cadastrarUsuario(Usuario novoUsuario){
        return usuarioService.cadastrarUsuario(novoUsuario);
    }

    public Usuario buscarUsuario(Long idUsuario){
        return usuarioService.buscaUsuario(idUsuario);
    }

    public void removeUsuario(Long idUsuario){
        usuarioService.removeUsuario(idUsuario);
    }
}
