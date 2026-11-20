package com.openai.conceptroom.dto;

public record ChatRequest(
        String model,
        String message
) {
}
