package com.example.whatsapp.services.impl;

import com.example.whatsapp.dtos.MessageDTO;
import com.example.whatsapp.exceptions.ResourceNotFoundException;
import com.example.whatsapp.models.*;
import com.example.whatsapp.repositories.*;
import com.example.whatsapp.responses.MessageResponse;
import com.example.whatsapp.services.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {
    private final MessageRepository messageRepository;
    private final ChatRepository chatRepository;
    private final UserRepository userRepository;
    private final ChatUserRepository chatUserRepository;
    private final MessageReadRepository messageReadRepository;
    @Override
    @Transactional
    public MessageResponse sendMessage(MessageDTO messageDTO) {
        Chat chat = chatRepository.findById(messageDTO.getChatId())
                .orElseThrow(() -> new ResourceNotFoundException("Chat not found"));
        List<ChatUser> chatUsers = chatUserRepository.findAllByChat(chat);
        for(ChatUser chatUser : chatUsers){
            chatUser.setLastTime(LocalDateTime.now());
        }
        chatUserRepository.saveAll(chatUsers);
        User user = userRepository.findById(messageDTO.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Message message = Message.builder()
                .content(messageDTO.getContent())
                .isImage(messageDTO.isImage())
                .timeStamp(LocalDateTime.now())
                .user(user)
                .chat(chat)
                .build();
        messageRepository.save(message);
        for(ChatUser chatUser : chatUsers){
            boolean status = Objects.equals(chatUser.getUser().getId(), message.getUser().getId());
            MessageRead messageRead = MessageRead.builder()
                    .message(message)
                    .user(chatUser.getUser())
                    .status(status)
                    .readTime(LocalDateTime.now())
                    .build();
            messageReadRepository.save(messageRead);
        }
        return MessageResponse.fromMessage(message,true);
    }

    @Override
    public List<MessageResponse> getAllMessagesByChat(Long chatId) {
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new ResourceNotFoundException("Chat not found"));
        return messageRepository.findAllByChat(chat)
                .stream()
                .map(message -> MessageResponse.fromMessage(message, true))
                .toList();
    }

    @Override
    @Transactional
    public void updateAllMessageStatus(long chatId, long userId) {
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new ResourceNotFoundException("Chat not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        List<Message> messages = messageRepository.findTop6MessageByChatAndUser(chat, user);
        for(Message message : messages){
            MessageRead messageRead = messageReadRepository.findByUserAndMessage(user, message);
            messageRead.setStatus(true);
            messageReadRepository.save(messageRead);
        }
    }
}
