package com.skillswap.backend.service;

import com.skillswap.backend.model.ChatMessage;
import com.skillswap.backend.model.Student;
import com.skillswap.backend.repository.ChatMessageRepository;
import com.skillswap.backend.repository.StudentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service managing backend-persisted chat messages between students.
 */
@Service
@Transactional
public class ChatService {

    private final ChatMessageRepository chatMessageRepository;
    private final StudentRepository studentRepository;

    public ChatService(ChatMessageRepository chatMessageRepository, StudentRepository studentRepository) {
        this.chatMessageRepository = chatMessageRepository;
        this.studentRepository = studentRepository;
    }

    public ChatMessage sendMessage(Long senderId, Long receiverId, String text) {
        if (text == null || text.trim().isEmpty()) {
            throw new IllegalArgumentException("Message text cannot be empty.");
        }

        String senderName = "Student";
        Student sender = studentRepository.findById(senderId).orElse(null);
        if (sender != null) {
            senderName = sender.getName();
        }

        ChatMessage message = new ChatMessage(senderId, receiverId, senderName, text.trim());
        return chatMessageRepository.save(message);
    }

    @Transactional(readOnly = true)
    public List<ChatMessage> getConversation(Long user1, Long user2) {
        return chatMessageRepository.findConversationBetween(user1, user2);
    }
}
