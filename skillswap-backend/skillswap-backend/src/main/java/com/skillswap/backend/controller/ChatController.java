package com.skillswap.backend.controller;

import com.skillswap.backend.dto.MessageRequestDto;
import com.skillswap.backend.model.ChatMessage;
import com.skillswap.backend.service.ChatService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * REST Controller for persistent chat between students.
 */
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/messages")
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @GetMapping
    public ResponseEntity<?> getConversation(@RequestParam Long student1, @RequestParam Long student2) {
        if (student1 == null || student2 == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "student1 and student2 parameters are required."));
        }
        List<ChatMessage> conversation = chatService.getConversation(student1, student2);
        return ResponseEntity.ok(conversation);
    }

    @PostMapping
    public ResponseEntity<?> sendMessage(@RequestBody MessageRequestDto req) {
        try {
            if (req == null || req.getSenderId() == null || req.getReceiverId() == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "senderId and receiverId are required."));
            }
            ChatMessage saved = chatService.sendMessage(req.getSenderId(), req.getReceiverId(), req.getMessageText());
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
