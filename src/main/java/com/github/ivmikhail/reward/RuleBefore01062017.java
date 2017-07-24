package com.github.ivmikhail.reward;

import com.github.ivmikhail.Transaction;

import java.math.BigDecimal;
import java.util.*;

/**
 * Тариф действоваший до 01 июня 2017:
 * <p>
 * - двойные мили за покупки за рубежом
 * - двойные мили в день рождения и за 2 дня до него
 * <p>
 * Created by ivmikhail on 01/07/2017.
 */
@Deprecated
public class RuleBefore01062017 extends MilesRewardRule {

    private static final BigDecimal FOUR_MILE_PRICE_RUR = new BigDecimal("35");
    private static final BigDecimal FOUR = new BigDecimal("4");
    private static final BigDecimal TWO = new BigDecimal("2");
    private static final BigDecimal CASHBACK_PERCENT_BY_THEORY = new BigDecimal("3.8");
    private static final BigDecimal ONE_MILE_IN_RUR = new BigDecimal("0.33");

    private Set<String> x2MilesWords;

    public RuleBefore01062017(Properties properties) {
        super(properties);
        x2MilesWords = getProperty(getClass().getSimpleName() + ".description.x2Miles", properties);
    }

    @Override
    public RewardResult process(List<Transaction> transactionList) {
        RewardResult result = new RewardResult();
        for (Transaction t : transactionList) {
            TransactionRewardResult transactionResult = process(t);

            if (!transactionResult.isWithdraw()) {
                result.addAsRefill(transactionResult);
            } else if (transactionResult.isIgnore()) {
                result.addAsIgnored(transactionResult);
            } else if (transactionResult.isX2()) {
                result.addAsX2(transactionResult);
            } else {
                result.addAsNormal(transactionResult);
            }
        }
        result.setCashbackPercentInTheory(CASHBACK_PERCENT_BY_THEORY);
        result.setOneMileInRUR(ONE_MILE_IN_RUR);
        return result;
    }

    @Override
    public TransactionRewardResult process(final Transaction transaction) {
        Transaction.Type type = determineType(transaction);
        boolean x2 = isForeign(transaction);//покупки за рубежом
        boolean ignore = isIgnore(transaction);

        BigDecimal miles = BigDecimal.ZERO;

        if (type == Transaction.Type.WITHDRAW && !ignore) {
            miles = transaction.getAmountInAccountCurrency()
                    .negate()
                    .divide(FOUR_MILE_PRICE_RUR, 0, BigDecimal.ROUND_DOWN)
                    .multiply(FOUR);

            if (!x2) {
                for (String word : x2MilesWords) {//Для убера
                    x2 = transaction.getDescription().contains(word);
                    if (x2) break;
                }
            }
            if (x2) miles = miles.multiply(TWO);
        }


        TransactionRewardResult result = new TransactionRewardResult();
        result.setMiles(miles);
        result.setX2(x2);
        result.setTransaction(transaction);
        result.setIgnore(ignore);
        result.setType(type);

        return result;
    }
}