package com.github.ivmikhail.reward;

import com.github.ivmikhail.app.Settings;
import com.github.ivmikhail.statement.Operation;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class RewardSummaryTest {

    @Test
    public void test() {
        RewardSummary result = processMockOperations();

        assertEquals(2, result.getTransactionsMap().values().size());
        assertEquals(0, result.getTotalRefillRUR().compareTo(new BigDecimal("50")));
        assertEquals(0, result.getTotalWithdrawRUR().compareTo(new BigDecimal("-100")));
    }

    private RewardSummary processMockOperations() {
        Calculator calculator = new Calculator(new Settings());
        List<Operation> operations = new ArrayList<>();

        Operation o1;
        o1 = new Operation();
        o1.setAccountNumberMasked("123XXX1234");
        o1.setDateTime(LocalDateTime.now());
        o1.setProcessedDate(LocalDate.now());
        o1.setAmount(new BigDecimal("-100"));
        o1.setCurrencyCode("RUR");
        o1.setAccountCurrencyCode("RUR");
        o1.setAmountInAccountCurrency(new BigDecimal("-100"));
        o1.setDescription("UBER RU JUN29 ABCD HELP;");
        o1.setStatus("Исполнено");

        Operation o2;
        o2 = new Operation();
        o2.setAccountNumberMasked("123XXX1234");
        o2.setDateTime(LocalDateTime.now());
        o2.setProcessedDate(LocalDate.now());
        o2.setAmount(new BigDecimal("50"));
        o2.setCurrencyCode("RUR");
        o2.setAccountCurrencyCode("RUR");
        o2.setAmountInAccountCurrency(new BigDecimal("50"));
        o2.setDescription("Пополнение по межбанку");
        o2.setStatus("Исполнено");

        operations.add(o1);
        operations.add(o2);

        return calculator.process(operations);
    }
}