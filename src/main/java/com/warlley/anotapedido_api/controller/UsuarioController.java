package com.warlley.anotapedido_api.controller;

import com.warlley.anotapedido_api.dto.UsuarioRequestDTO;
import com.warlley.anotapedido_api.dto.UsuarioResponseDTO;
import com.warlley.anotapedido_api.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class UsuarioController {

    private final UsuarioService usuarioService;

    @GetMapping("/Usuarios/{idUsuario}")
    public UsuarioResponseDTO buscarUsuario(@PathVariable Long idUsuario){
        return usuarioService.buscaUsuario(idUsuario);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/Usuarios")
    public UsuarioResponseDTO cadastrarUsuario(@Valid @RequestBody UsuarioRequestDTO usuarioRequestDTO){
        return usuarioService.cadastrarUsuario(usuarioRequestDTO);
    }

    @PutMapping("/Usuarios/{idUsuario}")
    public UsuarioResponseDTO editarUsuario(@Valid @RequestBody UsuarioRequestDTO usuarioRequestDTO, @PathVariable Long idUsuario){
        return usuarioService.editarUsuario(usuarioRequestDTO, idUsuario);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/Usuarios/{idUsuario}")
    public void removeUsuario(@PathVariable Long idUsuario){
        usuarioService.removeUsuario(idUsuario);
    }
}
