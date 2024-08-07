package com.example.whatsapp.controllers;

import com.example.whatsapp.dtos.ChatDTO;
import com.example.whatsapp.dtos.GroupChatDTO;
import com.example.whatsapp.responses.ChatResponse;
import com.example.whatsapp.responses.ResponseSuccess;
import com.example.whatsapp.services.ChatService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chats")
public class ChatController {
    private final ChatService chatService;

    @PostMapping("")
    public ResponseEntity<ResponseSuccess> createChat(@Valid @RequestBody ChatDTO chatDTO){
        ChatResponse chatResponse = chatService.createChat(chatDTO);
        return ResponseEntity.ok().body(ResponseSuccess.builder()
                .message("Tạo chat thành công")
                .status(HttpStatus.CREATED.value())
                .data(chatResponse)
                .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseSuccess> getChatById(@PathVariable long id){
        ChatResponse chatResponse = chatService.getChatById(id);
        return ResponseEntity.ok().body(ResponseSuccess.builder()
                .message("Lấy thông tin chat theo id thành công")
                .status(HttpStatus.OK.value())
                .data(chatResponse)
                .build());
    }

    @PostMapping("/create-group")
    public ResponseEntity<?> createGroupChat(@Valid @RequestBody GroupChatDTO groupChatDTO){
        return ResponseEntity.ok().body(ResponseSuccess.builder()
                .message("Tạo group thành công")
                .status(HttpStatus.CREATED.value())
                .data(chatService.createGroupChat(groupChatDTO))
                .build());
    }

    @PostMapping("/add-member/{id}")
    public ResponseEntity<?> addMemberToGroupChat(@PathVariable long id,
                                                  @RequestBody Map<String, List<Long>> payload){
        List<Long> users = payload.get("users");
        return ResponseEntity.ok().body(ResponseSuccess.builder()
                .message("Thêm mới thành viên thành công")
                .status(HttpStatus.CREATED.value())
                .data(chatService.addMemberToGroupChat(id, users))
                .build());
    }

    @DeleteMapping("remove-member/{chatId}/{userId}")
    public ResponseEntity<?> removeMember(@PathVariable("chatId") long chatId,
                                          @PathVariable("userId") long userId){
        chatService.removeMember(chatId, userId);
        return ResponseEntity.ok().body(ResponseSuccess.builder()
                .message("Leave group successfully")
                .status(HttpStatus.NO_CONTENT.value())
                .build());
    }

    @PutMapping("/transfer-team-leader/{id}")
    public ResponseEntity<?> transferTeamLeader(@PathVariable long id,
                                                @RequestBody Map<String, Long> payload){
        return ResponseEntity.ok().body(ResponseSuccess.builder()
                .message("Chuyển nhóm trưởng thành công")
                .status(HttpStatus.OK.value())
                .data(chatService.transferTeamLeader(id, payload.get("userId")))
                .build());
    }
}
