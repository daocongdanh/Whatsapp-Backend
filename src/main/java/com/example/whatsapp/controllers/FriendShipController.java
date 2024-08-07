package com.example.whatsapp.controllers;

import com.example.whatsapp.dtos.FriendShipDTO;
import com.example.whatsapp.models.FriendShip;
import com.example.whatsapp.responses.FriendShipResponse;
import com.example.whatsapp.responses.ResponseSuccess;
import com.example.whatsapp.services.FriendShipService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/friend-ships")
public class FriendShipController {
    private final FriendShipService friendShipService;

    @PutMapping("/reject-friend-request")
    public ResponseEntity<ResponseSuccess> rejectFriendRequest(
            @Valid @RequestBody FriendShipDTO friendShipDTO){
        friendShipService.cancelFriendRequest(friendShipDTO);
        return ResponseEntity.ok().body(ResponseSuccess.builder()
                .message("Từ chối kết bạn thành công")
                .status(HttpStatus.OK.value())
                .build());
    }

    @PutMapping("/accept-friend-request")
    public ResponseEntity<ResponseSuccess> acceptFriendRequest(
            @Valid @RequestBody FriendShipDTO friendShipDTO){
        FriendShipResponse friendShip = friendShipService.acceptFriendRequest(friendShipDTO);
        return ResponseEntity.ok().body(ResponseSuccess.builder()
                .message("Chấp nhận kết bạn thành công")
                .status(HttpStatus.OK.value())
                .data(friendShip)
                .build());
    }

    @PutMapping("/block-friend/{userBlock}")
    public ResponseEntity<ResponseSuccess> blockFriend(
            @PathVariable long userBlock,
            @Valid @RequestBody FriendShipDTO friendShipDTO){
        FriendShipResponse friendShip = friendShipService.blockFriend(friendShipDTO, userBlock);
        return ResponseEntity.ok().body(ResponseSuccess.builder()
                .message("Chặn bạn bè thành công")
                .status(HttpStatus.OK.value())
                .data(friendShip)
                .build());
    }

    @PutMapping("/unblock-friend")
    public ResponseEntity<ResponseSuccess> unblockFriend(
            @Valid @RequestBody FriendShipDTO friendShipDTO){
        FriendShipResponse friendShip = friendShipService.unblockFriend(friendShipDTO);
        return ResponseEntity.ok().body(ResponseSuccess.builder()
                .message("Mở chặn bạn bè thành công")
                .status(HttpStatus.OK.value())
                .data(friendShip)
                .build());
    }

    @DeleteMapping("delete-friend")
    public ResponseEntity<ResponseSuccess> deleteFriend(
            @Valid @RequestBody FriendShipDTO friendShipDTO){
        friendShipService.deleteFriend(friendShipDTO);
        return ResponseEntity.ok().body(ResponseSuccess.builder()
                .message("Xóa bạn bè thành công")
                .status(HttpStatus.NO_CONTENT.value())
                .build());
    }

    @GetMapping("/user/{uid}")
    public ResponseEntity<ResponseSuccess> getUserByEmail(
            @PathVariable long uid,
            @RequestParam("email") String email){
        return ResponseEntity.ok().body(ResponseSuccess.builder()
                .message("Get user by email successfully")
                .status(HttpStatus.OK.value())
                .data(friendShipService.getFriendByEmail(uid,email))
                .build());
    }

    @GetMapping("/friend-request/{uid}")
    public ResponseEntity<ResponseSuccess> getAllFriendRequestByUser(@PathVariable long uid){
        return ResponseEntity.ok().body(ResponseSuccess.builder()
                .message("Get all friend request successfully")
                .status(HttpStatus.OK.value())
                .data(friendShipService.getAllFriendRequestByUser(uid))
                .build());
    }
}
