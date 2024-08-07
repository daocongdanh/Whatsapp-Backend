package com.example.whatsapp.services;

import com.example.whatsapp.dtos.LoginDTO;
import com.example.whatsapp.dtos.RegisterDTO;
import com.example.whatsapp.models.User;
import com.example.whatsapp.responses.FriendResponse;
import com.example.whatsapp.responses.UserResponse;

import java.util.List;

public interface UserService {

    UserResponse createUser(RegisterDTO registerDTO);
    UserResponse login(LoginDTO loginDTO);
    UserResponse getUserById(long id);
    List<FriendResponse> getAllFriend(long id);
    UserResponse updateUserStatus(long id, boolean isActive);
    User findById(long id);
    User getUserByEmail(String email);
}
