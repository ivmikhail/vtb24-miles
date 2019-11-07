package dev.ivmikhail.vtb24.miles.reward.rule;

import dev.ivmikhail.vtb24.miles.reward.Transaction;

import java.math.BigDecimal;

public abstract class RewardRule {
    protected String name;
    protected BigDecimal rewardPercent;

    RewardRule() {
    }

    public BigDecimal getRewardPercent() {
        return rewardPercent;
    }

    public void setRewardPercent(BigDecimal rewardPercent) {
        this.rewardPercent = rewardPercent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * Calculate miles/bonus for given transaction
     *
     * @param t bank transaction
     * @return miles/bonus value
     */
    public abstract Transaction.Reward calculate(Transaction t);

    public abstract boolean isSpecialTotalMilesCalc();

    protected BigDecimal getRoundedAmountInRUR(Transaction t) {

        //4% каждые 100 руб, округление: 199 -> 100, 201 -> 200  и т.д
        return t.getAmountInRUR()
                .negate()
                .setScale(-2, BigDecimal.ROUND_DOWN);
    }
}