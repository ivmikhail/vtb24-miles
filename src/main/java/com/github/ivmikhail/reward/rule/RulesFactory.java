package com.github.ivmikhail.reward.rule;

import java.math.BigDecimal;
import java.util.Set;

import static com.github.ivmikhail.reward.rule.RulesFactory.RuleId.*;

public class RulesFactory {
    public enum RuleId {KM_PLATINUM, KM_GOLD, MC_TRAVEL, MC_CASHBACK}

    private BigDecimal withdraw;
    private Set<String> foreignTransactionWords;

    public void setWithdraw(BigDecimal withdraw) {
        this.withdraw = withdraw;
    }

    public void setForeignTransactionWords(Set<String> foreignTransactionWords) {
        this.foreignTransactionWords = foreignTransactionWords;
    }

    public RewardRule create(RuleId id) {
        if (id == null) return createKMPlatinum();

        if (id == KM_GOLD) return createKMGold();
        if (id == MC_TRAVEL) return createTravel();
        if (id == MC_CASHBACK) return createCashback();

        return createKMPlatinum();
    }

    private RewardRule createKMPlatinum() {
        KMRule km = new KMRule();
        km.setName("Карта Мира Platinum");
        km.setRewardPercent(new BigDecimal("0.04"));
        km.setForeignTransactionWords(foreignTransactionWords);
        km.setForeignRewardPercent(new BigDecimal("0.05"));

        return km;
    }


    private RewardRule createKMGold() {
        KMRule km = new KMRule();
        km.setName("Карта Мира Gold");
        km.setRewardPercent(new BigDecimal("0.02"));
        km.setForeignTransactionWords(foreignTransactionWords);
        km.setForeignRewardPercent(new BigDecimal("0.03"));

        return km;
    }

    private RewardRule createCashback() {
        BigDecimal reward = getReward(withdraw,
                new BigDecimal("0.01"),
                new BigDecimal("0.015"),
                new BigDecimal("0.02"));

        Multicard cashback = new Multicard();
        cashback.setName("Мультикарта Cashback");
        cashback.setRewardPercent(reward);

        return cashback;
    }

    private RewardRule createTravel() {

        BigDecimal reward = getReward(withdraw,
                new BigDecimal("0.01"),
                new BigDecimal("0.02"),
                new BigDecimal("0.04"));

        Multicard travel = new Multicard();
        travel.setName("Мльтикарта Travel/Коллекция");
        travel.setRewardPercent(reward);

        return travel;
    }

    private BigDecimal getReward(BigDecimal withdraw,
                                 BigDecimal rewardLow,
                                 BigDecimal rewardMedium,
                                 BigDecimal rewardHigh) {
        BigDecimal rur5000 = new BigDecimal("5000");
        BigDecimal rur15000 = new BigDecimal("15000");
        BigDecimal rur75000 = new BigDecimal("75000");

        if (withdraw.compareTo(rur5000) < 0) return BigDecimal.ZERO;
        if (withdraw.compareTo(rur5000) >= 0 && withdraw.compareTo(rur15000) < 0) return rewardLow;
        if (withdraw.compareTo(rur15000) >= 0 && withdraw.compareTo(rur75000) < 0) return rewardMedium;

        return rewardHigh;
    }
}