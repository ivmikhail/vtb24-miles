package com.github.ivmikhail.reward;

import com.github.ivmikhail.Transaction;

import java.math.BigDecimal;

/**
 * Created by ivmikhail on 01/07/2017.
 */
public class TransactionRewardResult {

    private BigDecimal miles;
    private boolean x2;
    private boolean ignore;
    private Transaction transaction;
    private Transaction.Type type;

    public boolean isWithdraw() {
        return Transaction.Type.WITHDRAW == type;
    }

    public Transaction.Type getType() {
        return type;
    }

    public void setType(Transaction.Type type) {
        this.type = type;
    }

    public boolean isIgnore() {
        return ignore;
    }

    public void setIgnore(boolean ignore) {
        this.ignore = ignore;
    }

    public boolean isX2() {
        return x2;
    }

    public void setX2(boolean x2) {
        this.x2 = x2;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

    public BigDecimal getMiles() {
        return miles;
    }

    public void setMiles(BigDecimal miles) {
        this.miles = miles;
    }

    @Override
    public String toString() {
        return "TransactionRewardResult{" +
                "miles=" + miles +
                ", type=" + type +
                ", x2=" + x2 +
                ", ignore=" + ignore +
                ", transaction=" + transaction +
                '}';
    }
}