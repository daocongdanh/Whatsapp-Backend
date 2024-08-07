package com.example.whatsapp.services.impl;

import com.example.whatsapp.dtos.ChatDTO;
import com.example.whatsapp.dtos.GroupChatDTO;
import com.example.whatsapp.exceptions.GroupChatException;
import com.example.whatsapp.exceptions.ResourceNotFoundException;
import com.example.whatsapp.models.Chat;
import com.example.whatsapp.models.ChatUser;
import com.example.whatsapp.models.Message;
import com.example.whatsapp.models.User;
import com.example.whatsapp.repositories.ChatRepository;
import com.example.whatsapp.repositories.ChatUserRepository;
import com.example.whatsapp.repositories.MessageReadRepository;
import com.example.whatsapp.repositories.MessageRepository;
import com.example.whatsapp.responses.ChatResponse;
import com.example.whatsapp.responses.UserResponse;
import com.example.whatsapp.services.ChatService;
import com.example.whatsapp.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {
    private final ChatRepository chatRepository;
    private final UserService userService;
    private final ChatUserRepository chatUserRepository;
    private final MessageRepository messageRepository;
    private final MessageReadRepository messageReadRepository;
    @Override
    @Transactional
    public ChatResponse createChat(ChatDTO chatDTO) {
        User user = null;
        if(chatDTO.getUserId() != null){
            user = userService.findById(chatDTO.getUserId());
        }
        Chat chat = Chat.builder()
                .name(chatDTO.getName())
                .image(chatDTO.getImage())
                .isGroup(chatDTO.isGroup())
                .user(user)
                .build();
        chatRepository.save(chat);
        List<UserResponse> users = chatUserRepository.findAllByChat(chat)
                .stream()
                .map(ChatUser::getUser)
                .map(UserResponse::fromUser)
                .toList();
        return ChatResponse.fromChat(chat, users);
    }

    @Override
    public ChatResponse getChatById(Long id) {
        Chat chat = chatRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Chat"));
        List<UserResponse> userResponses = new ArrayList<>();
        if(chat.isGroup()){
            List<User> users = new ArrayList<>(chatUserRepository.findAllByChat(chat)
                    .stream()
                    .map(ChatUser::getUser)
                    .filter(user -> !Objects.equals(user.getId(), chat.getUser().getId()))
                    .toList());
            users.add(0, chat.getUser());
            userResponses = users.stream().map(UserResponse::fromUser).toList();
        }
        else{
            userResponses = chatUserRepository.findAllByChat(chat)
                    .stream()
                    .map(ChatUser::getUser)
                    .map(UserResponse::fromUser)
                    .toList();
        }

        return ChatResponse.fromChat(chat, userResponses);
    }

    @Override
    @Transactional
    public ChatResponse createGroupChat(GroupChatDTO groupChatDTO) {
        User user = userService.findById(groupChatDTO.getUserId());
        Chat chat = Chat.builder()
                .name(groupChatDTO.getName())
                .isGroup(true)
                .image(groupChatDTO.getImage() == null ?
                        "https://www.pinclipart.com/picdir/middle/58-585935_memberships-computer-avatar-clipart.png"
                        : groupChatDTO.getImage())
                .user(user)
                .build();
        chatRepository.save(chat);
        List<User> users = new ArrayList<>();
        List<ChatUser> chatUsers = new ArrayList<>();
        chatUsers.add(ChatUser.builder()
                .chat(chat)
                .user(user)
                .addedAt(LocalDateTime.now())
                .lastTime(LocalDateTime.now())
                .build());
        for(Long userId : groupChatDTO.getUsers()){
            User existsUser = userService.findById(userId);
            ChatUser chatUser = ChatUser.builder()
                    .chat(chat)
                    .user(existsUser)
                    .addedAt(LocalDateTime.now())
                    .lastTime(LocalDateTime.now())
                    .build();
            chatUsers.add(chatUser);
            users.add(existsUser);
        }
        chatUserRepository.saveAll(chatUsers);
        for(User u : users){
            String mess = "";
            mess += "<span style=\"color: #495057; font-size: 13px; font-weight: 500; margin-right: 5px;\">" +
                    u.getFullName() + "</span>";
            mess += "<span style=\"color: gray; font-size: 13px;\">đã tham gia nhóm</span>";
            Message message = Message.builder()
                    .user(u)
                    .chat(chat)
                    .isImage(false)
                    .isSystem(true)
                    .timeStamp(LocalDateTime.now())
                    .content(mess)
                    .build();
            messageRepository.save(message);
        }
        return ChatResponse.fromChat(chat, users.stream().map(UserResponse::fromUser)
                .collect(Collectors.toList()));
    }

    @Override
    @Transactional
    public ChatResponse addMemberToGroupChat(long id, List<Long> users) {
        Chat chat = findById(id);
        List<ChatUser> chatUsers = chatUserRepository.findAllByChat(chat);
        for(Long userId : users){
            User user = userService.findById(userId);
            User existsUserGroup = chatUsers.stream()
                    .map(ChatUser::getUser)
                    .filter(u -> Objects.equals(u.getId(), user.getId()))
                    .findFirst()
                    .orElse(null);
            if(existsUserGroup != null) // User đã tồn tại trong group
                throw new GroupChatException("User đã tồn tại trong group");
            ChatUser chatUser = ChatUser.builder()
                    .addedAt(LocalDateTime.now())
                    .lastTime(LocalDateTime.now())
                    .user(user)
                    .chat(chat)
                    .build();
            chatUserRepository.save(chatUser);
            chatUsers.add(chatUser);
        }
        List<User> userList = chatUsers.stream().map(ChatUser::getUser).toList();
        return ChatResponse.fromChat(chat, userList.stream().map(UserResponse::fromUser)
                .collect(Collectors.toList()));
    }

    @Override
    @Transactional
    public ChatResponse removeMember(long id, long userId) {
        Chat chat = findById(id);
        User user = userService.findById(userId);
        // Nếu người bị xóa là nhóm trưởng => tổng số thành viên = 1 mới cho xóa
        int totalMember = chatUserRepository.findAllByChat(chat).size();
        if(Objects.equals(chat.getUser().getId(), user.getId())
                && totalMember > 1){
            throw new GroupChatException("Can't leave the group as the group leader");
        }
        ChatUser chatUser = chatUserRepository.findByChatAndUser(chat, user);
        chatUserRepository.delete(chatUser);
        if(totalMember == 1){
            List<Message> messages = messageRepository.findAllByChat(chat);
            for(Message message : messages){
                messageReadRepository.deleteAllByMessage(message);
            }
            messageRepository.deleteAllByChat(chat);
            chatRepository.delete(chat);
        }
        return ChatResponse.fromChat(chat, null);
    }

    @Override
    @Transactional
    public ChatResponse transferTeamLeader(long id, long userId) {
        Chat chat = findById(id);
        User user = userService.findById(userId);
        if(!chatUserRepository.existsByChatAndUser(chat, user))
            throw new GroupChatException("User not in group");
        chat.setUser(user);
        chatRepository.save(chat);
        return ChatResponse.fromChat(chat, null);
    }

    @Override
    public Chat findById(long id) {
        return chatRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Chat not found"));
    }
}
