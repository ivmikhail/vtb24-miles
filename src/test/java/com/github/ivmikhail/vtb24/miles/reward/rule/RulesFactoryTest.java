package com.github.ivmikhail.vtb24.miles.reward.rule;

import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Collections;

import static org.junit.Assert.assertTrue;

public class RulesFactoryTest {
    private RulesFactory factory;

    @Before
    public void setUp() {
        factory = new RulesFactory();
        factory.setWithdraw(new BigDecimal("-100"));
        factory.setForeignTransactionWords(Collections.singleton("foreign"));
    }

    @Test
    public void testNull() {
        RewardRule r = factory.create(null);
        assertTrue(r instanceof KMRule);
    }

    @Test
    public void testKMPlatinum() {
        RewardRule r = factory.create(RulesFactory.RuleId.KM_PLATINUM);
        assertTrue(r instanceof KMRule);
    }

    @Test
    public void testKMGold() {
        RewardRule r = factory.create(RulesFactory.RuleId.KM_GOLD);
        assertTrue(r instanceof KMRule);
    }

    @Test
    public void testMCCashback() {
        RewardRule r = factory.create(RulesFactory.RuleId.MC_CASHBACK);
        assertTrue(r instanceof Multicard);
    }

    @Test
    public void testMCTravel() {
        RewardRule r = factory.create(RulesFactory.RuleId.MC_TRAVEL);
        assertTrue(r instanceof Multicard);
    }
}