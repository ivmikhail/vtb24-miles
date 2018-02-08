package com.github.ivmikhail.fx;

import java.math.BigDecimal;
import java.time.LocalDate;

public class FakeFxProvider implements FxProvider {

    @Override
    public BigDecimal getRate(String baseCurrency, String quoteCurrency, LocalDate date) {
        return new BigDecimal("54.9");
    }
}