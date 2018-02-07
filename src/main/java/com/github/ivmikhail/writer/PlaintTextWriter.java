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
        Map<Transaction.Type, List<TransactionRewardResult>> transactionsMap = rewardResult.getTransactionsMap();

        return toString("Операции с кэшбеком 4%", transactionsMap.get(Transaction.Type.WITHDRAW_NORMAL)) +
                toString("Операции с кешбэком 5%", transactionsMap.get(Transaction.Type.WITHDRAW_FOREIGN)) +
                toString("Пополнения", transactionsMap.get(Transaction.Type.REFILL)) +
                toString("Операции, кэшбек за которые не положен", transactionsMap.get(Transaction.Type.IGNORE)) +
                "Период c " + settings.getMinDate() + " по " + settings.getMaxDate() +
                LINE_SEPARATOR +
                LINE_SEPARATOR +
                "Всего миль получено     " + rewardResult.getTotalRewardMiles() + LINE_SEPARATOR +
                "Всего пополнений, в руб " + rewardResult.getTotalRefillRUR() + LINE_SEPARATOR +
                "Всего списаний  , в руб " + rewardResult.getTotalWithdrawRUR() + LINE_SEPARATOR +
                "Эффективный кэшбек %    " + rewardResult.getEffectiveCashback();
    }

    private String toString(String title, List<TransactionRewardResult> transactionsResults) {
        StringBuilder sb = new StringBuilder();
        sb.append(title);
        sb.append(LINE_SEPARATOR);
        sb.append(LINE_SEPARATOR);

        boolean noTransaction = transactionsResults.isEmpty();

        if (noTransaction) sb.append("<нет операций>");

        if (!noTransaction) {
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
        sb.append(LINE_SEPARATOR);
        sb.append(LINE_SEPARATOR);

        return sb.toString();
    }

    private String rightPad(String s, int length) {
        String pad = s.trim() + String.format("%" + length + "s", "");
        return pad.substring(0, length);
    }
}
