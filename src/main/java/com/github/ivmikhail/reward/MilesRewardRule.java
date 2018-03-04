package com.github.ivmikhail.reward;

import com.github.ivmikhail.transactions.CSVLoader;
import com.github.ivmikhail.app.Settings;
import com.github.ivmikhail.transactions.Transaction;
import com.github.ivmikhail.fx.FxProvider;
import com.github.ivmikhail.fx.vtb.VTBFxProvider;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.*;

/**
 * Created by ivmikhail on 01/07/2017.
 */
public class MilesRewardRule {

    private static final String RUR = "RUR";
    private static final String COMMA = ",";
    private static final BigDecimal PERCENT_5 = new BigDecimal("0.05");
    private static final BigDecimal PERCENT_4 = new BigDecimal("0.04");

    private Set<String> ignoreWords;
    private Set<String> foreignTransactionWords;

    private FxProvider fxProvider;
    private Settings settings;

    public MilesRewardRule(Settings settings) {
        this.settings = settings;
        Properties properties = settings.getProperties();

        if(properties == null) {
            ignoreWords = new HashSet<>();
            foreignTransactionWords = new HashSet<>();
         } else {
            fxProvider = new VTBFxProvider(properties);
            ignoreWords = getPropertyAsSet("MilesRewardRule.transactions.ignore.description", properties);
            foreignTransactionWords = getPropertyAsSet("MilesRewardRule.transactions.foreign.description", properties);
        }
    }

    public void setFxProvider(FxProvider fxProvider) {
        this.fxProvider = fxProvider;
    }

    public RewardResult process() {
        return process(loadTransactions());
    }

    public RewardResult process(List<Transaction> transactions) {
        RewardResult result = new RewardResult();
        result.setSettings(settings);
        for (Transaction t : transactions) {
            result.add(process(t));
        }
        if (fxProvider != null) fxProvider.clear();
        return result;
    }

    private List<Transaction> loadTransactions() {
        try {
            return CSVLoader.load(settings);
        } catch (IOException | ParseException e) {
            throw new IllegalStateException(e);
        }
    }

    private TransactionReward process(final Transaction t) {
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

        TransactionReward result = new TransactionReward();
        result.setMiles(miles);
        result.setTransaction(t);
        result.setTransactionType(tType);
        return result;
    }

    private BigDecimal getAmountInRUR(Transaction t) {
        if (t.getAccountCurrencyCode().equals(RUR)) {
            return t.getAmountInAccountCurrency();
        } else {
            String baseCurrency = t.getAccountCurrencyCode();
            BigDecimal rate = fxProvider.getRate(baseCurrency, RUR, t.getProcessedDate());

            return t.getAmountInAccountCurrency().multiply(rate);
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