package com.github.ivmikhail.reward;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ivmikhail on 01/07/2017.
 */
public class RewardResult {

    private static final RoundingMode ROUNDING_MODE = RoundingMode.DOWN;
    private static int SCALE = 4;
    private static final BigDecimal HUNDRED = new BigDecimal("100");

    private BigDecimal totalRewardMiles = BigDecimal.ZERO;
    private BigDecimal totalRefillRUR = BigDecimal.ZERO;
    private BigDecimal totalWithdrawRUR = BigDecimal.ZERO;

    private BigDecimal cashbackPercentInTheory;
    private BigDecimal oneMileInRUR;

    private List<TransactionRewardResult> ignored;
    private List<TransactionRewardResult> x2;
    private List<TransactionRewardResult> refill;
    private List<TransactionRewardResult> normal;

    public RewardResult() {
        this.ignored = new ArrayList<>();
        this.x2 = new ArrayList<>();
        this.refill = new ArrayList<>();
        this.normal = new ArrayList<>();
    }

    public List<TransactionRewardResult> getIgnored() {
        return ignored;
    }

    public void setIgnored(List<TransactionRewardResult> ignored) {
        this.ignored = ignored;
    }

    public List<TransactionRewardResult> getX2() {
        return x2;
    }

    public void setX2(List<TransactionRewardResult> x2) {
        this.x2 = x2;
    }

    public List<TransactionRewardResult> getRefill() {
        return refill;
    }

    public void setRefill(List<TransactionRewardResult> refill) {
        this.refill = refill;
    }

    public List<TransactionRewardResult> getNormal() {
        return normal;
    }

    public void setNormal(List<TransactionRewardResult> normal) {
        this.normal = normal;
    }

    public void setOneMileInRUR(BigDecimal oneMileInRUR) {
        this.oneMileInRUR = oneMileInRUR;
    }

    public void setCashbackPercentInTheory(BigDecimal cashbackPercentInTheory) {
        this.cashbackPercentInTheory = cashbackPercentInTheory;
    }

    public void addAsRefill(TransactionRewardResult trr) {
        processRefill(trr);
        refill.add(trr);
    }

    public void addAsIgnored(TransactionRewardResult trr) {
        ignored.add(trr);
    }

    public void addAsX2(TransactionRewardResult trr) {
        processWithdraw(trr);
        x2.add(trr);
    }

    public void addAsNormal(TransactionRewardResult trr) {
        processWithdraw(trr);
        normal.add(trr);
    }

    public BigDecimal getTotalRefillRUR() {
        return totalRefillRUR;
    }

    public BigDecimal getTotalWithdrawRUR() {
        return totalWithdrawRUR;
    }

    public BigDecimal getTotalRewardMiles() {
        return totalRewardMiles;
    }

    public BigDecimal getBalanceRUR() {
        return totalRefillRUR.add(totalWithdrawRUR);
    }

    public BigDecimal getCashbackInRURByTheory() {
        return totalWithdrawRUR.negate().multiply(cashbackPercentInTheory);
    }

    public BigDecimal getCashbackInRURByFact() {
        if (totalRewardMiles.compareTo(BigDecimal.ZERO) == 0) return null;

        return totalRewardMiles
                .multiply(oneMileInRUR)
                .divide(HUNDRED, SCALE, ROUNDING_MODE);
    }

    public BigDecimal getOneMileInRUR() {
        return oneMileInRUR;
    }

    public BigDecimal getCashbackPercentInTheory() {
        return cashbackPercentInTheory;
    }

    public BigDecimal getCashbackPercentInFact() {
        BigDecimal cashbackInRURByFact = getCashbackInRURByFact();
        if (cashbackInRURByFact == null || cashbackInRURByFact.compareTo(BigDecimal.ZERO) == 0) {
            return null;
        } else {
            return cashbackInRURByFact.multiply(HUNDRED).divide(totalWithdrawRUR.negate(), SCALE, ROUNDING_MODE);
        }
    }

    private void processWithdraw(TransactionRewardResult trr) {
        if (!trr.isWithdraw()) new IllegalArgumentException("Transaction cannot be refill");

        totalWithdrawRUR = totalWithdrawRUR.add(trr.getTransaction().getAmountInAccountCurrency());
        totalRewardMiles = totalRewardMiles.add(trr.getMiles());

    }

    private void processRefill(TransactionRewardResult trr) {
        if (trr.isWithdraw()) new IllegalArgumentException("Transaction cannot be withdraw");

        totalRefillRUR = totalRefillRUR.add(trr.getTransaction().getAmountInAccountCurrency());
    }

    @Override
    public String toString() {
        return "RewardResult{" +
                "totalRewardMiles=" + totalRewardMiles +
                ", totalRefillRUR=" + totalRefillRUR +
                ", totalWithdrawRUR=" + totalWithdrawRUR +
                ", cashbackPercentInTheory=" + cashbackPercentInTheory +
                ", oneMileInRUR=" + oneMileInRUR +
                ", ignored=" + ignored +
                ", x2=" + x2 +
                ", refill=" + refill +
                ", normal=" + normal +
                '}';
    }
}
