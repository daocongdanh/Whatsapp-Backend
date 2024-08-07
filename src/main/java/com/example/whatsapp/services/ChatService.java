package com.example.whatsapp.services;

import com.example.whatsapp.dtos.ChatDTO;
import com.example.whatsapp.dtos.GroupChatDTO;
import com.example.whatsapp.models.Chat;
import com.example.whatsapp.responses.ChatResponse;

import java.util.List;

public interface ChatService {
    ChatResponse createChat(ChatDTO chatDTO);
    ChatResponse getChatById(Long id);
    ChatResponse createGroupChat(GroupChatDTO groupChatDTO);
    ChatResponse addMemberToGroupChat(long id, List<Long> users);
    ChatResponse removeMember(long id, long userId);
    ChatResponse transferTeamLeader(long id, long userId);
    Chat findById(long id);
}
