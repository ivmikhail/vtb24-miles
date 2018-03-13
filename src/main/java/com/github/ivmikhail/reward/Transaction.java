package com.github.ivmikhail.reward;

import com.github.ivmikhail.fx.FxRate;
import com.github.ivmikhail.statement.Operation;

import java.math.BigDecimal;

public class Transaction {
    public enum Type {
        REFILL,
        WITHDRAW,
        WITHDRAW_IGNORE
    }

    public static class Reward {
        private BigDecimal percent;
        private BigDecimal miles;

        public BigDecimal getPercent() {
            return percent;
        }

        public void setPercent(BigDecimal percent) {
            this.percent = percent;
        }

        public BigDecimal getMiles() {
            return miles;
        }

        public void setMiles(BigDecimal miles) {
            this.miles = miles;
        }
    }

    private Operation operation;
    private Type type;
    private FxRate accountCurrencyRate;
    private BigDecimal amountInRUR;
    private Reward[] rewards;

    public Reward[] getRewards() {
        return rewards;
    }

    public void setRewards(Reward[] rewards) {
        this.rewards = rewards;
    }

    public FxRate getAccountCurrencyRate() {
        return accountCurrencyRate;
    }

    public void setAccountCurrencyRate(FxRate accountCurrencyRate) {
        this.accountCurrencyRate = accountCurrencyRate;
    }

    public Operation getOperation() {
        return operation;
    }

    public void setOperation(Operation operation) {
        this.operation = operation;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public BigDecimal getAmountInRUR() {
        return amountInRUR;
    }

    public void setAmountInRUR(BigDecimal amountInRUR) {
        this.amountInRUR = amountInRUR;
    }
}