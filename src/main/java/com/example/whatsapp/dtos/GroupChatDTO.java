package com.example.whatsapp.dtos;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GroupChatDTO {
    private String name;
    private String image;
    private Long userId;
    private List<Long> users;
}
