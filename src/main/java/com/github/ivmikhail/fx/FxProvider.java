package com.github.ivmikhail.fx;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface FxProvider {
    BigDecimal getRate(String baseCurrency, String quoteCurrency, LocalDate date);

    FxProvider load();

    void clear();
}
