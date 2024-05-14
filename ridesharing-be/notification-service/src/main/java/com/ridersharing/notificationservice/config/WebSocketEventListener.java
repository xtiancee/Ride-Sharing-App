package com.ridersharing.notificationservice.config;

import com.ridesharing.core.dto.ClientDisconnectDto;
import com.ridesharing.core.utils.Constants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
@RequiredArgsConstructor
@Slf4j
public class WebSocketEventListener {

    private final SimpMessageSendingOperations messageTemplate;

    private final KafkaTemplate<String, ClientDisconnectDto> kafkaTemplate;

   @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {

        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String username = headerAccessor.getUser().getName();

        System.out.println("User " + username + " disconnected.");

        var clientDisconnectedRequest = ClientDisconnectDto.builder().clientId(username).build();

       Message<ClientDisconnectDto> msg = MessageBuilder
               .withPayload(clientDisconnectedRequest)
               .setHeader(KafkaHeaders.TOPIC, Constants.CLIENT_DISCONNECTS_TOPIC)
               .build();

       System.out.println("Notifying Rider Service ");

             kafkaTemplate.send(msg);
        }
    }
