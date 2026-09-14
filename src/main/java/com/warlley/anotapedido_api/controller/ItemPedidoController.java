package com.warlley.anotapedido_api.controller;

import com.warlley.anotapedido_api.service.ItemPedidoSevice;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class ItemPedidoController {

    private final ItemPedidoSevice itemPedidoSevice;
}
