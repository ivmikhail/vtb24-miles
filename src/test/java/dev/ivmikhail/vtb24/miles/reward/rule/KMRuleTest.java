package dev.ivmikhail.vtb24.miles.reward.rule;

import dev.ivmikhail.vtb24.miles.reward.Transaction;
import dev.ivmikhail.vtb24.miles.statement.Operation;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Collections;

import static org.junit.Assert.assertEquals;

public class KMRuleTest {
    private KMRule rule;

    @Before
    public void setUp() {
        rule = new KMRule();
        rule.setName("KM");
        rule.setRewardPercent(new BigDecimal("0.04"));
        rule.setForeignRewardPercent(new BigDecimal("0.05"));
        rule.setForeignTransactionWords(Collections.singleton("foreign"));
    }

    @Test
    public void testNormal() {
        Operation op = new Operation();
        op.setCurrencyCode("RUR");
        op.setAccountCurrencyCode("RUR");
        op.setDescription("some transaction");
        Transaction t = new Transaction();
        t.setOperation(op);
        t.setAmountInRUR(new BigDecimal("-100"));

        Transaction.Reward r = rule.calculate(t);
        assertEquals(0, r.getMiles().compareTo(new BigDecimal("4")));
        assertEquals(0, r.getPercent().compareTo(new BigDecimal("0.04")));
    }

    @Test
    public void testForeignByDescription() {
        Operation op = new Operation();
        op.setCurrencyCode("RUR");
        op.setAccountCurrencyCode("RUR");
        op.setDescription("foreign transaction");
        Transaction t = new Transaction();
        t.setOperation(op);
        t.setAmountInRUR(new BigDecimal("-100"));

        Transaction.Reward r = rule.calculate(t);
        assertEquals(0, r.getMiles().compareTo(new BigDecimal("5")));
        assertEquals(0, r.getPercent().compareTo(new BigDecimal("0.05")));
    }

    @Test
    public void testForeignByCurrencyCode() {
        Operation op = new Operation();
        op.setCurrencyCode("GBP");
        op.setAccountCurrencyCode("USD");
        op.setDescription("some transaction");
        Transaction t = new Transaction();
        t.setOperation(op);
        t.setAmountInRUR(new BigDecimal("-100"));

        Transaction.Reward r = rule.calculate(t);
        assertEquals(0, r.getMiles().compareTo(new BigDecimal("5")));
        assertEquals(0, r.getPercent().compareTo(new BigDecimal("0.05")));
    }
}