package com.openai.conceptroom.controller;

import com.openai.conceptroom.dto.ChatRequest;
import com.openai.conceptroom.dto.ChatResponse;
import com.openai.conceptroom.service.ChatService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api")
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping("/chat")
    public ChatResponse chat(@RequestBody ChatRequest request) {
        if (request == null || request.message() == null || request.message().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "message must not be blank");
        }
        if (request.model() == null || request.model().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "model must be 'openai' or 'ollama' or 'googlegenai'");
        }

        try {
            return chatService.chat(request);
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage(), ex);
        }
    }
}
