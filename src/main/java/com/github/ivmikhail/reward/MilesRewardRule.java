package com.github.ivmikhail.reward;

import com.github.ivmikhail.Transaction;

import java.util.*;

/**
 * Created by ivmikhail on 01/07/2017.
 */
public abstract class MilesRewardRule {
    private static final String RUR = "RUR";
    private static final String COMMA = ",";

    private Set<String> ignoreWords;

    public MilesRewardRule(Properties properties) {
        ignoreWords = getProperty(MilesRewardRule.class.getSimpleName() + ".description.ignore", properties);
    }

    public abstract RewardResult process(List<Transaction> transactionList);

    public abstract TransactionRewardResult process(Transaction transaction);

    protected Set<String> getProperty(String key, Properties properties) {
        String[] arr = properties.getProperty(key).split(COMMA);
        Set<String> set = new HashSet<>();
        for (String s : arr) set.add(s.trim());
        return set;
    }

    protected Transaction.Type determineType(Transaction t) {
        if (t.getAmountInAccountCurrency().signum() == 1) {
            return Transaction.Type.REFILL;
        } else {
            return Transaction.Type.WITHDRAW;
        }
    }

    protected boolean isForeign(Transaction t) {
        return !t.getCurrencyCode().equals(RUR);
    }

    protected boolean isIgnore(Transaction transaction) {
        for (String word : ignoreWords) {
            if (transaction.getDescription().contains(word)) return true;
        }
        return false;
    }
}