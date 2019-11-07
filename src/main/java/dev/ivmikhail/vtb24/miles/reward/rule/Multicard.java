package dev.ivmikhail.vtb24.miles.reward.rule;

import dev.ivmikhail.vtb24.miles.reward.Transaction;

import java.math.BigDecimal;

public class Multicard extends RewardRule {
    Multicard() {
    }

    @Override
    public Transaction.Reward calculate(Transaction t) {
        BigDecimal amount = t.getAmountInRUR().negate();

        Transaction.Reward reward = new Transaction.Reward();
        reward.setPercent(this.rewardPercent);
        reward.setMiles(amount.multiply(rewardPercent));
        return reward;
    }

    @Override
    public boolean isSpecialTotalMilesCalc() {
        return true;
    }
}