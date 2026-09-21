package com.warlley.anotapedido_api.service;

import com.warlley.anotapedido_api.model.Usuario;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class TokenService {

    @Value("${api.security.token.secret:minha-chave-secreta-muito-segura-123456}")
    private String secret;

    // Gera a SecretKey utilizando o novo formato
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String gerarToken(Usuario usuario) {
        return Jwts.builder()
                .subject(usuario.getEmail()) // Antes era setSubject()
                .issuedAt(new Date()) // Antes era setIssuedAt()
                .expiration(new Date(System.currentTimeMillis() + 86400000)) // Antes era setExpiration() (24h)
                .signWith(getSigningKey()) // Não precisa passar o algoritmo explicitamente se a chave já for compatível
                .compact();
    }

    public String validarToken(String token) {
        try {
            return Jwts.parser() // Antes era parserBuilder()
                    .verifyWith(getSigningKey()) // Antes era setSigningKey()
                    .build()
                    .parseSignedClaims(token) // Antes era parseClaimsJws()
                    .getPayload() // Antes era getBody()
                    .getSubject();
        } catch (Exception e) {
            return null; // Token inválido ou expirado
        }
    }
}