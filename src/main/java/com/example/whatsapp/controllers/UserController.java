package com.example.whatsapp.controllers;

import com.example.whatsapp.dtos.LoginDTO;
import com.example.whatsapp.dtos.RegisterDTO;
import com.example.whatsapp.responses.ResponseSuccess;
import com.example.whatsapp.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<ResponseSuccess> createUser(@Valid @RequestBody RegisterDTO registerDTO){
        return ResponseEntity.ok().body(ResponseSuccess.builder()
                .message("Tạo user thành công")
                .status(HttpStatus.CREATED.value())
                .data(userService.createUser(registerDTO))
                .build());
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseSuccess> login(@Valid @RequestBody LoginDTO loginDTO){
        return ResponseEntity.ok().body(ResponseSuccess.builder()
                .message("Đăng nhập thành công")
                .status(HttpStatus.OK.value())
                .data(userService.login(loginDTO))
                .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseSuccess> getUserById(@PathVariable("id") long id){
        return ResponseEntity.ok().body(ResponseSuccess.builder()
                .message("Lấy thông tin user theo id thành công")
                .status(HttpStatus.OK.value())
                .data(userService.getUserById(id))
                .build());
    }

    @GetMapping("/friend-ship/{id}")
    public ResponseEntity<ResponseSuccess> getAllFriend(@PathVariable("id") long id){
        return ResponseEntity.ok().body(ResponseSuccess.builder()
                .message("Get all friend successfully")
                .status(HttpStatus.OK.value())
                .data(userService.getAllFriend(id))
                .build());
    }

    @GetMapping("/active/{id}/{isActive}")
    public ResponseEntity<ResponseSuccess> updateUserStatus(@PathVariable long id,
                                                            @PathVariable boolean isActive){
        return ResponseEntity.ok().body(ResponseSuccess.builder()
                .message("Update user status successfully")
                .status(HttpStatus.OK.value())
                .data(userService.updateUserStatus(id, isActive))
                .build());
    }

}
