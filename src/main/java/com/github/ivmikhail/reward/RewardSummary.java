package com.github.ivmikhail.reward;

import com.github.ivmikhail.reward.rule.RewardRule;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

/**
 * Created by ivmikhail on 01/07/2017.
 */
public class RewardSummary {
    private BigDecimal ONE_HUNDRED = new BigDecimal("100");

    private LocalDate minDate;
    private LocalDate maxDate;
    private RewardRule[] rules;
    private BigDecimal totalRefillRUR;
    private BigDecimal totalWithdrawRUR;
    private BigDecimal[] totalMiles;
    private Map<Transaction.Type, List<Transaction>> transactionsMap;

    public RewardSummary(BigDecimal totalWithdrawRUR, RewardRule[] rules) {
        this.rules = rules;
        this.totalMiles = new BigDecimal[rules.length];
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

    public BigDecimal[] getTotalMiles() {
        return totalMiles;
    }

    public RewardRule[] getRules() {
        return rules;
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
        //total miles calculation for every rule
        Transaction.Reward[] rewards = t.getRewards();
        Transaction.Reward r;
        for (int i = 0; i < rewards.length; i++) {
            r = rewards[i];
            if (r == null) continue; //no reward for rules[i], do nothing

            BigDecimal miles = totalMiles[i];
            if (miles == null) {
                totalMiles[i] = r.getMiles();
            } else {
                totalMiles[i] = miles.add(r.getMiles());
            }
        }
    }
}