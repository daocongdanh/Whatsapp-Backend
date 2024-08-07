package com.example.whatsapp.services;

import com.example.whatsapp.dtos.ChatUserDTO;
import com.example.whatsapp.models.ChatUser;
import com.example.whatsapp.responses.ChatUserResponse;

import java.util.List;

public interface ChatUserService {
    ChatUserResponse addUserToGroup(ChatUserDTO chatUserDTO);
    List<ChatUserResponse> getAllChatsByUser(long uid);

    List<ChatUser> getAllChatUsersByChat(long chatId);
}
