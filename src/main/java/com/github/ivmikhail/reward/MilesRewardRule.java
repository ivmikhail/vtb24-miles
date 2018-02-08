package com.github.ivmikhail.reward;

import com.github.ivmikhail.fx.FakeFxProvider;
import com.github.ivmikhail.Transaction;
import com.github.ivmikhail.fx.FxProvider;

import java.math.BigDecimal;
import java.util.*;
import java.util.logging.Logger;

/**
 * Created by ivmikhail on 01/07/2017.
 */
public class MilesRewardRule {
    private static final Logger LOG = Logger.getLogger(FakeFxProvider.class.getName());

    private static final String RUR = "RUR";
    private static final String COMMA = ",";
    private static final BigDecimal PERCENT_5 = new BigDecimal("0.05");
    private static final BigDecimal PERCENT_4 = new BigDecimal("0.04");

    private Set<String> ignoreWords;
    private Set<String> foreignTransactionWords;

    private FxProvider fxProvider;

    public MilesRewardRule(Properties properties) {
        ignoreWords = getPropertyAsSet("MilesRewardRule.transactions.ignore.description", properties);
        foreignTransactionWords = getPropertyAsSet("MilesRewardRule.transactions.foreign.description", properties);
    }

    public void setCcyConverter(FxProvider fxProvider) {
        this.fxProvider = fxProvider;
    }

    public RewardResult process(List<Transaction> transactionList) {
        RewardResult result = new RewardResult();
        for (Transaction t : transactionList) {
            result.add(process(t));
        }
        return result;
    }

    private TransactionRewardResult process(final Transaction t) {
        Transaction.Type tType = determineType(t);
        BigDecimal miles = BigDecimal.ZERO;

        boolean calculateMiles = tType == Transaction.Type.WITHDRAW_NORMAL || tType == Transaction.Type.WITHDRAW_FOREIGN;

        if (calculateMiles) {
            BigDecimal cashbackPercent = getCashbackPercent(tType);
            BigDecimal amountInRUR = getAmountInRUR(t);

            //4% каждые 100 руб, округление: 199 -> 100, 201 -> 200  и т.д
            amountInRUR = amountInRUR.negate().setScale(-2, BigDecimal.ROUND_DOWN);

            miles = amountInRUR.multiply(cashbackPercent);
        }

        TransactionRewardResult result = new TransactionRewardResult();
        result.setMiles(miles);
        result.setTransaction(t);
        result.setTransactionType(tType);
        return result;
    }

    private BigDecimal getAmountInRUR(Transaction t) {
        if (t.getAccountCurrencyCode().equals(RUR)) {
            return t.getAmountInAccountCurrency();
        } else {
            String base = t.getAccountCurrencyCode();
            String quote = RUR;

            BigDecimal rate = fxProvider.getRate(
                    base,
                    quote,
                    t.getProcessedDate());

            BigDecimal amountInRUR = t.getAmountInAccountCurrency().multiply(rate);

            LOG.info(String.format("%s%s on %s is %s",
                    base,
                    quote,
                    t.getProcessedDate().toString(),
                    rate.toString()
            ));
            return amountInRUR;
        }
    }

    private Set<String> getPropertyAsSet(String key, Properties properties) {
        String[] arr = properties.getProperty(key, "").split(COMMA);
        Set<String> set = new HashSet<>();
        for (String s : arr) {
            if (!s.trim().isEmpty()) {
                set.add(s.trim());
            }
        }
        return set;
    }

    private Transaction.Type determineType(Transaction t) {
        if (t.getAmountInAccountCurrency().signum() == 1) {
            return Transaction.Type.REFILL;
        } else if (isIgnore(t)) {
            return Transaction.Type.WITHDRAW_IGNORE;
        } else if (isForeign(t)) {
            return Transaction.Type.WITHDRAW_FOREIGN;
        } else {
            return Transaction.Type.WITHDRAW_NORMAL;
        }
    }

    private boolean isForeign(Transaction t) {
        if (!t.getCurrencyCode().equals(t.getAccountCurrencyCode())) return true;

        for (String word : foreignTransactionWords) {//Для убера
            if (t.getDescription().toLowerCase().contains(word.toLowerCase())) return true;
        }

        return false;
    }

    private boolean isIgnore(Transaction t) {
        for (String word : ignoreWords) {
            if (t.getDescription().toLowerCase().contains(word.toLowerCase())) return true;
        }
        return false;
    }

    private BigDecimal getCashbackPercent(Transaction.Type type) {
        switch (type) {
            case WITHDRAW_NORMAL:
                return PERCENT_4;
            case WITHDRAW_FOREIGN:
                return PERCENT_5;
            default:
                throw new IllegalArgumentException("There are no cashback for " + type);
        }
    }
}