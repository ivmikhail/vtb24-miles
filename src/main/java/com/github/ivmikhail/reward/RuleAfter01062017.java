package com.github.ivmikhail.reward;

import com.github.ivmikhail.Transaction;

import java.math.BigDecimal;
import java.util.List;
import java.util.Properties;
import java.util.Set;

public class RuleAfter01062017 extends MilesRewardRule {
    private static final BigDecimal FOREIGN_TRANS_CASHBACK_PERCENT = new BigDecimal("0.05");
    private static final BigDecimal CASHBACK_PERCENT = new BigDecimal("0.04");
    private static final BigDecimal ONE_MILE_PRICE = new BigDecimal("1.00");

    private Set<String> foreignTransactionWords;

    public RuleAfter01062017(Properties properties) {
        super(properties);
        foreignTransactionWords = getProperty(getClass().getSimpleName() + ".description.foreignTransaction", properties);
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
            } else if (transactionResult.isForeign()) {
                result.addAsForeign(transactionResult);
            } else {
                result.addAsNormal(transactionResult);
            }
        }
        result.setCashbackPercentInTheory(CASHBACK_PERCENT);
        result.setOneMileInRUR(ONE_MILE_PRICE);
        return result;
    }

    @Override
    public TransactionRewardResult process(final Transaction transaction) {
        Transaction.Type type = determineType(transaction);
        boolean isForeign = isForeign(transaction) || calculateAsForeign(transaction);//покупки за рубежом
        boolean ignore = isIgnore(transaction);

        BigDecimal miles = BigDecimal.ZERO;

        if (type == Transaction.Type.WITHDRAW && !ignore) {
            BigDecimal cashback = isForeign ? FOREIGN_TRANS_CASHBACK_PERCENT : CASHBACK_PERCENT;

            BigDecimal amount = transaction
                    .getAmountInAccountCurrency()
                    .negate()
                    .setScale(0, BigDecimal.ROUND_DOWN);//убираем копейки

            amount = amount.setScale(-2, BigDecimal.ROUND_DOWN); //4% каждые 100 руб, округление: 199 -> 100, 201 -> 200  и т.д
            miles = amount.multiply(cashback);
        }

        TransactionRewardResult result = new TransactionRewardResult();
        result.setMiles(miles);
        result.setTransaction(transaction);
        result.setIgnore(ignore);
        result.setType(type);
        result.setForeign(isForeign);

        return result;
    }

    private boolean calculateAsForeign(Transaction transaction) {
        for (String word : foreignTransactionWords) {//Для убера
            if (transaction.getDescription().contains(word)) {
                return true;
            }
        }
        return false;
    }
}