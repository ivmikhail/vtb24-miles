package com.github.ivmikhail.vtb24.miles.fx.vtb.dto;

import java.util.List;

public class RatesWrapper {
    private List<ExchangeRate> getHalfYearCardsRatesJsonResult;
    private String error;

    public void setGetHalfYearCardsRatesJsonResult(List<ExchangeRate> getHalfYearCardsRatesJsonResult) {
        this.getHalfYearCardsRatesJsonResult = getHalfYearCardsRatesJsonResult;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public List<ExchangeRate> getGetHalfYearCardsRatesJsonResult() {
        return getHalfYearCardsRatesJsonResult;
    }

    public boolean isValid() {
        boolean hasError = error != null && !error.isEmpty();
        boolean hasRates = getHalfYearCardsRatesJsonResult != null && !getHalfYearCardsRatesJsonResult.isEmpty();

        return !hasError && hasRates;
    }
}