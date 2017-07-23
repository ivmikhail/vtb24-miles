package com.github.ivmikhail.writer;

import com.github.ivmikhail.reward.RewardResult;
import com.github.ivmikhail.reward.TransactionRewardResult;

import java.io.OutputStream;
import java.util.List;

/**
 * Created by ivmikhail on 02/07/2017.
 */
public class PlaintTextWriter extends AbstractWriter {
    private static final String LINE_SEPARATOR = System.lineSeparator();
    private static final String SPACE4 = "    ";
    private static final int PAD_DATE = 10;
    private static final int PAD_DESCRIPTION = 24;
    private static final int PAD_AMOUNT = 12;
    private static final int PAD_MILES = 8;

    public PlaintTextWriter(OutputStream out) {
        super(out);
    }

    @Override
    protected String format(RewardResult rewardResult) {
        StringBuilder sb = new StringBuilder();
        sb.append("Операции с кэшбеком 4%");
        sb.append(LINE_SEPARATOR);
        sb.append(LINE_SEPARATOR);
        sb.append(toString(rewardResult.getNormal(), true));

        sb.append(LINE_SEPARATOR);
        sb.append(LINE_SEPARATOR);
        sb.append("Операции с кешбэком 5%");
        sb.append(LINE_SEPARATOR);
        sb.append(LINE_SEPARATOR);
        sb.append(toString(rewardResult.getForeign()));

//        sb.append(LINE_SEPARATOR);
//        sb.append(LINE_SEPARATOR);
//        sb.append("Операции с двойным кэшбеком");
//        sb.append(LINE_SEPARATOR);
//        sb.append(LINE_SEPARATOR);
//        sb.append(toString(rewardResult.getX2()));

        sb.append(LINE_SEPARATOR);
        sb.append(LINE_SEPARATOR);
        sb.append("Операции пополнения");
        sb.append(LINE_SEPARATOR);
        sb.append(LINE_SEPARATOR);
        sb.append(toString(rewardResult.getRefill()));

        sb.append(LINE_SEPARATOR);
        sb.append(LINE_SEPARATOR);
        sb.append("Операции без кэшбека");
        sb.append(LINE_SEPARATOR);
        sb.append(LINE_SEPARATOR);
        sb.append(toString(rewardResult.getIgnored()));


        sb.append(LINE_SEPARATOR);
        sb.append(LINE_SEPARATOR);
        sb.append("Кэшбек в рублях, в теории    ").append(rewardResult.getCashbackInRURByTheory()).append(LINE_SEPARATOR);
        sb.append("Кэшбек в рублях, фактический ").append(rewardResult.getCashbackInRURByFact()).append(LINE_SEPARATOR);
        sb.append("Кэшбек в %, в теории         ").append(rewardResult.getCashbackPercentInTheory()).append(LINE_SEPARATOR);
        sb.append("Кэшбек в %, фактический      ").append(rewardResult.getCashbackPercentInFact()).append(LINE_SEPARATOR);
        sb.append(LINE_SEPARATOR);
        sb.append("Всего миль получено ").append(rewardResult.getTotalRewardMiles()).append(LINE_SEPARATOR);
        sb.append(LINE_SEPARATOR);
        sb.append("Всего пополнений, в руб ").append(rewardResult.getTotalRefillRUR()).append(LINE_SEPARATOR);
        sb.append("Всего списаний  , в руб ").append(rewardResult.getTotalWithdrawRUR()).append(LINE_SEPARATOR);
        sb.append("Баланс          , в руб ").append(rewardResult.getBalanceRUR()).append(LINE_SEPARATOR);

        return sb.toString();
    }

    private String toString(List<TransactionRewardResult> transactionsResults) {
        return toString(transactionsResults, false);
    }

    private String toString(List<TransactionRewardResult> transactionsResults, boolean withColumnHeader) {
        StringBuilder sb = new StringBuilder();
        boolean notTransaction = transactionsResults.isEmpty();

        if (notTransaction) sb.append("<нет операций>");
        
        if (!notTransaction && withColumnHeader) {
            sb.append(rightPad("date", PAD_DATE)).append(SPACE4);
            sb.append(rightPad("description", PAD_DESCRIPTION)).append(SPACE4);
            sb.append(rightPad("amount", PAD_AMOUNT)).append(SPACE4);
            sb.append(rightPad("miles", PAD_MILES)).append(SPACE4);
            sb.append(LINE_SEPARATOR);
        }

        for (TransactionRewardResult trr : transactionsResults) {
            String processedDate = trr.getTransaction().getProcessedDate().toString();
            String description = trr.getTransaction().getDescription();
            String amount = trr.getTransaction().getAmountInAccountCurrency().toString();
            String miles = trr.getMiles().toString();

            sb.append(rightPad(processedDate, PAD_DATE)).append(SPACE4);
            sb.append(rightPad(description, PAD_DESCRIPTION)).append(SPACE4);
            sb.append(rightPad(amount, PAD_AMOUNT)).append(SPACE4);
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
