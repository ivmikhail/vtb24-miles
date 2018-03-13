package com.github.ivmikhail.reward.rule;

import java.math.BigDecimal;
import java.util.Set;

public class RulesFactory {

    public RewardRule createKMPlatinum(Set<String> foreignTransactionWords) {
        KMRule km = new KMRule();
        km.setName("КМ P");
        km.setRewardPercent(new BigDecimal("0.04"));
        km.setForeignTransactionWords(foreignTransactionWords);
        km.setForeignRewardPercent(new BigDecimal("0.05"));

        return km;
    }


    public RewardRule createKMGold(Set<String> foreignTransactionWords) {
        KMRule km = new KMRule();
        km.setName("КМ G");
        km.setRewardPercent(new BigDecimal("0.02"));
        km.setForeignTransactionWords(foreignTransactionWords);
        km.setForeignRewardPercent(new BigDecimal("0.03"));

        return km;
    }

    public RewardRule createCashback(BigDecimal withdraw) {

        BigDecimal reward = getReward(withdraw,
                new BigDecimal("0.01"),
                new BigDecimal("0.015"),
                new BigDecimal("0.02"));

        Multicard cashback = new Multicard();
        cashback.setName("М CB");
        cashback.setRewardPercent(reward);

        return cashback;
    }

    public RewardRule createTravel(BigDecimal withdraw) {

        BigDecimal reward = getReward(withdraw,
                new BigDecimal("0.01"),
                new BigDecimal("0.02"),
                new BigDecimal("0.04"));

        Multicard travel = new Multicard();
        travel.setName("М TRVL/C");
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