package com.varshar.financial_ai_analyzer_backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "financial_transaction")
@Getter
@Setter
public class FinancialTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "document_id", nullable = false)
    private Document document;

    private LocalDate transactionDate;

    @Column(columnDefinition = "TEXT")
    private String narration;

    @Column(precision = 19, scale = 2)
    private BigDecimal withdrawal;

    @Column(precision = 19, scale = 2)
    private BigDecimal deposit;

    @Column(precision = 19, scale = 2)
    private BigDecimal closingBalance;

    private String referenceNumber;
}