package com.github.ivmikhail.reward;

import com.github.ivmikhail.Transaction;

import java.util.List;
import java.util.Properties;
import java.util.Set;

public class RuleAfter01062017 extends MilesRewardRule {

    private Set<String> foreignTransactionWords;

    public RuleAfter01062017(Properties properties) {
        super(properties);
        foreignTransactionWords = getProperty(getClass().getSimpleName() + "description.foreignTransaction", properties);
    }

    @Override
    public RewardResult process(List<Transaction> transactionList) {
        return null;
    }

    @Override
    public TransactionRewardResult process(Transaction transaction) {
        return null;
    }
}
