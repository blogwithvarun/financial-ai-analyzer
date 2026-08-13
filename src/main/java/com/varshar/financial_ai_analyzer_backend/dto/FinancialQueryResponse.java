package com.varshar.financial_ai_analyzer_backend.dto;

public record FinancialQueryResponse(
        Long documentId,
        String question,
        String answer
) {
}