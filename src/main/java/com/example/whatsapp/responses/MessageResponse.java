package com.example.whatsapp.responses;

import com.example.whatsapp.models.Chat;
import com.example.whatsapp.models.Message;
import com.example.whatsapp.models.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.swing.text.DateFormatter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageResponse {
    private Long id;
    private String content;
    private boolean status;
    private String timeStamp;
    @JsonProperty("isImage")
    private boolean isImage;
    @JsonProperty("isSystem")
    private boolean isSystem;
    private UserResponse user;
    private Chat chat;
    public static MessageResponse fromMessage(Message message, boolean status){
        LocalDateTime date = message.getTimeStamp();
        String time;
        if(date.toLocalDate().equals(LocalDate.now())){
            time = DateTimeFormatter.ofPattern("HH:mm").format(date);
        }
        else time = DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy").format(date);
        return MessageResponse.builder()
                .id(message.getId())
                .content(message.getContent())
                .status(status)
                .timeStamp(time)
                .isImage(message.isImage())
                .isSystem(message.isSystem())
                .user(UserResponse.fromUser(message.getUser()))
                .chat(message.getChat())
                .build();
    }
}
