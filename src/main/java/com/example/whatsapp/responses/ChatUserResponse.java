package com.example.whatsapp.responses;

import com.example.whatsapp.models.ChatUser;
import lombok.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatUserResponse {
    private Long id;
    private LocalDateTime addedAt;
    private Long userId;
    private Long chatId;
    private String lastTime;
    private boolean isGroup;
    private Object participant;
    private List<MessageResponse> messages;

    public static ChatUserResponse fromChatUser(ChatUser chatUser,
                                                Object participant, List<MessageResponse> messages){
        String lastTime = "";
        LocalDateTime now = LocalDateTime.now();
        if(chatUser.getLastTime() != null){
            long minutes = Duration.between(chatUser.getLastTime(), now).toMinutes();
            long hours = Duration.between(chatUser.getLastTime(), now).toHours();
            long days = Duration.between(chatUser.getLastTime(), now).toDays();
            long weeks = ChronoUnit.WEEKS.between(chatUser.getLastTime(), now);
            long months = ChronoUnit.MONTHS.between(chatUser.getLastTime(), now);
            long years = ChronoUnit.YEARS.between(chatUser.getLastTime(), now);
            if (minutes < 60) {
                lastTime = String.format("%d minutes",minutes);
            } else if (hours < 24) {
                lastTime = String.format("%d hours",hours);
            } else if (days < 7) {
                lastTime = String.format("%d days",days);
            } else if (weeks < 4) {//
                lastTime = DateTimeFormatter.ofPattern("dd/MM").format(chatUser.getLastTime());
            } else if(months < 12){
                lastTime = DateTimeFormatter.ofPattern("dd/MM").format(chatUser.getLastTime());
            }
            else{
                lastTime = DateTimeFormatter.ofPattern("dd/MM/yyyy").format(chatUser.getLastTime());
            }
        }
        boolean isGroup = chatUser.getChat().isGroup();
        return ChatUserResponse.builder()
                .id(chatUser.getId())
                .addedAt(chatUser.getAddedAt())
                .userId(chatUser.getUser().getId())
                .chatId(chatUser.getChat().getId())
                .lastTime(lastTime)
                .isGroup(isGroup)
                .participant(participant)
                .messages(messages)
                .build();
    }
}
