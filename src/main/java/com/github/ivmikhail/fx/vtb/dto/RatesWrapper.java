package com.github.ivmikhail.fx.vtb.dto;

import java.util.List;

public class RatesWrapper {
    private String items;//always null
    private String settings;//always null
    private List<ExchangeRate> getHalfYearCardsRatesJsonResult;
    private String error;

    public String getItems() {
        return items;
    }

    public void setItems(String items) {
        this.items = items;
    }

    public String getSettings() {
        return settings;
    }

    public void setSettings(String settings) {
        this.settings = settings;
    }

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
        return getHalfYearCardsRatesJsonResult != null && !getHalfYearCardsRatesJsonResult.isEmpty();
    }
}