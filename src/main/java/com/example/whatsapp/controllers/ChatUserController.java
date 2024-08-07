package com.example.whatsapp.controllers;

import com.example.whatsapp.dtos.ChatUserDTO;
import com.example.whatsapp.responses.ChatUserResponse;
import com.example.whatsapp.responses.ResponseSuccess;
import com.example.whatsapp.services.ChatUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chat-users")
public class ChatUserController {
    private final ChatUserService chatUserService;

    @PostMapping("")
    public ResponseEntity<ResponseSuccess> addUserToGroup(
            @Valid @RequestBody ChatUserDTO chatUserDTO){
        ChatUserResponse chatUserResponse = chatUserService.addUserToGroup(chatUserDTO);
        return ResponseEntity.ok().body(ResponseSuccess.builder()
                .message("Thêm user vào group thành công")
                .status(HttpStatus.CREATED.value())
                .data(chatUserResponse)
                .build());
    }

    @GetMapping("/user/{uid}")
    public ResponseEntity<ResponseSuccess> getAllChatsByUser(@PathVariable long uid){
        List<ChatUserResponse> chatUserResponses = chatUserService.getAllChatsByUser(uid);
        return ResponseEntity.ok().body(ResponseSuccess.builder()
                .message("Get all chat-users by user")
                .status(HttpStatus.OK.value())
                .data(chatUserResponses)
                .build());
    }

}
