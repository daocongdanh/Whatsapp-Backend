package com.example.whatsapp.dtos;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatUserDTO {
    private Long userId;
    private Long chatId;
}
