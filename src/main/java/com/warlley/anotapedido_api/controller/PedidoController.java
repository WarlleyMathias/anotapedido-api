package com.warlley.anotapedido_api.controller;

import com.warlley.anotapedido_api.service.PedidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class PedidoController {

    private final PedidoService pedidoService;

}
