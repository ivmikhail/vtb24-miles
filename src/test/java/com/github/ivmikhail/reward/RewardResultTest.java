package com.github.ivmikhail.reward;

import com.github.ivmikhail.app.Settings;
import com.github.ivmikhail.transactions.Transaction;

import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class RewardResultTest {

    @Test
    public void testZeroEffectiveCashback() {
        RewardResult reward = new RewardResult();
        assertEquals(0, BigDecimal.ZERO.compareTo(reward.getEffectiveCashback()));
    }

    @Test
    public void test() {
        RewardResult result = createMockResult();

        assertEquals(2, result.getTransactionsMap().values().size());

        assertEquals(0, result.getTotalRefillRUR().compareTo(new BigDecimal("50")));
        assertEquals(0, result.getTotalWithdrawRUR().compareTo(new BigDecimal("-100")));
        assertEquals(0, result.getTotalRewardMiles().compareTo(new BigDecimal("4")));
        assertEquals(0, result.getEffectiveCashback().compareTo(new BigDecimal("4")));
    }

    private RewardResult createMockResult() {
        MilesRewardRule rule = new MilesRewardRule(new Settings());
        List<Transaction> transactions = new ArrayList<>();

        Transaction t1;
        t1 = new Transaction();
        t1.setAccountNumberMasked("123XXX1234");
        t1.setDateTime(LocalDateTime.now());
        t1.setProcessedDate(LocalDate.now());
        t1.setAmount(new BigDecimal("-100"));
        t1.setCurrencyCode("RUR");
        t1.setAccountCurrencyCode("RUR");
        t1.setAmountInAccountCurrency(new BigDecimal("-100"));
        t1.setDescription("UBER RU JUN29 ABCD HELP;");
        t1.setStatus("Исполнено");

        Transaction t2;
        t2 = new Transaction();
        t2.setAccountNumberMasked("123XXX1234");
        t2.setDateTime(LocalDateTime.now());
        t2.setProcessedDate(LocalDate.now());
        t2.setAmount(new BigDecimal("50"));
        t2.setCurrencyCode("RUR");
        t2.setAccountCurrencyCode("RUR");
        t2.setAmountInAccountCurrency(new BigDecimal("50"));
        t2.setDescription("Пополнение по межбанку");
        t2.setStatus("Исполнено");

        transactions.add(t1);
        transactions.add(t2);

        return rule.process(transactions);
    }
}