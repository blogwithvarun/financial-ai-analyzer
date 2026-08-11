package com.varshar.financial_ai_analyzer_backend.controller;

import com.varshar.financial_ai_analyzer_backend.dto.UploadedDocumentResponse;
import com.varshar.financial_ai_analyzer_backend.service.DocumentService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/ai/api/v1")
public class DocumentController {

    private final DocumentService documentService;

    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UploadedDocumentResponse> uploadDocuments(@RequestParam("file") MultipartFile multipartFile, @RequestHeader("Authorization") String authToken) {

        return documentService.uploadDocuments(authToken, multipartFile);

    }
}
