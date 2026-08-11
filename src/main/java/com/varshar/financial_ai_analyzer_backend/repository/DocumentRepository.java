package com.varshar.financial_ai_analyzer_backend.repository;

import com.varshar.financial_ai_analyzer_backend.entity.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {
}