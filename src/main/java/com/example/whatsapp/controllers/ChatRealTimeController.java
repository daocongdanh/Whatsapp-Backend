package com.example.whatsapp.controllers;

import com.example.whatsapp.dtos.GroupChatDTO;
import com.example.whatsapp.dtos.MessageDTO;
import com.example.whatsapp.models.ChatUser;;
import com.example.whatsapp.responses.ChatResponse;
import com.example.whatsapp.responses.MessageResponse;
import com.example.whatsapp.services.ChatService;
import com.example.whatsapp.services.ChatUserService;
import com.example.whatsapp.services.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ChatRealTimeController {
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final MessageService messageService;
    private final ChatUserService chatUserService;
    private final ChatService chatService;
//    @MessageMapping("/sendMessage")
//    @SendTo("/topic/public")
//    public ChatMessage sendMessage(ChatMessage chatMessage) {
//        return chatMessage;
//    }

    @MessageMapping("/sendMessage/{chatId}")
    public void sendMessage(@DestinationVariable Long chatId, @Payload MessageDTO messageDTO){
        MessageResponse messageResponse = messageService.sendMessage(messageDTO);
        List<ChatUser> chatUsers = chatUserService.getAllChatUsersByChat(messageDTO.getChatId())
                        .stream()
                        .filter(chatUser ->
                                !Objects.equals(chatUser.getUser().getId(), messageDTO.getUserId()))
                        .toList();
        simpMessagingTemplate.convertAndSend("/topic/chat/"
                + messageDTO.getChatId(), messageResponse);

        // Tạo thread để chạy song song
        ExecutorService executorService = Executors.newFixedThreadPool(chatUsers.size());
        for (ChatUser chatUser : chatUsers) {
            CompletableFuture.runAsync(() -> {
                simpMessagingTemplate.convertAndSend("/topic/notification/"
                        + chatUser.getUser().getId(), messageResponse);
            }, executorService);
        }
        executorService.shutdown();
//        for (ChatUser chatUser : chatUsers) {
//            simpMessagingTemplate.convertAndSend("/topic/notification/"
//                        + chatUser.getUser().getId(), messageResponse);
//        }
//        simpMessagingTemplate.convertAndSend("/topic/sendMessage/" +
//                messageDTO.getUserId(), "Success");
    }
    @MessageMapping("/create-group")
    public void createGroup(@Payload GroupChatDTO groupChatDTO){
        ChatResponse chatResponse = chatService.createGroupChat(groupChatDTO);
        ExecutorService executorService = Executors.newFixedThreadPool(groupChatDTO.getUsers().size());
        List<Long> users = groupChatDTO.getUsers();
        users.add(0,groupChatDTO.getUserId());
        for (Long userId : users) {
            CompletableFuture.runAsync(() -> {
                simpMessagingTemplate.convertAndSend("/topic/create-group/"
                        + userId, chatResponse);
            }, executorService);
        }
        executorService.shutdown();
    }

    @MessageMapping("/add-members/{chatId}")
    public void addMemberToGroupChat(@DestinationVariable Long chatId, @Payload Map<String, List<Long>> payload){
        List<Long> users = payload.get("users");
        ChatResponse chatResponse = chatService.addMemberToGroupChat(chatId, users);
        ExecutorService executorService = Executors.newFixedThreadPool(users.size());
        for (Long userId : users) {
            CompletableFuture.runAsync(() -> {
                simpMessagingTemplate.convertAndSend("/topic/create-group/"
                        + userId, chatResponse);
            }, executorService);
        }
        executorService.shutdown();
    }

    @MessageMapping("remove-member/{chatId}/{userId}")
    public void removeMember(@DestinationVariable Long chatId,
                             @DestinationVariable Long userId){
        ChatResponse chatResponse = chatService.removeMember(chatId, userId);

        // Send cho user bị xóa
        simpMessagingTemplate.convertAndSend("/topic/remove-member/"
                + userId, chatResponse);

        // Sen cho trưởng nhóm
        simpMessagingTemplate.convertAndSend("/topic/remove-member/"
                + chatResponse.getUserId(), "Success");
    }
}
