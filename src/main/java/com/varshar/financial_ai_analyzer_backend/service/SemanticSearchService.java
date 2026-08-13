package com.varshar.financial_ai_analyzer_backend.service;

import com.varshar.financial_ai_analyzer_backend.dto.SemanticSearchResult;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SemanticSearchService {

    private final VectorStore vectorStore;

    public SemanticSearchService(VectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }

    public List<SemanticSearchResult> search(String query, int topK) {

        SearchRequest searchRequest = SearchRequest.builder()
                .query(query)
                .topK(topK)
                .build();

        return vectorStore.similaritySearch(searchRequest)
                .stream()
                .map(document -> new SemanticSearchResult(
                        document.getText(),
                        document.getMetadata(),
                        document.getScore()
                ))
                .toList();
    }
}