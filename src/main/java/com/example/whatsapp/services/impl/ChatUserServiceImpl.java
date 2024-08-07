package com.example.whatsapp.services.impl;

import com.example.whatsapp.dtos.ChatUserDTO;
import com.example.whatsapp.exceptions.GroupChatException;
import com.example.whatsapp.exceptions.ResourceNotFoundException;
import com.example.whatsapp.models.*;
import com.example.whatsapp.repositories.*;
import com.example.whatsapp.responses.ChatUserResponse;
import com.example.whatsapp.responses.MessageResponse;
import com.example.whatsapp.services.ChatUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ChatUserServiceImpl implements ChatUserService {
    private final ChatUserRepository chatUserRepository;
    private final ChatRepository chatRepository;
    private final UserRepository userRepository;
    private final MessageRepository messageRepository;
    private final MessageReadRepository messageReadRepository;
    @Override
    @Transactional
    public ChatUserResponse addUserToGroup(ChatUserDTO chatUserDTO) {
        User user = userRepository.findById(chatUserDTO.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy user"));
        Chat chat = chatRepository.findById(chatUserDTO.getChatId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy chat"));
        if(!chat.isGroup())
            throw new GroupChatException("Không phải group chat");
        ChatUser chatUser = ChatUser.builder()
                .addedAt(LocalDateTime.now())
                .user(user)
                .chat(chat)
                .build();
        chatUserRepository.save(chatUser);
        if(chatUser.getChat().isGroup()){
            Chat c = chatUser.getChat();
            return ChatUserResponse.fromChatUser(chatUser, c, null);
        }
        else{
            User u = chatUserRepository.findAllByChat(chatUser.getChat())
                    .stream()
                    .filter(cs -> cs.getUser().getId() != chatUser.getUser().getId())
                    .toList().get(0).getUser();
            return ChatUserResponse.fromChatUser(chatUser, u, null);
        }
    }

    @Override
    public List<ChatUserResponse> getAllChatsByUser(long uid) {
        User user = userRepository.findById(uid)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy user"));
        List<ChatUser> chatUsers = chatUserRepository.findAllByUser(user);
        List<ChatUserResponse> chatUserResponses = new ArrayList<>();
        for(ChatUser chatUser : chatUsers){
            List<Message> messages = messageRepository.findTop6MessageByChatAndUser(chatUser.getChat(), user);
            Message mess = messageRepository.findLastMessageByChat(chatUser.getChat());
            if(mess != null){
                if(messages.isEmpty() || mess.getId() != messages.get(0).getId()){
                    messages.add(0, mess);
                }
            }
            List<MessageResponse> messageResponses = messages.stream()
                    .map(message -> {
                        MessageRead messageRead = messageReadRepository.findByUserAndMessage(user, message);
                        return MessageResponse.fromMessage(message, messageRead == null || messageRead.isStatus());
                    })
                    .toList();
            if(chatUser.getChat().isGroup()){
                Chat c = chatUser.getChat();
                chatUserResponses.add(ChatUserResponse.fromChatUser(chatUser, c, messageResponses));
            }
            else{
                User u = chatUserRepository.findAllByChat(chatUser.getChat())
                        .stream()
                        .filter(cs -> !Objects.equals(cs.getUser().getId(), chatUser.getUser().getId()))
                        .toList().get(0).getUser();
                chatUserResponses.add(ChatUserResponse.fromChatUser(chatUser, u, messageResponses));
            }
        }
        return chatUserResponses;
    }


    @Override
    public List<ChatUser> getAllChatUsersByChat(long chatId) {
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new ResourceNotFoundException("Chat not found"));
        return chatUserRepository.findAllByChat(chat);
    }
}
