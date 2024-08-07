package com.example.whatsapp.repositories;

import com.example.whatsapp.models.Chat;
import com.example.whatsapp.models.ChatUser;
import com.example.whatsapp.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ChatUserRepository extends JpaRepository<ChatUser, Long> {

    @Query("select c from ChatUser c where c.user = ?1 order by c.lastTime desc")
    List<ChatUser> findAllByUser(User user);
    List<ChatUser> findAllByChat(Chat chat);
    ChatUser findByChatAndUser(Chat chat, User user);
    boolean existsByChatAndUser(Chat chat, User user);
}
