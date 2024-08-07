package com.example.whatsapp.controllers;

import com.example.whatsapp.dtos.FriendShipDTO;
import com.example.whatsapp.responses.FriendShipResponse;
import com.example.whatsapp.services.FriendShipService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class FriendShipRealTimeController {
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final FriendShipService friendShipService;

    @MessageMapping("/send-friend-request/{uid}")
    public void sendFriendRequest(@DestinationVariable long uid, @Payload FriendShipDTO friendShipDTO){
        FriendShipResponse friendShipResponse = friendShipService.sendFriendRequest(friendShipDTO);
        simpMessagingTemplate.convertAndSend("/topic/friend-request/"
                + uid, friendShipResponse);
    }

    @MessageMapping("/cancel-friend-request/{uid}")
    public void cancelFriendRequest(@DestinationVariable long uid, @Payload FriendShipDTO friendShipDTO){
        friendShipService.cancelFriendRequest(friendShipDTO);
        simpMessagingTemplate.convertAndSend("/topic/friend-request/"
                + uid, "Xóa lời mời kết bạn thành công");
    }

    @MessageMapping("/accept-friend-request/{uid}")
    public void acceptFriendRequest(@DestinationVariable long uid, @Payload FriendShipDTO friendShipDTO){
        FriendShipResponse friendShipResponse = friendShipService.acceptFriendRequest(friendShipDTO);
        simpMessagingTemplate.convertAndSend("/topic/friend-request/"
                + uid, friendShipResponse);
    }
}
