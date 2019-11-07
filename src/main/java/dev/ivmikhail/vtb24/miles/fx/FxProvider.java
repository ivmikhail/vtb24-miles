package dev.ivmikhail.vtb24.miles.fx;

import java.time.LocalDate;

public interface FxProvider {
    FxRate getRate(String baseCurrency, String quoteCurrency, LocalDate date);

    void clear();
}