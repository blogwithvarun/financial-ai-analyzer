package com.varshar.financial_ai_analyzer_backend.dto;

import java.util.Map;

public record SemanticSearchResult(
        String content,
        Map<String, Object> metadata,
        Double score
) {
}