package com.warlley.anotapedido_api.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final SecurityFilter securityFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http){
        return http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize

                        // 1. Rotas Públicas (Documentação, Login e Cadastro)
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers(HttpMethod.GET,"/usuarios", "/usuarios/**").hasAnyRole("USER","ADMIN")
                        .requestMatchers(HttpMethod.POST,"/usuarios", "/usuarios/**").permitAll()
                        .requestMatchers(HttpMethod.PUT,"/usuarios", "/usuarios/**").hasAnyRole("USER","ADMIN")
                        .requestMatchers(HttpMethod.DELETE,"/usuarios", "/usuarios/**").hasAnyRole("USER","ADMIN")
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()

                        // 2. Rotas do Cardápio / Produtos (Apenas ADMIN altera; USER e ADMIN podem visualizar)
                        .requestMatchers(HttpMethod.GET,"/produtos", "/produtos/**").hasAnyRole("USER","ADMIN")
                        .requestMatchers(HttpMethod.POST,"/produtos", "/produtos/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT,"/produtos", "/produtos/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE,"/produtos", "/produtos/**").hasRole("ADMIN")

                        // 3. Rotas de Pedidos (Qualquer usuário autenticado pode criar e ver pedidos)
                        .requestMatchers(HttpMethod.GET,"/pedidos", "/pedidos/**").hasAnyRole("USER","ADMIN")
                        .requestMatchers(HttpMethod.POST,"/pedidos", "/pedidos/**").hasRole("USER")
                        .requestMatchers(HttpMethod.PUT,"/pedidos", "/pedidos/**").hasRole("USER")
                        .requestMatchers(HttpMethod.DELETE,"/pedidos", "/pedidos/**").hasRole("USER")

                        // LIBERE A ROTA DE ERRO DO SPRING:
                        .requestMatchers("/error").permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration){
        return configuration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Criptografa senhas no banco de dados
    }
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(List.of("*"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type", "Accept"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
