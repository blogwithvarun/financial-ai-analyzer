package com.varshar.financial_ai_analyzer_backend.controller;

import com.varshar.financial_ai_analyzer_backend.dto.FinancialQueryRequest;
import com.varshar.financial_ai_analyzer_backend.dto.FinancialQueryResponse;
import com.varshar.financial_ai_analyzer_backend.service.FinancialRagService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/financial")
@RequiredArgsConstructor
public class FinancialQueryController {

    private final FinancialRagService financialRagService;

    @PostMapping("/query")
    public ResponseEntity<FinancialQueryResponse> query(
            @RequestBody FinancialQueryRequest request) {

        String answer = financialRagService.ask(
                request.documentId(),
                request.question()
        );

        FinancialQueryResponse response =
                new FinancialQueryResponse(
                        request.documentId(),
                        request.question(),
                        answer
                );

        return ResponseEntity.ok(response);
    }
}