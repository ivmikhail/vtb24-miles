package com.github.ivmikhail.vtb24.miles.reward.rule;

import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class RulesFactoryTest {
    private RulesFactory factory;

    @Before
    public void setUp() {
        factory = new RulesFactory();
        factory.setWithdrawAbs(new BigDecimal("-100"));
        factory.setForeignTransactionWords(Collections.singleton("foreign"));
    }

    @Test
    public void testNull() {
        RewardRule r = factory.create(null);
        assertTrue(r instanceof KMRule);
    }

    @Test
    public void testKMPlatinum() {
        KMRule platinum = (KMRule) factory.create(RulesFactory.RuleId.KM_PLATINUM);

        assertEquals(0, platinum.getRewardPercent().compareTo(new BigDecimal("0.04")));
        assertEquals(0, platinum.getForeignRewardPercent().compareTo(new BigDecimal("0.05")));
        assertEquals(1, platinum.getForeignTransactionWords().size());
    }

    @Test
    public void testKMGold() {
        KMRule gold = (KMRule) factory.create(RulesFactory.RuleId.KM_GOLD);

        assertEquals(0, gold.getRewardPercent().compareTo(new BigDecimal("0.02")));
        assertEquals(0, gold.getForeignRewardPercent().compareTo(new BigDecimal("0.03")));
        assertEquals(1, gold.getForeignTransactionWords().size());
    }

    @Test
    public void testCashbackLessThan5000() {
        factory.setWithdrawAbs(new BigDecimal("2000"));
        Multicard multicard = (Multicard) factory.create(RulesFactory.RuleId.MC_CASHBACK);

        assertEquals(0, multicard.getRewardPercent().compareTo(BigDecimal.ZERO));
    }

    @Test
    public void testCashback7000() {
        factory.setWithdrawAbs(new BigDecimal("7000"));
        Multicard multicard = (Multicard) factory.create(RulesFactory.RuleId.MC_CASHBACK);

        assertEquals(0, multicard.getRewardPercent().compareTo(new BigDecimal("0.01")));
    }

    @Test
    public void testCashback16000() {
        factory.setWithdrawAbs(new BigDecimal("16000"));
        Multicard multicard = (Multicard) factory.create(RulesFactory.RuleId.MC_CASHBACK);

        assertEquals(0, multicard.getRewardPercent().compareTo(new BigDecimal("0.015")));
    }

    @Test
    public void testCashback100000() {
        factory.setWithdrawAbs(new BigDecimal("100000"));
        Multicard multicard = (Multicard) factory.create(RulesFactory.RuleId.MC_CASHBACK);

        assertEquals(0, multicard.getRewardPercent().compareTo(new BigDecimal("0.02")));
    }

    @Test
    public void testMCTravelLessThan5000() {
        factory.setWithdrawAbs(new BigDecimal("2000"));
        Multicard multicard = (Multicard) factory.create(RulesFactory.RuleId.MC_TRAVEL);

        assertEquals(0, multicard.getRewardPercent().compareTo(BigDecimal.ZERO));
    }

    @Test
    public void testMCTravel7000() {
        factory.setWithdrawAbs(new BigDecimal("7000"));
        Multicard multicard = (Multicard) factory.create(RulesFactory.RuleId.MC_TRAVEL);

        assertEquals(0, multicard.getRewardPercent().compareTo(new BigDecimal("0.01")));
    }

    @Test
    public void testMCTravel16000() {
        factory.setWithdrawAbs(new BigDecimal("16000"));
        Multicard multicard = (Multicard) factory.create(RulesFactory.RuleId.MC_TRAVEL);

        assertEquals(0, multicard.getRewardPercent().compareTo(new BigDecimal("0.02")));
    }

    @Test
    public void testMCTravel100000() {
        factory.setWithdrawAbs(new BigDecimal("100000"));
        Multicard multicard = (Multicard) factory.create(RulesFactory.RuleId.MC_TRAVEL);

        assertEquals(0, multicard.getRewardPercent().compareTo(new BigDecimal("0.04")));
    }
}