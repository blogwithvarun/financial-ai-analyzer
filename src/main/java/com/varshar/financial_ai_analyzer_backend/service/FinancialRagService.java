package com.varshar.financial_ai_analyzer_backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FinancialRagService {

    private final ChatClient chatClient;
    private final VectorStore vectorStore;

    public String ask(Long documentId, String question) {

        // 1. Search PGVector
        List<Document> documents = vectorStore.similaritySearch(
                SearchRequest.builder()
                        .query(question)
                        .topK(5)
                        .similarityThreshold(0.3)
                        .filterExpression("documentId == " + documentId)
                        .build()
        );
        if (documents == null || documents.isEmpty()) {
            return "I could not find relevant information in the uploaded financial document.";
        }
        String context = documents.stream()
                .map(Document::getText)
                .collect(Collectors.joining("\n\n---\n\n"));

        String systemPrompt = """
                You are a financial document assistant.

                Answer the user's question using ONLY the information
                provided in the context.

                Important rules:
                - Do not invent financial information.
                - Do not make assumptions.
                - Do not calculate information that is not supported
                  by the provided context.
                - If the answer is not available in the context,
                  clearly say that the information is not available
                  in the uploaded document.
                - Keep the answer concise and clear.

                Context:
                %s
                """.formatted(context);
        return chatClient
                .prompt()
                .system(systemPrompt)
                .user(question)
                .call()
                .content();
    }
}