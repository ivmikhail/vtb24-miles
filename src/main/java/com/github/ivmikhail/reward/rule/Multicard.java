package com.github.ivmikhail.reward.rule;

import com.github.ivmikhail.reward.Transaction;

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