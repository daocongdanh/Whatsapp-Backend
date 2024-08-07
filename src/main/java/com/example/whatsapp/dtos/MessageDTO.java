package com.example.whatsapp.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageDTO {
    private Long userId;
    private Long chatId;
    private String content;
    @JsonProperty("isImage")
    private boolean isImage;
}
