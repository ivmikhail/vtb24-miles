package com.github.ivmikhail.reward;

import com.github.ivmikhail.transactions.Transaction;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;

/**
 * Created by ivmikhail on 01/07/2017.
 */
public class RewardResult {
    private BigDecimal ONE_HUNDRED = new BigDecimal("100");

    private BigDecimal totalRewardMiles;
    private BigDecimal totalRefillRUR;
    private BigDecimal totalWithdrawRUR;

    private LocalDate minDate;
    private LocalDate maxDate;

    private Map<Transaction.Type, List<TransactionReward>> transactionsMap;

    public RewardResult() {
        transactionsMap = new TreeMap<>();
        totalRewardMiles = BigDecimal.ZERO;
        totalRefillRUR = BigDecimal.ZERO;
        totalWithdrawRUR = BigDecimal.ZERO;
    }

    public LocalDate getMinDate() {
        return minDate;
    }

    public void setMinDate(LocalDate minDate) {
        this.minDate = minDate;
    }

    public LocalDate getMaxDate() {
        return maxDate;
    }

    public void setMaxDate(LocalDate maxDate) {
        this.maxDate = maxDate;
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