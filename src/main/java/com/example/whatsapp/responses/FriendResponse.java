package com.example.whatsapp.responses;

import com.example.whatsapp.models.User;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FriendResponse {
    private Character character;
    private List<User> users;
}
