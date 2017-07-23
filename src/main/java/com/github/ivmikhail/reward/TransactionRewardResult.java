package com.github.ivmikhail.reward;

import com.github.ivmikhail.Transaction;

import java.math.BigDecimal;

/**
 * Created by ivmikhail on 01/07/2017.
 */
public class TransactionRewardResult {

    private BigDecimal miles;
    @Deprecated
    private boolean x2;
    private boolean ignore;
    private boolean foreign;
    private Transaction transaction;
    private Transaction.Type type;

    public boolean isForeign() {
        return foreign;
    }

    public void setForeign(boolean foreign) {
        this.foreign = foreign;
    }

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

    @Deprecated
    public boolean isX2() {
        return x2;
    }

    @Deprecated
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
                ", foreign=" + foreign +
                ", transaction=" + transaction +
                '}';
    }
}