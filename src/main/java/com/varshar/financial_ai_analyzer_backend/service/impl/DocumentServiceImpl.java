package com.varshar.financial_ai_analyzer_backend.service.impl;

import com.varshar.financial_ai_analyzer_backend.constants.DocumentStatus;
import com.varshar.financial_ai_analyzer_backend.dto.UploadedDocumentResponse;
import com.varshar.financial_ai_analyzer_backend.entity.Document;
import com.varshar.financial_ai_analyzer_backend.entity.DocumentChunk;
import com.varshar.financial_ai_analyzer_backend.repository.DocumentChunkRepository;
import com.varshar.financial_ai_analyzer_backend.repository.DocumentRepository;
import com.varshar.financial_ai_analyzer_backend.service.DocumentService;
import com.varshar.financial_ai_analyzer_backend.service.PdfExtractionService;
import com.varshar.financial_ai_analyzer_backend.service.TextChunkingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import tools.jackson.databind.JsonNode;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class DocumentServiceImpl implements DocumentService {
    private static final Logger log = LoggerFactory.getLogger(DocumentServiceImpl.class);

    private final PdfExtractionService pdfExtractionService;
    private final DocumentRepository documentRepository;
    private final DocumentChunkRepository documentChunkRepository;
    private final TextChunkingService textChunkingService;

    public DocumentServiceImpl(PdfExtractionService pdfExtractionService, DocumentRepository documentRepository, DocumentChunkRepository documentChunkRepository, TextChunkingService textChunkingService) {
        this.pdfExtractionService = pdfExtractionService;
        this.documentRepository = documentRepository;
        this.documentChunkRepository = documentChunkRepository;
        this.textChunkingService = textChunkingService;
    }

    @Override
    public ResponseEntity<UploadedDocumentResponse> uploadDocuments(String authToken, MultipartFile file) {

        try {
            if(file.isEmpty())return null;
            String extractedText = pdfExtractionService.extractTextFromPdfFiles(file);

            Document document = new Document();

            document.setFileName(file.getOriginalFilename());
            document.setExtractedText(extractedText);
            document.setUploadedAt(LocalDateTime.now());
            document.setProcessedAt(LocalDateTime.now());
            document.setStatus(DocumentStatus.PROCESSED);

            Document savedDocument = documentRepository.save(document);

            List<String> chunks =
                    textChunkingService.chunkText(extractedText);

            List<DocumentChunk> documentChunks = new ArrayList<>();

            for (int i = 0; i < chunks.size(); i++) {

                DocumentChunk documentChunk = new DocumentChunk();

                documentChunk.setDocument(savedDocument);
                documentChunk.setChunkIndex(i);
                documentChunk.setContent(chunks.get(i));

                documentChunks.add(documentChunk);
            }

            documentChunkRepository.saveAll(documentChunks);
            UploadedDocumentResponse response = new UploadedDocumentResponse();
            response.setFileName(savedDocument.getFileName());
            response.setText(savedDocument.getExtractedText());
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            log.error("Exception while uploading document",e);
            return null;
        }

//        return null;
    }

    @Override
    public JsonNode extractStructuredData(String rawText) {

        return null;
    }
}
