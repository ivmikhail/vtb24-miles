package com.github.ivmikhail;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.github.ivmikhail.reward.TransactionRewardResult;
import com.github.ivmikhail.reward.RuleBefore01062017;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Properties;

/**
 * Created by ivmikhail on 01/07/2017.
 */
public class RuleBefore01062017Test {
    private RuleBefore01062017 rule;
    private Transaction transaction;

    @Before
    public void setUp() {
        Properties properties = new Properties();
        properties.setProperty("RuleBefore01062017.description.ignore", "ignore, another ignore");
        properties.setProperty("RuleBefore01062017.description.x2Miles", "x2,x3");

        rule = new RuleBefore01062017(properties);

        transaction = new Transaction();
        transaction.setCardNumberMasked("123XXX1234");
        transaction.setDateTime(LocalDateTime.now());
        transaction.setProcessedDate(LocalDate.now());
        transaction.setAmount(new BigDecimal("-95"));
        transaction.setCurrencyCode("RUR");
        transaction.setAmountInAccountCurrency(new BigDecimal("-95"));
        transaction.setDescription("UBER RU JUN29 ABCD HELP;");
        transaction.setStatus("Исполнено");
    }

    @Test
    public void test() {
        TransactionRewardResult result = rule.process(transaction);

        assertTrue(result.isWithdraw());
        assertFalse(result.isIgnore());
        assertFalse(result.isX2());

        assertEquals(new BigDecimal("8"), result.getMiles());
    }

    @Test
    public void testX2MilesForForeignTransaction() {
        transaction.setCurrencyCode("USD");
        TransactionRewardResult result = rule.process(transaction);

        assertTrue(result.isWithdraw());
        assertFalse(result.isIgnore());
        assertTrue(result.isX2());

        assertEquals(new BigDecimal("16"), result.getMiles());
    }

    @Test
    public void testIgnoreTransactionByDescription() {
        transaction.setDescription("Ignore this transaction, please ignore");
        TransactionRewardResult result = rule.process(transaction);

        assertTrue(result.isWithdraw());
        assertTrue(result.isIgnore());
        assertFalse(result.isX2());

        assertEquals(new BigDecimal("0"), result.getMiles());

    }

    @Test
    public void testX2MilesByDescription() {
        transaction.setDescription("x2 description");
        TransactionRewardResult result = rule.process(transaction);

        assertTrue(result.isWithdraw());
        assertFalse(result.isIgnore());
        assertTrue(result.isX2());

        assertEquals(new BigDecimal("16"), result.getMiles());
    }
}
