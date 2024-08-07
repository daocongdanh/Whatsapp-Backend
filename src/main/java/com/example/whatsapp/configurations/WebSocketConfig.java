package com.example.whatsapp.configurations;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // stompClient.subscribe('/topic/public', function (message) -> @SendTo("/topic/public")
        // Client đăng ký lắng nghe một topic từ server
        config.enableSimpleBroker("/topic");

        // stompClient.send('/app/sendMessage', {}, JSON.stringify(message)) -> @MessageMapping("/sendMessage")
        // Client gửi tin nhắn đến server
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/chat") // Client kết nối đến server
                // const socket = new SockJS('http://localhost:8080/chat');
                .setAllowedOrigins("http://localhost:3000").withSockJS();
    }
}
