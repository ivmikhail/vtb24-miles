package dev.ivmikhail.vtb24.miles.reward;

import dev.ivmikhail.vtb24.miles.statement.Operation;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.QuoteMode;

import java.io.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public final class ExportAs {
    private static final char ZERO_WIDTH_SPACE = '\u200B';
    private static final char SPACE = '\u2000';

    private static final CSVFormat TEXT_FORMAT = CSVFormat.DEFAULT
            .withDelimiter(SPACE)
            .withEscape(ZERO_WIDTH_SPACE)
            .withQuoteMode(QuoteMode.NONE);

    private static final CSVFormat FILE_FORMAT = CSVFormat.EXCEL
            .withDelimiter(';')
            .withQuoteMode(QuoteMode.ALL);
    private static final String HEADER_DELIMITER = String.join("", Collections.nCopies(120, "-"));

    private static final int PAD_ACC = 17;
    private static final int PAD_DATE = 10;
    private static final int PAD_DESCRIPTION = 38;
    private static final int PAD_AMOUNT = 11;
    private static final int PAD_CCY = 6;
    private static final int PAD_RATE = 6;
    private static final int PAD_PERCENT = 5;
    private static final int PAD_MILES = 11;

    private ExportAs() { /* helper class */}

    public static String txt(RewardSummary reward) {
        StringWriter out = new StringWriter();
        write(reward, TEXT_FORMAT, out, true);
        return out.toString();
    }

    /**
     * Export reward to CSV file using default character encoding. Encoding can be specified with -Dfile.encoding=UTF8
     */
    public static File csv(RewardSummary reward, String pathToCsv) {
        File f = new File(pathToCsv);

        try (Writer out = new FileWriter(f)) {
            write(reward, FILE_FORMAT, out, false);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }

        return f;
    }

    public static void write(RewardSummary reward, CSVFormat format, Writer out, boolean withHeaderDelimiter) {
        try (
                CSVPrinter csv = new CSVPrinter(out, format)
        ) {
            Map<Transaction.Type, List<Transaction>> transactionMap = reward.getTransactionsMap();

            printTransactions(csv, "Операции c кэшбеком", transactionMap.get(Transaction.Type.WITHDRAW), true, withHeaderDelimiter);
            csv.println();

            printTransactions(csv, "Операции, кешбэк за которые не положен", transactionMap.get(Transaction.Type.WITHDRAW_IGNORE), withHeaderDelimiter);
            csv.println();

            printTransactions(csv, "Пополнения", transactionMap.get(Transaction.Type.REFILL), withHeaderDelimiter);
            csv.println();

            csv.printRecord("Период, с " + reward.getMinDate() + " по " + reward.getMaxDate());
            csv.println();

            csv.printRecord("Правило расчета - " + reward.getRule().getName());
            csv.printRecord("Всего миль/бонусов/cashback положено: " + reward.getTotalMiles());

            csv.println();
            csv.printRecord("Всего пополнений, в руб", reward.getTotalRefillRUR());
            csv.printRecord("Всего списаний  , в руб", reward.getTotalWithdrawRUR());
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private static void printTransactions(CSVPrinter csv,
                                          String title,
                                          List<Transaction> transactions,
                                          boolean withHeaderDelimiter) throws IOException {
        printTransactions(csv, title, transactions, false, withHeaderDelimiter);
    }

    private static void printTransactions(CSVPrinter csv,
                                          String title,
                                          List<Transaction> transactions,
                                          boolean withReward,
                                          boolean withHeaderDelimiter) throws IOException {
        csv.printRecord(title);
        csv.println();
        csv.printRecord(createHeader(withReward));
        if (withHeaderDelimiter) {
            csv.printRecord(HEADER_DELIMITER);
        }

        if (transactions == null || transactions.isEmpty()) {
            csv.printRecord("<нет операций>");
            return;
        }

        for (Transaction t : transactions) {
            csv.printRecord(createRow(t, withReward));
        }
    }

    private static List<String> createHeader(boolean withReward) {

        List<String> header = new ArrayList<>();
        header.add(pad("СЧЕТ", PAD_ACC));
        header.add(pad("ДАТА ОБР", PAD_DATE));
        header.add(pad("ОПИСАНИЕ", PAD_DESCRIPTION));
        header.add(pad("СУММА", PAD_AMOUNT));
        header.add(pad("ВАЛЮТА", PAD_CCY));
        header.add(pad("КУРС", PAD_RATE));
        header.add(pad("СУММА(РУБ)", PAD_AMOUNT));

        if (withReward) {
            header.add(pad("КБ %", PAD_PERCENT));
            header.add(pad("МИЛИ/БОНУСЫ", PAD_MILES));
        }

        return header;
    }

    private static List<String> createRow(Transaction t, boolean withRewards) {
        List<String> row = new ArrayList<>();

        Operation op = t.getOperation();
        row.add(pad(op.getAccountNumberMasked(), PAD_ACC));
        row.add(pad(op.getProcessedDate(), PAD_DATE));
        row.add(pad(op.getDescription(), PAD_DESCRIPTION));
        row.add(pad(op.getAmountInAccountCurrency(), PAD_AMOUNT));
        row.add(pad(op.getAccountCurrencyCode(), PAD_CCY));
        row.add(pad(t.getAccountCurrencyRate().getValue(), PAD_RATE));
        row.add(pad(t.getAmountInRUR(), PAD_AMOUNT));

        if (withRewards) {
            Transaction.Reward r = t.getReward();
            row.add(pad(r.getPercent(), PAD_PERCENT));
            row.add(pad(r.getMiles(), PAD_MILES));
        }
        return row;
    }

    private static String pad(Object o, int length) {
        if (o == null) return pad("N/A", length);

        if (o instanceof BigDecimal) {
            BigDecimal b = (BigDecimal) o;
            BigDecimal rounded = b.setScale(2, BigDecimal.ROUND_DOWN);

            return pad(rounded.toString(), length);
        }
        return pad(o.toString(), length);
    }

    private static String pad(String s, int length) {
        if (length < 1) return s.trim();

        String pad = s.trim() + String.format("%" + length + "s", "");
        return pad.substring(0, length);
    }
}