package com.example.whatsapp.repositories;

import com.example.whatsapp.models.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRepository extends JpaRepository<Chat, Long> {
}
