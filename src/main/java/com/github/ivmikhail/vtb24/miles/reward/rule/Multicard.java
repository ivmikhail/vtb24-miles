package com.github.ivmikhail.vtb24.miles.reward.rule;

import com.github.ivmikhail.vtb24.miles.reward.Transaction;

public class Multicard extends RewardRule {
    Multicard() {
    }

    @Override
    public Transaction.Reward calculate(Transaction t) {
        Transaction.Reward reward = new Transaction.Reward();
        reward.setPercent(this.rewardPercent);
        reward.setMiles(null);//there are no miles for each transaction
        return reward;
    }

    @Override
    public boolean calcMilesForEachTransaction() {
        return false;
    }
}