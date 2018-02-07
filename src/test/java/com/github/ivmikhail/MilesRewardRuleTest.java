package com.github.ivmikhail;

import com.github.ivmikhail.reward.MilesRewardRule;
import com.github.ivmikhail.reward.RewardResult;
import com.github.ivmikhail.reward.TransactionRewardResult;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import static org.junit.Assert.assertEquals;

public class MilesRewardRuleTest {

    private MilesRewardRule rule;
    private Transaction transaction;

    @Before
    public void setUp() {
        Properties properties = new Properties();
        properties.setProperty("MilesRewardRule.transactions.ignore.description", "ignore, another ignore");
        properties.setProperty("MilesRewardRule.transactions.foreign.description", "foreign");

        rule = new MilesRewardRule(properties);

        transaction = new Transaction();
        transaction.setCardNumberMasked("123XXX1234");
        transaction.setDateTime(LocalDateTime.now());
        transaction.setProcessedDate(LocalDate.now());
        transaction.setAmount(new BigDecimal("-950"));
        transaction.setCurrencyCode("RUR");
        transaction.setAmountInAccountCurrency(new BigDecimal("-950"));
        transaction.setDescription("UBER RU JUN29 ABCD HELP;");
        transaction.setStatus("Исполнено");
    }

//    @Test
//    public void test() {
//        transaction.setAmountInAccountCurrency(new BigDecimal("-100.0"));
//        RewardResult result = rule.process(Collections.singletonList(transaction));
//
//        assertEquals(1, result.getTransactionsMap().values().size());
//        assertEquals(0, result.getBalanceRUR().compareTo(new BigDecimal("100.0")));
//
//    }

//    @Test
//    public void testLessThan100() {
//        transaction.setAmountInAccountCurrency(new BigDecimal("-95.0"));
//        RewardResult result = rule.process(Collections.singletonList(transaction));
//
//        assertTrue(result.isWithdraw());
//        assertFalse(result.isIgnore());
//        assertFalse(result.isForeign());
//        assertEquals(0, result.getMiles().compareTo(new BigDecimal("0")));
//
//    }

//    @Test
//    public void testRounding() {
//        transaction.setAmountInAccountCurrency(new BigDecimal("-195.0"));
//        RewardResult result = rule.process(Collections.singletonList(transaction));
//
//
//        assertTrue(result.isWithdraw());
//        assertFalse(result.isIgnore());
//        assertFalse(result.isForeign());
//        assertEquals(0, result.getMiles().compareTo(new BigDecimal("4")));
//
//    }

    @Test
    public void testForeign() {
        transaction.setCurrencyCode("USD");
        transaction.setAmountInAccountCurrency(new BigDecimal("-100.0"));
        RewardResult result = rule.process(Collections.singletonList(transaction));
        List<TransactionRewardResult> foreignTransactions = result.getTransactionsMap().get(Transaction.Type.WITHDRAW_FOREIGN);

        assertEquals(1, foreignTransactions.size());

    }

//    @Test
//    public void testForeignByDescription() {
//        transaction.setDescription("my foreign transaction");
//        transaction.setAmountInAccountCurrency(new BigDecimal("-100.0"));
//        RewardResult result = rule.process(Collections.singletonList(transaction));
//
//        assertEquals(0, result.getMiles().compareTo(new BigDecimal("5")));
//
//    }

    @Test
    public void testIgnoreByDescription() {
        transaction.setDescription("Ignore this transaction, please ignore");
        RewardResult result = rule.process(Collections.singletonList(transaction));

        assertEquals(0, result.getTotalRewardMiles().compareTo(new BigDecimal("0")));
    }
}