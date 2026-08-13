package com.varshar.financial_ai_analyzer_backend.controller;

import com.varshar.financial_ai_analyzer_backend.dto.SemanticSearchResult;
import com.varshar.financial_ai_analyzer_backend.service.SemanticSearchService;
import org.springframework.ai.document.Document;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/search")
public class SemanticSearchController {

    private final SemanticSearchService semanticSearchService;

    public SemanticSearchController(
            SemanticSearchService semanticSearchService) {
        this.semanticSearchService = semanticSearchService;
    }

    @GetMapping
    public List<SemanticSearchResult> search(
            @RequestParam String query,
            @RequestParam(defaultValue = "5") int topK) {

        return semanticSearchService.search(query, topK);
    }
}