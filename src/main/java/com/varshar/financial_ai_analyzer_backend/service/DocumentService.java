package com.varshar.financial_ai_analyzer_backend.service;

import com.varshar.financial_ai_analyzer_backend.dto.UploadedDocumentResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tools.jackson.databind.JsonNode;

@Service
public interface DocumentService {
    ResponseEntity<UploadedDocumentResponse> uploadDocuments(String authToken, MultipartFile multipartFile);

    JsonNode extractStructuredData(String rawText);
}
