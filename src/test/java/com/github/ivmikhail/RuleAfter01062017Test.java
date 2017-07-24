package com.github.ivmikhail;

import com.github.ivmikhail.reward.RuleAfter01062017;
import com.github.ivmikhail.reward.TransactionRewardResult;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class RuleAfter01062017Test {

    private RuleAfter01062017 rule;
    private Transaction transaction;

    @Before
    public void setUp() {
        Properties properties = new Properties();
        properties.setProperty("MilesRewardRule.description.ignore", "ignore, another ignore");
        properties.setProperty("RuleAfter01062017.description.foreignTransaction", "foreign");

        rule = new RuleAfter01062017(properties);

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

    @Test
    public void test() {
        transaction.setAmountInAccountCurrency(new BigDecimal("-100.0"));
        TransactionRewardResult result = rule.process(transaction);

        assertTrue(result.isWithdraw());
        assertFalse(result.isIgnore());
        assertFalse(result.isForeign());
        assertEquals(0, result.getMiles().compareTo(new BigDecimal("4")));

    }

    @Test
    public void testLessThan100() {
        transaction.setAmountInAccountCurrency(new BigDecimal("-95.0"));
        TransactionRewardResult result = rule.process(transaction);

        assertTrue(result.isWithdraw());
        assertFalse(result.isIgnore());
        assertFalse(result.isForeign());
        assertEquals(0, result.getMiles().compareTo(new BigDecimal("0")));

    }

    @Test
    public void testRounding() {
        transaction.setAmountInAccountCurrency(new BigDecimal("-195.0"));
        TransactionRewardResult result = rule.process(transaction);

        assertTrue(result.isWithdraw());
        assertFalse(result.isIgnore());
        assertFalse(result.isForeign());
        assertEquals(0, result.getMiles().compareTo(new BigDecimal("4")));

    }

    @Test
    public void testForeign() {
        transaction.setCurrencyCode("USD");
        transaction.setAmountInAccountCurrency(new BigDecimal("-100.0"));
        TransactionRewardResult result = rule.process(transaction);

        assertTrue(result.isWithdraw());
        assertFalse(result.isIgnore());
        assertTrue(result.isForeign());
        assertEquals(0, result.getMiles().compareTo(new BigDecimal("5")));

    }

    @Test
    public void testForeignByDescription() {
        transaction.setDescription("my foreign transaction");
        transaction.setAmountInAccountCurrency(new BigDecimal("-100.0"));
        TransactionRewardResult result = rule.process(transaction);

        assertTrue(result.isWithdraw());
        assertFalse(result.isIgnore());
        assertTrue(result.isForeign());
        assertEquals(0, result.getMiles().compareTo(new BigDecimal("5")));

    }

    @Test
    public void testIgnoreByDescription() {
        transaction.setDescription("Ignore this transaction, please ignore");
        TransactionRewardResult result = rule.process(transaction);

        assertTrue(result.isWithdraw());
        assertTrue(result.isIgnore());
        assertFalse(result.isForeign());
        assertEquals(0, result.getMiles().compareTo(new BigDecimal("0")));
    }
}