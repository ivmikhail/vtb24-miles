package com.github.ivmikhail;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Created by ivmikhail on 01/07/2017.
 */
public class Transaction {

    private String cardNumberMasked;
    private LocalDateTime dateTime;
    private LocalDate processedDate;
    private BigDecimal amount;
    private String currencyCode;
    private BigDecimal amountInAccountCurrency;
    private String accountCurrencyCode;
    private String description;
    private String status;

    public String getCardNumberMasked() {
        return cardNumberMasked;
    }

    public void setCardNumberMasked(String cardNumberMasked) {
        this.cardNumberMasked = cardNumberMasked;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public LocalDate getProcessedDate() {
        return processedDate;
    }

    public void setProcessedDate(LocalDate processedDate) {
        this.processedDate = processedDate;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public BigDecimal getAmountInAccountCurrency() {
        return amountInAccountCurrency;
    }

    public void setAmountInAccountCurrency(BigDecimal amountInAccountCurrency) {
        this.amountInAccountCurrency = amountInAccountCurrency;
    }

    public String getAccountCurrencyCode() {
        return accountCurrencyCode;
    }

    public void setAccountCurrencyCode(String accountCurrencyCode) {
        this.accountCurrencyCode = accountCurrencyCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "cardNumberMasked='" + cardNumberMasked + '\'' +
                ", dateTime=" + dateTime +
                ", processedDate=" + processedDate +
                ", amount=" + amount +
                ", currencyCode='" + currencyCode + '\'' +
                ", amountInAccountCurrency=" + amountInAccountCurrency +
                ", accountCurrencyCode='" + accountCurrencyCode + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
