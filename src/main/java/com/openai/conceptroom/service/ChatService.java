package com.openai.conceptroom.service;

import com.openai.conceptroom.dto.ChatRequest;
import com.openai.conceptroom.dto.ChatResponse;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class ChatService {

    private final ChatClient openAiChatClient;
    private final ChatClient ollamaChatClient;

    public ChatService(
            @Qualifier("openAiChatClient") ChatClient openAiChatClient,
            @Qualifier("ollamaChatClient") ChatClient ollamaChatClient) {
        this.openAiChatClient = openAiChatClient;
        this.ollamaChatClient = ollamaChatClient;
    }

    public ChatResponse chat(ChatRequest request) {
        String selectedModel = request.model().trim().toLowerCase(Locale.ROOT);

        ChatClient selectedClient = switch (selectedModel) {
            case "openai" -> openAiChatClient;
            case "ollama" -> ollamaChatClient;
            default -> throw new IllegalArgumentException(
                    "Unsupported model: " + request.model() + ". Use 'openai' or 'ollama'.");
        };

        String answer = selectedClient.prompt()
                .user(request.message())
                .call()
                .content();

        return new ChatResponse(selectedModel, answer);
    }
}
