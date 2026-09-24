package com.warlley.anotapedido_api.service;

import com.warlley.anotapedido_api.dto.UsuarioRequestDTO;
import com.warlley.anotapedido_api.dto.UsuarioResponseDTO;
import com.warlley.anotapedido_api.model.Usuario;
import com.warlley.anotapedido_api.model.enums.UserRole;
import com.warlley.anotapedido_api.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@RequiredArgsConstructor
@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioResponseDTO buscaUsuario(Long idUsuario){
        return new UsuarioResponseDTO(usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Usuário não encontrado.")));
    }
    public UsuarioResponseDTO cadastrarUsuario(UsuarioRequestDTO usuarioRequestDTO){
        Usuario usuarioNovo = new Usuario(usuarioRequestDTO);
        usuarioNovo.setSenha(passwordEncoder.encode(usuarioRequestDTO.senha()));
        usuarioNovo.setRole(UserRole.USER);
        if(usuarioRepository.existsByEmail(usuarioRequestDTO.email())){
            throw new ResponseStatusException(HttpStatus.CONFLICT,"Já existe um Usuário cadastrado com esse email.");
        }
        return new UsuarioResponseDTO(usuarioRepository.save(usuarioNovo));
        }

    public UsuarioResponseDTO editarUsuario(UsuarioRequestDTO usuarioRequestDTO, Long idUsuario){
        Usuario usuarioNovo = new Usuario(usuarioRequestDTO);
        if(usuarioRepository.existsById(idUsuario)){
            if(usuarioRepository.existsByEmail(usuarioRequestDTO.email())){
                if(usuarioRequestDTO.email().equals(buscaUsuario(idUsuario).email())){
                    return new UsuarioResponseDTO(usuarioRepository.save(usuarioNovo));
                }
                throw new ResponseStatusException(HttpStatus.CONFLICT,"Já existe um Usuário cadastrado com esse email.");
            }
            return new UsuarioResponseDTO(usuarioRepository.save(usuarioNovo));
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Usuário não encontrado, para ser editado.");
    }

    public void removeUsuario(Long idUsuario){
        if (usuarioRepository.existsById(idUsuario)){
            usuarioRepository.deleteById(idUsuario);
        }else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Usuário não encontrado, para ser deletado.");
        }
    }
}
