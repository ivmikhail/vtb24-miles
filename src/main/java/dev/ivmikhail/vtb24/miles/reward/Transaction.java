package dev.ivmikhail.vtb24.miles.reward;

import dev.ivmikhail.vtb24.miles.fx.FxRate;
import dev.ivmikhail.vtb24.miles.statement.Operation;

import java.math.BigDecimal;

public class Transaction {
    private Operation operation;
    private Type type;
    private FxRate accountCurrencyRate;
    private BigDecimal amountInRUR;
    private Reward reward;

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

    public Reward getReward() {
        return reward;
    }

    public void setRewards(Reward reward) {
        this.reward = reward;
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