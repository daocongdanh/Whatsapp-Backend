package com.example.whatsapp.responses;

import com.example.whatsapp.models.User;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SearchFriendResponse {
    private User friend;
    private String status;
}
