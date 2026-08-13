package com.varshar.financial_ai_analyzer_backend.controller;

import com.varshar.financial_ai_analyzer_backend.service.EmbeddingService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ai")
public class AiTestController {

    private final EmbeddingService embeddingService;

    public AiTestController(EmbeddingService embeddingService) {
        this.embeddingService = embeddingService;
    }

    @GetMapping("/embedding")
    public float[] embedding(@RequestParam("text") String text) {
        return embeddingService.generateEmbedding(text);
    }
}