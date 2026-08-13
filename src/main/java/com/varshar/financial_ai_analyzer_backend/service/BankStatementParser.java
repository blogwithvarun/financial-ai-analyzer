package com.varshar.financial_ai_analyzer_backend.service;

import com.varshar.financial_ai_analyzer_backend.dto.ParsedTransaction;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class BankStatementParser {

    private static final Pattern TRANSACTION_START =
            Pattern.compile("^\\d{2}/\\d{2}/\\d{4}\\s+.*");

    private static final Pattern DATE_PATTERN =
            Pattern.compile("^(\\d{2}/\\d{2}/\\d{4})\\s+(.*)");

    private static final Pattern REFERENCE_PATTERN =
            Pattern.compile("\\bRef\\s*([A-Za-z0-9]+)", Pattern.CASE_INSENSITIVE);

    private static final Pattern AMOUNT_PATTERN =
            Pattern.compile("([\\d,]+\\.\\d{2})");

    public List<ParsedTransaction> parse(String text) {

        List<ParsedTransaction> transactions = new ArrayList<>();

        String[] lines = text.split("\\R");

        StringBuilder currentTransaction = new StringBuilder();

        for (String rawLine : lines) {

            String line = rawLine.trim();

            if (line.isEmpty()) {
                continue;
            }

            if (TRANSACTION_START.matcher(line).matches()) {

                if (!currentTransaction.isEmpty()) {
                    ParsedTransaction transaction =
                            parseTransaction(currentTransaction.toString());

                    if (transaction != null) {
                        transactions.add(transaction);
                    }
                }

                currentTransaction.setLength(0);
            }

            if (!currentTransaction.isEmpty()) {
                currentTransaction.append(" ");
            }

            currentTransaction.append(line);
        }

        // Process last transaction
        if (!currentTransaction.isEmpty()) {

            ParsedTransaction transaction =
                    parseTransaction(currentTransaction.toString());

            if (transaction != null) {
                transactions.add(transaction);
            }
        }

        return transactions;
    }

    private ParsedTransaction parseTransaction(String transaction) {

        Matcher dateMatcher =
                DATE_PATTERN.matcher(transaction);

        if (!dateMatcher.find()) {
            return null;
        }

        LocalDate transactionDate =
                LocalDate.parse(
                        dateMatcher.group(1),
                        DateTimeFormatter.ofPattern("dd/MM/yyyy")
                );

        String remainingText = dateMatcher.group(2);

        Matcher amountMatcher =
                AMOUNT_PATTERN.matcher(remainingText);

        List<String> amounts = new ArrayList<>();

        while (amountMatcher.find()) {
            amounts.add(amountMatcher.group(1));
        }

        /*
         * HDFC format:
         *
         * Withdrawals Deposits Closing Balance
         *
         * Example:
         *
         * 30,000.00 0.00 1,05,425.94
         */

        if (amounts.size() < 3) {
            return null;
        }

        int size = amounts.size();

        BigDecimal withdrawal =
                parseAmount(amounts.get(size - 3));

        BigDecimal deposit =
                parseAmount(amounts.get(size - 2));

        BigDecimal closingBalance =
                parseAmount(amounts.get(size - 1));

        String narration =
                remainingText.substring(
                        0,
                        findAmountStart(remainingText, amounts.get(size - 3))
                ).trim();

        String referenceNumber =
                extractReference(narration);

        return new ParsedTransaction(
                transactionDate,
                narration,
                withdrawal,
                deposit,
                closingBalance,
                referenceNumber
        );
    }

    private int findAmountStart(String text, String amount) {

        int index = text.lastIndexOf(amount);

        return index == -1 ? text.length() : index;
    }

    private BigDecimal parseAmount(String amount) {

        return new BigDecimal(
                amount.replace(",", "")
        );
    }

    private String extractReference(String narration) {

        Matcher matcher =
                REFERENCE_PATTERN.matcher(narration);

        return matcher.find()
                ? matcher.group(1)
                : null;
    }
}