package com.varshar.financial_ai_analyzer_backend.controller;

import com.varshar.financial_ai_analyzer_backend.service.FinancialAiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AiController {

    private final FinancialAiService financialAiService;

    @GetMapping("/test")
    public ResponseEntity<String> test(
            @RequestParam String question) {

        return ResponseEntity.ok(
                financialAiService.ask(question)
        );
    }
}