package com.example.whatsapp.repositories;

import com.example.whatsapp.models.Message;
import com.example.whatsapp.models.MessageRead;
import com.example.whatsapp.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageReadRepository extends JpaRepository<MessageRead, Long> {
    MessageRead findByUserAndMessage(User user, Message message);
    void deleteAllByMessage(Message message);
}
