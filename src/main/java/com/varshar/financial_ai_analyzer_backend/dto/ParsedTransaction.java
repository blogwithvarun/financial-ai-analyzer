package com.varshar.financial_ai_analyzer_backend.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ParsedTransaction(
        LocalDate transactionDate,
        String narration,
        BigDecimal withdrawal,
        BigDecimal deposit,
        BigDecimal closingBalance,
        String referenceNumber
) {
}