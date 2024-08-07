package com.example.whatsapp.dtos;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatDTO {
    private String name;
    private String image;
    private boolean isGroup;
    private Long userId;
}
