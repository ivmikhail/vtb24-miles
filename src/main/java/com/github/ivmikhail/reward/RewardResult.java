package com.github.ivmikhail.reward;

import com.github.ivmikhail.app.Settings;
import com.github.ivmikhail.transactions.Transaction;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * Created by ivmikhail on 01/07/2017.
 */
public class RewardResult {
    private BigDecimal ONE_HUNDRED = new BigDecimal("100");

    private BigDecimal totalRewardMiles = BigDecimal.ZERO;
    private BigDecimal totalRefillRUR = BigDecimal.ZERO;
    private BigDecimal totalWithdrawRUR = BigDecimal.ZERO;

    private Map<Transaction.Type, List<TransactionReward>> transactionsMap;
    private Settings settings;

    public RewardResult() {
        transactionsMap = new TreeMap<>();
    }

    public Settings getSettings() {
        return settings;
    }

    public void setSettings(Settings settings) {
        this.settings = settings;
    }

    public Map<Transaction.Type, List<TransactionReward>> getTransactionsMap() {
        return transactionsMap;
    }

    public List<TransactionReward> getTransactions(String transactionTypeName) {
        Transaction.Type type = Transaction.Type.valueOf(transactionTypeName);
        return transactionsMap.getOrDefault(type, Collections.emptyList());
    }

    public void add(TransactionReward tr) {
        totalRewardMiles = totalRewardMiles.add(tr.getMiles());
        switch (tr.getTransactionType()) {
            case REFILL:
                totalRefillRUR = totalRefillRUR.add(tr.getTransaction().getAmountInAccountCurrency());
                break;
            default:
                totalWithdrawRUR = totalWithdrawRUR.add(tr.getTransaction().getAmountInAccountCurrency());
                break;
        }

        List<TransactionReward> transactions = transactionsMap.getOrDefault(tr.getTransactionType(), new ArrayList<>());
        transactions.add(tr);
        transactionsMap.putIfAbsent(tr.getTransactionType(), transactions);
    }

    public BigDecimal getTotalRefillRUR() {
        return totalRefillRUR;
    }

    public BigDecimal getTotalWithdrawRUR() {
        return totalWithdrawRUR;
    }

    public BigDecimal getTotalRewardMiles() {
        return totalRewardMiles;
    }

    public BigDecimal getEffectiveCashback() {
        //1 mile = 1 rub
        BigDecimal onePercent = totalWithdrawRUR.negate().divide(ONE_HUNDRED, RoundingMode.DOWN);
        if (onePercent.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        } else {
            return totalRewardMiles.divide(onePercent, 2, RoundingMode.DOWN);
        }
    }
}