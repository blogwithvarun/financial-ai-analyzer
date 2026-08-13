package com.varshar.financial_ai_analyzer_backend.repository;

import com.varshar.financial_ai_analyzer_backend.entity.FinancialTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
@Repository
public interface FinancialTransactionRepository
        extends JpaRepository<FinancialTransaction, Long> {

    @Query("""
        SELECT COALESCE(SUM(t.withdrawal), 0)
        FROM FinancialTransaction t
        WHERE t.document.id = :documentId
    """)
    BigDecimal getTotalSpending(@Param("documentId") Long documentId);

    @Query("""
        SELECT COALESCE(SUM(t.deposit), 0)
        FROM FinancialTransaction t
        WHERE t.document.id = :documentId
    """)
    BigDecimal getTotalDeposits(@Param("documentId") Long documentId);
}