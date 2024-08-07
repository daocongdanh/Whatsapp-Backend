package com.example.whatsapp.services;

import com.example.whatsapp.dtos.FriendShipDTO;
import com.example.whatsapp.responses.FriendShipResponse;
import com.example.whatsapp.responses.SearchFriendResponse;

import java.util.List;

public interface FriendShipService {
    FriendShipResponse sendFriendRequest(FriendShipDTO friendShipDTO);
    FriendShipResponse acceptFriendRequest(FriendShipDTO friendShipDTO);
    FriendShipResponse blockFriend(FriendShipDTO friendShipDTO, long userBlock);
    FriendShipResponse unblockFriend(FriendShipDTO friendShipDTO);
    void deleteFriend(FriendShipDTO friendShipDTO);
    SearchFriendResponse getFriendByEmail(long uid, String email);
    List<FriendShipResponse> getAllFriendRequestByUser(long uid);
    void cancelFriendRequest(FriendShipDTO friendShipDTO);
}
