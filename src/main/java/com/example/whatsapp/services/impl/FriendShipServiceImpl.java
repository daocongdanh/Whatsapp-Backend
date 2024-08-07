package com.example.whatsapp.services.impl;

import com.example.whatsapp.dtos.ChatDTO;
import com.example.whatsapp.dtos.FriendShipDTO;
import com.example.whatsapp.enums.FriendShipStatus;
import com.example.whatsapp.exceptions.ResourceNotFoundException;
import com.example.whatsapp.models.Chat;
import com.example.whatsapp.models.ChatUser;
import com.example.whatsapp.models.FriendShip;
import com.example.whatsapp.models.User;
import com.example.whatsapp.repositories.ChatRepository;
import com.example.whatsapp.repositories.ChatUserRepository;
import com.example.whatsapp.repositories.FriendShipRepository;
import com.example.whatsapp.repositories.UserRepository;
import com.example.whatsapp.responses.FriendShipResponse;
import com.example.whatsapp.responses.SearchFriendResponse;
import com.example.whatsapp.services.FriendShipService;
import com.example.whatsapp.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FriendShipServiceImpl implements FriendShipService {
    private final UserService userService;
    private final FriendShipRepository friendShipRepository;
    private final ChatRepository chatRepository;
    private final ChatUserRepository chatUserRepository;
    @Override
    @Transactional
    public FriendShipResponse sendFriendRequest(FriendShipDTO friendShipDTO) {
        User userOne = userService.findById(friendShipDTO.getUserOneId());
        User userTwo = userService.findById(friendShipDTO.getUserTwoId());
        FriendShip friendShip = FriendShip.builder()
                .userOne(userOne)
                .userTwo(userTwo)
                .friendShipStatus(FriendShipStatus.PENDING)
                .userSend(userOne.getId())
                .userBlock(0)
                .build();
        return FriendShipResponse.fromFriendShip(friendShipRepository.save(friendShip));
    }

    @Override
    @Transactional
    public FriendShipResponse acceptFriendRequest(FriendShipDTO friendShipDTO) {
        User userOne = userService.findById(friendShipDTO.getUserOneId());
        User userTwo = userService.findById(friendShipDTO.getUserTwoId());
        FriendShip friendShip = friendShipRepository.findByUserOneAndUserTwo(userOne, userTwo);
        friendShip.setFriendShipStatus(FriendShipStatus.ACCEPTED);
        friendShipRepository.save(friendShip);

        // Tạo chat cho 2 user
        Chat chat = Chat.builder()
                .name(null)
                .image(null)
                .isGroup(false)
                .user(null)
                .build();
        chatRepository.save(chat);

        ChatUser chatUserOne = ChatUser.builder()
                .addedAt(LocalDateTime.now())
                .lastTime(LocalDateTime.now())
                .user(userOne)
                .chat(chat)
                .build();

        ChatUser chatUserTwo = ChatUser.builder()
                .addedAt(LocalDateTime.now())
                .lastTime(LocalDateTime.now())
                .user(userTwo)
                .chat(chat)
                .build();

        chatUserRepository.save(chatUserOne);
        chatUserRepository.save(chatUserTwo);

        return FriendShipResponse.fromFriendShip(friendShip);
    }

    @Override
    @Transactional
    public FriendShipResponse blockFriend(FriendShipDTO friendShipDTO, long userBlock) {
        User userOne = userService.findById(friendShipDTO.getUserOneId());
        User userTwo = userService.findById(friendShipDTO.getUserTwoId());
        FriendShip friendShip = friendShipRepository.
                findByUserOneAndUserTwoOrUserTwoAndUserOne(userOne, userTwo, userOne, userTwo);
        friendShip.setFriendShipStatus(FriendShipStatus.BLOCKED);
        friendShip.setUserBlock(userBlock);
        return FriendShipResponse.fromFriendShip(friendShipRepository.save(friendShip));
    }

    @Override
    @Transactional
    public FriendShipResponse unblockFriend(FriendShipDTO friendShipDTO) {
        User userOne = userService.findById(friendShipDTO.getUserOneId());
        User userTwo = userService.findById(friendShipDTO.getUserTwoId());
        FriendShip friendShip = friendShipRepository.findByUserOneAndUserTwo(userOne, userTwo);
        friendShip.setFriendShipStatus(FriendShipStatus.ACCEPTED);
        return FriendShipResponse.fromFriendShip(friendShipRepository.save(friendShip));
    }

    @Override
    @Transactional
    public void deleteFriend(FriendShipDTO friendShipDTO) {
        User userOne = userService.findById(friendShipDTO.getUserOneId());
        User userTwo = userService.findById(friendShipDTO.getUserTwoId());
        friendShipRepository.deleteByUserOneAndUserTwo(userOne, userTwo);
    }

    @Override
    public SearchFriendResponse getFriendByEmail(long uid, String email) {
        User user = userService.findById(uid);
        User friend = userService.getUserByEmail(email);
        String status = "";
        if(user.getId().equals(friend.getId())){
            status = "ME";
            return new SearchFriendResponse(friend, status);
        }
        FriendShip friendShip = friendShipRepository.
                findByUserOneAndUserTwoOrUserTwoAndUserOne(user, friend, user, friend);
        if(friendShip == null){
            status = "STRANGER";
            return new SearchFriendResponse(friend, status);
        }
        if(!friendShip.getFriendShipStatus().equals(FriendShipStatus.PENDING))
            status = "FRIEND";
        else {
            if(user.getId().equals(friendShip.getUserOne().getId())){
                status = "WAIT_ACCEPTED";
            }
            else status = "PENDING";
        }
        return new SearchFriendResponse(friend, status);
    }

    @Override
    public List<FriendShipResponse> getAllFriendRequestByUser(long uid) {
        User user = userService.findById(uid);
        List<FriendShip> friendShips =
                friendShipRepository.findAllByUserTwoAndFriendShipStatus(user, FriendShipStatus.PENDING);
        return friendShips.stream()
                .map(FriendShipResponse::fromFriendShip)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void cancelFriendRequest(FriendShipDTO friendShipDTO) {
        User userOne = userService.findById(friendShipDTO.getUserOneId());
        User userTwo = userService.findById(friendShipDTO.getUserTwoId());
        friendShipRepository.deleteByUserOneAndUserTwo(userOne, userTwo);
    }

}
