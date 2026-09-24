package com.warlley.anotapedido_api.dto;


public record TokenResponseDTO(
        String token,
        String type,
        String nome,
        String email,
        String role
) {
    // Construtor auxiliar simplificado com o tipo padrão "Bearer"
    public TokenResponseDTO(String token, String nome, String email, String role) {
        this(token, "Bearer", nome, email, role);
    }
}
