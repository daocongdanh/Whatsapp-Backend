package com.example.whatsapp.responses;

import com.example.whatsapp.models.User;
import lombok.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {
    private long id;
    private String fullName;
    private String email;
    private String avatar;
    private boolean active;
    private String lastActive;

    public static UserResponse fromUser(User user){
        String lastActive = "";
        LocalDateTime now = LocalDateTime.now();
        if(user.getLastActive() != null){
            long minutes = Duration.between(user.getLastActive(), now).toMinutes();
            long hours = Duration.between(user.getLastActive(), now).toHours();
            long days = Duration.between(user.getLastActive(), now).toDays();
            long weeks = ChronoUnit.WEEKS.between(user.getLastActive(), now);
            long months = ChronoUnit.MONTHS.between(user.getLastActive(), now);
            if (minutes < 60) {
                lastActive = String.format("Active %d minutes ago",minutes);
            } else if (hours < 24) {
                lastActive = String.format("Active %d hours ago",hours);
            } else if (days < 7) {
                lastActive = String.format("Active %d days ago",days);
            } else if (weeks < 4) {
                lastActive = String.format("Active %d weeks ago",weeks);
            } else {
                lastActive = String.format("Active %d months ago",months);
            }
        }
        return UserResponse.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .avatar(user.getAvatar())
                .active(user.isActive())
                .lastActive(lastActive)
                .build();
    }
}
