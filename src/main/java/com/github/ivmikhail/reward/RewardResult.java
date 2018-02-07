package com.github.ivmikhail.reward;

import com.github.ivmikhail.Transaction;

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

    private Map<Transaction.Type, List<TransactionRewardResult>> transactionsMap;

    public RewardResult() {
        transactionsMap = new TreeMap<>();
    }

    public Map<Transaction.Type, List<TransactionRewardResult>> getTransactionsMap() {
        return transactionsMap;
    }

    public List<TransactionRewardResult> getTransactions(String transactionTypeName) {
        Transaction.Type type = Transaction.Type.valueOf(transactionTypeName);
        return transactionsMap.get(type);
    }

    public void add(TransactionRewardResult trr) {
        totalRewardMiles = totalRewardMiles.add(trr.getMiles());
        switch (trr.getTransactionType()) {
            case REFILL:
                totalRefillRUR = totalRefillRUR.add(trr.getTransaction().getAmountInAccountCurrency());
                break;
            default:
                totalWithdrawRUR = totalWithdrawRUR.add(trr.getTransaction().getAmountInAccountCurrency());
                break;
        }

        List<TransactionRewardResult> transactions = transactionsMap.getOrDefault(trr.getTransactionType(), new ArrayList<>());
        transactions.add(trr);
        transactionsMap.putIfAbsent(trr.getTransactionType(), transactions);
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
            return null;
        } else {
            return totalRewardMiles.divide(onePercent, 2, RoundingMode.DOWN);
        }
    }
}