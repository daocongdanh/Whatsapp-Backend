package com.example.whatsapp.controllers;

import com.example.whatsapp.dtos.MessageDTO;
import com.example.whatsapp.dtos.RegisterDTO;
import com.example.whatsapp.responses.ResponseSuccess;
import com.example.whatsapp.services.MessageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/messages")
public class MessageController {
    private final MessageService messageService;

    @PostMapping("")
    public ResponseEntity<ResponseSuccess> sendMessage(@Valid @RequestBody MessageDTO messageDTO){
        return ResponseEntity.ok().body(ResponseSuccess.builder()
                .message("Send message successfully")
                .status(HttpStatus.CREATED.value())
                .data(messageService.sendMessage(messageDTO))
                .build());
    }

    @GetMapping("/chat/{cid}")
    public ResponseEntity<ResponseSuccess> getAllMessagesByChat(@PathVariable long cid){
        return ResponseEntity.ok().body(ResponseSuccess.builder()
                .message("Get all message by chat successfully")
                .status(HttpStatus.OK.value())
                .data(messageService.getAllMessagesByChat(cid))
                .build());
    }

    @PutMapping("/chat/update-status/{cid}/{uid}")
    public ResponseEntity<ResponseSuccess> updateAllMessageStatus(@PathVariable("cid") long cid,
                                                                  @PathVariable("uid") long uid){
        messageService.updateAllMessageStatus(cid, uid);
        return ResponseEntity.ok().body(ResponseSuccess.builder()
                .message("Update all message by chat successfully")
                .status(HttpStatus.NO_CONTENT.value())
                .build());
    }
}
