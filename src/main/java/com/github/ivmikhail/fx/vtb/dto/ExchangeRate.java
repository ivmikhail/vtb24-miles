package com.github.ivmikhail.fx.vtb.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ExchangeRate {
    private BigDecimal buy;
    private String buyArrow;
    private String currencyAbbr;
    private String currencyGroupAbbr;
    private LocalDateTime dateActiveFrom;
    private int gradation;
    private boolean metal;
    private int quantity;
    private BigDecimal sell;
    private String sellArrow;
    private String title;
    private int zone;

    public BigDecimal getBuy() {
        return buy;
    }

    public String getBuyArrow() {
        return buyArrow;
    }

    public String getCurrencyAbbr() {
        return currencyAbbr;
    }

    public String getCurrencyGroupAbbr() {
        return currencyGroupAbbr;
    }

    public LocalDateTime getDateActiveFrom() {
        return dateActiveFrom;
    }

    public int getGradation() {
        return gradation;
    }

    public boolean isMetal() {
        return metal;
    }

    public int getQuantity() {
        return quantity;
    }

    public BigDecimal getSell() {
        return sell;
    }

    public String getSellArrow() {
        return sellArrow;
    }

    public String getTitle() {
        return title;
    }

    public int getZone() {
        return zone;
    }

    @Override
    public String toString() {
        return "ExchangeRate{" +
                "buy=" + buy +
                ", buyArrow='" + buyArrow + '\'' +
                ", currencyAbbr='" + currencyAbbr + '\'' +
                ", currencyGroupAbbr='" + currencyGroupAbbr + '\'' +
                ", dateActiveFrom=" + dateActiveFrom +
                ", gradation=" + gradation +
                ", metal=" + metal +
                ", quantity=" + quantity +
                ", sell=" + sell +
                ", sellArrow='" + sellArrow + '\'' +
                ", title='" + title + '\'' +
                ", zone=" + zone +
                '}';
    }
}