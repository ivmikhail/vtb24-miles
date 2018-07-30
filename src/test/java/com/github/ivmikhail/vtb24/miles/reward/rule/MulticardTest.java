package com.github.ivmikhail.vtb24.miles.reward.rule;

import com.github.ivmikhail.vtb24.miles.reward.Transaction;
import com.github.ivmikhail.vtb24.miles.statement.Operation;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;

public class MulticardTest {
    private Multicard rule;

    @Before
    public void setUp() {
        rule = new Multicard();
        rule.setName("Multicard");
        rule.setRewardPercent(new BigDecimal("0.01"));
    }

    @Test
    public void test() {
        Transaction t = new Transaction();
        t.setOperation(new Operation());
        t.setAmountInRUR(new BigDecimal("-100"));

        Transaction.Reward r = rule.calculate(t);
        assertEquals(0, new BigDecimal("1").compareTo(r.getMiles()));
        assertEquals(0, new BigDecimal("0.01").compareTo(r.getPercent()));
    }
}