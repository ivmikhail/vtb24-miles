package com.github.ivmikhail.vtb24.miles.reward.rule;

import com.github.ivmikhail.vtb24.miles.reward.Transaction;

import java.math.BigDecimal;

public class Multicard extends RewardRule {
    Multicard() {
    }

    @Override
    public Transaction.Reward calculate(Transaction t) {
        BigDecimal amount = getRoundedAmountInRUR(t);

        Transaction.Reward r = new Transaction.Reward();
        r.setPercent(rewardPercent);
        r.setMiles(amount.multiply(rewardPercent));
        return r;
    }
}