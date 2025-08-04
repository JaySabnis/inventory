package com.project.inventory.inventory.config;

import com.project.inventory.inventory.socket.ItemWebSocketHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;


@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final ItemWebSocketHandler handler;

    public WebSocketConfig(ItemWebSocketHandler handler) {
        this.handler = handler;
    }


    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(handler, "/ws/items").setAllowedOrigins("*");
    }
}
