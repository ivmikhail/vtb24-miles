package com.github.ivmikhail.vtb24.miles.reward.rule;

import com.github.ivmikhail.vtb24.miles.reward.Transaction;
import com.github.ivmikhail.vtb24.miles.statement.Operation;

import java.math.BigDecimal;
import java.util.Set;

public class KMRule extends RewardRule {
    private BigDecimal foreignRewardPercent;
    private Set<String> foreignTransactionWords;

    KMRule() {
    }

    public BigDecimal getForeignRewardPercent() {
        return foreignRewardPercent;
    }

    public Set<String> getForeignTransactionWords() {
        return foreignTransactionWords;
    }

    public void setForeignTransactionWords(Set<String> foreignTransactionWords) {
        this.foreignTransactionWords = foreignTransactionWords;
    }

    public void setForeignRewardPercent(BigDecimal foreignRewardPercent) {
        this.foreignRewardPercent = foreignRewardPercent;
    }

    @Override
    public Transaction.Reward calculate(Transaction t) {
        BigDecimal rewardPercent = isForeign(t) ? foreignRewardPercent : this.rewardPercent;
        BigDecimal amount = getRoundedAmountInRUR(t);

        Transaction.Reward r = new Transaction.Reward();
        r.setPercent(rewardPercent);
        r.setMiles(amount.multiply(rewardPercent));
        return r;
    }

    @Override
    public boolean calcMilesForEachTransaction() {
        return true;
    }

    private boolean isForeign(Transaction t) {
        Operation op = t.getOperation();
        if (!op.getCurrencyCode().equals(op.getAccountCurrencyCode())) {
            return true;
        }

        for (String word : foreignTransactionWords) {
            if (op.getDescription().toLowerCase().contains(word.toLowerCase())) {
                return true;
            }
        }

        return false;
    }
}