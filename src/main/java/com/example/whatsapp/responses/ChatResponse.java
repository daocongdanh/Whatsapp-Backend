package com.example.whatsapp.responses;

import com.example.whatsapp.models.Chat;
import com.example.whatsapp.models.User;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatResponse {
    private Long id;
    private String name;
    private String image;
    private boolean isGroup;
    private Long userId;
    private List<UserResponse> users;

    public static ChatResponse fromChat(Chat chat, List<UserResponse> users){
        return ChatResponse.builder()
                .id(chat.getId())
                .name(chat.getName())
                .image(chat.getImage())
                .isGroup(chat.isGroup())
                .userId(chat.getUser() == null ? null : chat.getUser().getId())
                .users(users)
                .build();
    }
}
