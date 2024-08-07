package com.example.whatsapp.responses;

import com.example.whatsapp.models.FriendShip;
import com.example.whatsapp.models.User;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FriendShipResponse {
    private Long id;
    private User userOne;
    private User userTwo;
    private String status;

    public static FriendShipResponse fromFriendShip(FriendShip friendShip){
        return FriendShipResponse.builder()
                .id(friendShip.getId())
                .userOne(friendShip.getUserOne())
                .userTwo(friendShip.getUserTwo())
                .status(friendShip.getFriendShipStatus().toString())
                .build();
    }
}
