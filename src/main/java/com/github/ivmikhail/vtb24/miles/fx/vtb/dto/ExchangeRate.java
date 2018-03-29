package com.github.ivmikhail.vtb24.miles.fx.vtb.dto;

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

    public void setBuy(BigDecimal buy) {
        this.buy = buy;
    }

    public String getBuyArrow() {
        return buyArrow;
    }

    public void setBuyArrow(String buyArrow) {
        this.buyArrow = buyArrow;
    }

    public String getCurrencyAbbr() {
        return currencyAbbr;
    }

    public void setCurrencyAbbr(String currencyAbbr) {
        this.currencyAbbr = currencyAbbr;
    }

    public String getCurrencyGroupAbbr() {
        return currencyGroupAbbr;
    }

    public void setCurrencyGroupAbbr(String currencyGroupAbbr) {
        this.currencyGroupAbbr = currencyGroupAbbr;
    }

    public LocalDateTime getDateActiveFrom() {
        return dateActiveFrom;
    }

    public void setDateActiveFrom(LocalDateTime dateActiveFrom) {
        this.dateActiveFrom = dateActiveFrom;
    }

    public int getGradation() {
        return gradation;
    }

    public void setGradation(int gradation) {
        this.gradation = gradation;
    }

    public boolean isMetal() {
        return metal;
    }

    public void setMetal(boolean metal) {
        this.metal = metal;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getSell() {
        return sell;
    }

    public void setSell(BigDecimal sell) {
        this.sell = sell;
    }

    public String getSellArrow() {
        return sellArrow;
    }

    public void setSellArrow(String sellArrow) {
        this.sellArrow = sellArrow;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getZone() {
        return zone;
    }

    public void setZone(int zone) {
        this.zone = zone;
    }
}