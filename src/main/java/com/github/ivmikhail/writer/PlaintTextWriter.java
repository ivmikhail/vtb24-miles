package com.github.ivmikhail.writer;

import com.github.ivmikhail.Settings;
import com.github.ivmikhail.Transaction;
import com.github.ivmikhail.reward.RewardResult;
import com.github.ivmikhail.reward.TransactionRewardResult;

import java.io.OutputStream;
import java.util.List;
import java.util.Map;

/**
 * Created by ivmikhail on 02/07/2017.
 */
public class PlaintTextWriter extends AbstractWriter {
    private static final String LINE_SEPARATOR = System.lineSeparator();
    private static final String SPACE4 = "    ";
    private static final int PAD_ACC = 10;
    private static final int PAD_DATE = 10;
    private static final int PAD_DESCRIPTION = 30;
    private static final int PAD_AMOUNT = 10;
    private static final int PAD_CCY = 6;
    private static final int PAD_MILES = 4;

    public PlaintTextWriter(OutputStream out) {
        super(out);
    }

    @Override
    protected String format(Settings settings, RewardResult rewardResult) {
        StringBuilder sb = new StringBuilder();
        sb.append("Операции с кэшбеком 4%");
        sb.append(LINE_SEPARATOR);
        sb.append(LINE_SEPARATOR);
        sb.append(toString(rewardResult.getTransactions(Transaction.Type.WITHDRAW_NORMAL)));

        sb.append(LINE_SEPARATOR);
        sb.append(LINE_SEPARATOR);
        sb.append("Операции с кешбэком 5%");
        sb.append(LINE_SEPARATOR);
        sb.append(LINE_SEPARATOR);
        sb.append(toString(rewardResult.getTransactions(Transaction.Type.WITHDRAW_FOREIGN)));

        sb.append(LINE_SEPARATOR);
        sb.append(LINE_SEPARATOR);
        sb.append("Пополнения");
        sb.append(LINE_SEPARATOR);
        sb.append(LINE_SEPARATOR);
        sb.append(toString(rewardResult.getTransactions(Transaction.Type.REFILL)));

        sb.append(LINE_SEPARATOR);
        sb.append(LINE_SEPARATOR);
        sb.append("Операции, кэшбек за которые не положен");
        sb.append(LINE_SEPARATOR);
        sb.append(LINE_SEPARATOR);
        sb.append(toString(rewardResult.getTransactions(Transaction.Type.IGNORE)));

        sb.append(LINE_SEPARATOR);
        sb.append(LINE_SEPARATOR);

        sb.append("Период c ")
                .append(settings.getMinDate())
                .append(" по ")
                .append(settings.getMaxDate());
        sb.append(LINE_SEPARATOR);
        sb.append(LINE_SEPARATOR);
        sb.append("Всего миль получено     ").append(rewardResult.getTotalRewardMiles()).append(LINE_SEPARATOR);
        sb.append("Всего пополнений, в руб ").append(rewardResult.getTotalRefillRUR()).append(LINE_SEPARATOR);
        sb.append("Всего списаний  , в руб ").append(rewardResult.getTotalWithdrawRUR()).append(LINE_SEPARATOR);
        sb.append("Эффективный кэшбек %    ").append(rewardResult.getEffectiveCashback()).append(LINE_SEPARATOR);

        return sb.toString();
    }

    private String toString(List<TransactionRewardResult> transactionsResults) {
        return toString(transactionsResults, true);
    }

    private String toString(List<TransactionRewardResult> transactionsResults, boolean withColumnHeader) {
        StringBuilder sb = new StringBuilder();
        boolean noTransaction = transactionsResults.isEmpty();

        if (noTransaction) sb.append("<нет операций>");

        if (!noTransaction && withColumnHeader) {
            sb.append(rightPad("СЧЕТ", PAD_ACC)).append(SPACE4);
            sb.append(rightPad("ДАТА ОБР", PAD_DATE)).append(SPACE4);
            sb.append(rightPad("ОПИСАНИЕ", PAD_DESCRIPTION)).append(SPACE4);
            sb.append(rightPad("СУММА", PAD_AMOUNT)).append(SPACE4);
            sb.append(rightPad("ВАЛЮТА", PAD_CCY)).append(SPACE4);
            sb.append(rightPad("МИЛИ", PAD_MILES)).append(SPACE4);
            sb.append(LINE_SEPARATOR);
            sb.append("--------------------------------------------------------------------------------------------");
            sb.append(LINE_SEPARATOR);
        }

        for (TransactionRewardResult trr : transactionsResults) {
            String processedDate = trr.getTransaction().getProcessedDate().toString();
            String description = trr.getTransaction().getDescription();
            String amount = trr.getTransaction().getAmountInAccountCurrency().toString();
            String ccy = trr.getTransaction().getAccountCurrencyCode();
            String miles = trr.getMiles().toString();

            sb.append(rightPad(trr.getTransaction().getCardNumberMasked(), PAD_ACC)).append(SPACE4);
            sb.append(rightPad(processedDate, PAD_DATE)).append(SPACE4);
            sb.append(rightPad(description, PAD_DESCRIPTION)).append(SPACE4);
            sb.append(rightPad(amount, PAD_AMOUNT)).append(SPACE4);
            sb.append(rightPad(ccy, PAD_CCY)).append(SPACE4);
            sb.append(rightPad(miles, PAD_MILES)).append(SPACE4);

            sb.append(LINE_SEPARATOR);
        }
        return sb.toString();
    }

    private String rightPad(String s, int length) {
        String pad = s.trim() + String.format("%" + length + "s", "");
        return pad.substring(0, length);
    }
}
