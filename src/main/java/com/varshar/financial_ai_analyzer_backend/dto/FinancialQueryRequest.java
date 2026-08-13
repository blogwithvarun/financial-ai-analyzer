package com.varshar.financial_ai_analyzer_backend.dto;

public record FinancialQueryRequest(
        Long documentId,
        String question
) {
}