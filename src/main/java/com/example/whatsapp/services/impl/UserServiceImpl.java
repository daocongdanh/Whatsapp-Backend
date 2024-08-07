package com.example.whatsapp.services.impl;

import com.example.whatsapp.dtos.LoginDTO;
import com.example.whatsapp.dtos.RegisterDTO;
import com.example.whatsapp.enums.FriendShipStatus;
import com.example.whatsapp.exceptions.BadCredentialsException;
import com.example.whatsapp.exceptions.ResourceNotFoundException;
import com.example.whatsapp.models.FriendShip;
import com.example.whatsapp.models.User;
import com.example.whatsapp.repositories.FriendShipRepository;
import com.example.whatsapp.repositories.UserRepository;
import com.example.whatsapp.responses.FriendResponse;
import com.example.whatsapp.responses.UserResponse;
import com.example.whatsapp.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final FriendShipRepository friendShipRepository;
    @Override
    @Transactional
    public UserResponse createUser(RegisterDTO registerDTO) {
        User user = User.builder()
                .fullName(registerDTO.getFullName())
                .email(registerDTO.getEmail())
                .password(registerDTO.getPassword())
                .avatar(registerDTO.getAvatar())
                .active(false)
                .lastActive(LocalDateTime.now())
                .build();
        return UserResponse.fromUser(userRepository.save(user));
    }

    @Override
    public UserResponse login(LoginDTO loginDTO) {
        User user = userRepository.findByEmail(loginDTO.getEmail())
                .orElseThrow(() -> new BadCredentialsException("Tài khoản hoặc mật khẩu sai"));
        if(!user.getPassword().equals(loginDTO.getPassword()))
            throw new BadCredentialsException("Tài khoản hoặc mật khẩu sai");
        return UserResponse.fromUser(user);
    }

    @Override
    public UserResponse getUserById(long id) {
        User user = findById(id);
        return UserResponse.fromUser(user);
    }

    @Override
    public List<FriendResponse> getAllFriend(long id) {
        User userOne = findById(id);
        User userTwo = findById(id);
        Map<Character, List<User>> map =
                friendShipRepository.findAllByUserOneOrUserTwo(userOne,userTwo, FriendShipStatus.PENDING)
                .stream()
                .map(friendShip -> {
                    if(friendShip.getUserOne().getId().equals(id))
                        return friendShip.getUserTwo();
                    return friendShip.getUserOne();
                })
                .collect(Collectors.groupingBy(
                        user -> user.getFullName().charAt(0),
                        LinkedHashMap::new, // Specify the supplier for LinkedHashMap
                        Collectors.toList()
                ));
        return map.entrySet().stream()
                .map(entry -> new FriendResponse(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }

    @Override
    public UserResponse updateUserStatus(long id, boolean isActive) {
        User user = userRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        user.setActive(isActive);
        if(!isActive)
            user.setLastActive(LocalDateTime.now());
        return UserResponse.fromUser(userRepository.save(user));
    }

    @Override
    public User findById(long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id = " + id));
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy user"));
    }
}
