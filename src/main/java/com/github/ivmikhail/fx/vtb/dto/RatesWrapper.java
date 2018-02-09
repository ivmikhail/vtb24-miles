package com.github.ivmikhail.fx.vtb.dto;

import java.util.List;

public class RatesWrapper {
    private String items;//always null
    private String settings;//always null
    private List<ExchangeRate> getHalfYearCardsRatesJsonResult;
    private String error;

    public List<ExchangeRate> getGetHalfYearCardsRatesJsonResult() {
        return getHalfYearCardsRatesJsonResult;
    }

    public boolean isValid() {
        return getHalfYearCardsRatesJsonResult != null && !getHalfYearCardsRatesJsonResult.isEmpty();
    }
}