package com.github.ivmikhail.reward;

import com.github.ivmikhail.transactions.Transaction;

import java.math.BigDecimal;

/**
 * Created by ivmikhail on 01/07/2017.
 */
public class TransactionReward {

    private BigDecimal miles;
    private Transaction transaction;
    private Transaction.Type transactionType;

    public Transaction.Type getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(Transaction.Type transactionType) {
        this.transactionType = transactionType;
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
}