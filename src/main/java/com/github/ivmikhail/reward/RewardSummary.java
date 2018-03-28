package com.github.ivmikhail.reward;

import com.github.ivmikhail.reward.rule.RewardRule;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ivmikhail on 01/07/2017.
 */
public class RewardSummary {
    private LocalDate minDate;
    private LocalDate maxDate;
    private RewardRule rule;
    private BigDecimal totalRefillRUR;
    private BigDecimal totalWithdrawRUR;
    private BigDecimal totalMiles;
    private Map<Transaction.Type, List<Transaction>> transactionsMap;

    public RewardSummary(BigDecimal totalWithdrawRUR, RewardRule rule) {
        this.rule = rule;
        this.totalMiles = BigDecimal.ZERO;
        this.totalWithdrawRUR = totalWithdrawRUR;
        this.totalRefillRUR = BigDecimal.ZERO;
        this.transactionsMap = new HashMap<>();
    }

    public BigDecimal getTotalRefillRUR() {
        return totalRefillRUR;
    }

    public BigDecimal getTotalWithdrawRUR() {
        return totalWithdrawRUR;
    }

    public BigDecimal getTotalMiles() {
        return totalMiles;
    }

    public RewardRule getRule() {
        return rule;
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

    public Map<Transaction.Type, List<Transaction>> getTransactionsMap() {
        return transactionsMap;
    }

    public void add(Transaction t) {
        appendToTransactions(t);
        appendToTotals(t);
        appendToMiles(t);
    }

    private void appendToTransactions(Transaction t) {
        List<Transaction> list = transactionsMap.getOrDefault(t.getType(), new ArrayList<>());
        list.add(t);
        transactionsMap.putIfAbsent(t.getType(), list);
    }

    private void appendToTotals(Transaction t) {
        if (t.getType() == Transaction.Type.REFILL) {
            totalRefillRUR = totalRefillRUR.add(t.getAmountInRUR());
        }
    }

    private void appendToMiles(Transaction t) {
        Transaction.Reward r = t.getReward();
        if (r == null) return;

        BigDecimal miles = r.getMiles();
        if (miles != null) {
            totalMiles = totalMiles.add(miles);
        }
    }
}