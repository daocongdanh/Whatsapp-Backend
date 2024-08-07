package com.example.whatsapp.repositories;

import com.example.whatsapp.models.Chat;
import com.example.whatsapp.models.Message;
import com.example.whatsapp.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findAllByChat(Chat chat);

    @Query("select m from Message m where m.chat = ?1 " +
            "and m.user != ?2 and m.isSystem = false order by m.timeStamp desc limit 6")
    List<Message> findTop6MessageByChatAndUser(Chat chat, User user);

    @Query("select m from Message m where m.chat = ?1 " +
            "order by m.timeStamp desc limit 1" )
    Message findLastMessageByChat(Chat chat);

    void deleteAllByChat(Chat chat);
}
