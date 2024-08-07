package com.example.whatsapp.services;

import com.example.whatsapp.dtos.MessageDTO;
import com.example.whatsapp.responses.MessageResponse;

import java.util.List;

public interface MessageService {
    MessageResponse sendMessage(MessageDTO messageDTO);
    List<MessageResponse> getAllMessagesByChat(Long chatId);
    void updateAllMessageStatus(long chatId, long userId);
}
