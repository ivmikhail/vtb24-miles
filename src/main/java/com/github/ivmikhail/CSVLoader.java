package com.github.ivmikhail;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.*;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static com.github.ivmikhail.CSVLoader.Column.*;

/**
 * Created by ivmikhail on 01/07/2017.
 */
public class CSVLoader {
    private static final Character COLUMN_SEPARATOR = ';';
    private static final String CHARSET_NAME = "windows-1251";
    private static final long SKIP_FIRST_ROWS = 12;
    private static final CSVFormat CSV_FORMAT = CSVFormat.newFormat(COLUMN_SEPARATOR);

    private static final DateTimeFormatter CSV_DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter CSV_DATETIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private static final DecimalFormat DF = (DecimalFormat) NumberFormat.getInstance();
    private static final DecimalFormatSymbols DF_SYMBOLS = new DecimalFormatSymbols();

    static {
        DF_SYMBOLS.setDecimalSeparator(',');
        DF_SYMBOLS.setGroupingSeparator(' ');

        DF.setDecimalFormatSymbols(DF_SYMBOLS);
        DF.setParseBigDecimal(true);
        DF.setGroupingUsed(true);
    }


    enum Column {
        CARD_NUM_MASKED,
        DATETIME,
        PROCESSED_DATE,
        AMOUNT,
        CCY,
        AMOUNT_IN_ACC_CCY,
        CCY_ACC,
        DESCRIPTION,
        STATUS
    }

    public static List<Transaction> load(Settings settings) throws IOException, ParseException {

        List<Transaction> transactionList = new ArrayList<>();

        try (
                Reader reader = new InputStreamReader(new FileInputStream(settings.getStatementFile()), CHARSET_NAME);
                CSVParser parser = new CSVParser(reader, CSV_FORMAT);
        ) {
            LocalDate min = settings.getMinDate();
            LocalDate max = settings.getMaxDate();

            for (CSVRecord r : parser) {
                if (parser.getCurrentLineNumber() <= SKIP_FIRST_ROWS) continue;
                if (r.size() != Column.values().length) continue;

                Transaction t = mapRecord(r);
                LocalDate date = t.getProcessedDate();
                if (date == null) continue; //transaction not processed by Bank, skip it
                boolean isInRange = (date.isEqual(min) || date.isAfter(min)) && (date.isEqual(max) || date.isBefore(max));

                if (isInRange) transactionList.add(t);
            }
        }

        return transactionList;
    }

    private static Transaction mapRecord(CSVRecord r) throws ParseException {
        Transaction t = new Transaction();

        String rawProcDate = r.get(PROCESSED_DATE.ordinal());
        LocalDate processedDate = rawProcDate.isEmpty() ? null : LocalDate.parse(rawProcDate, CSV_DATE_FORMAT);
        LocalDateTime dateTime = LocalDateTime.parse(r.get(DATETIME.ordinal()), CSV_DATETIME_FORMAT);
        BigDecimal amount = (BigDecimal) DF.parseObject(r.get(AMOUNT.ordinal()));
        BigDecimal amountInAccountCurrency = (BigDecimal) DF.parseObject(r.get(AMOUNT_IN_ACC_CCY.ordinal()));

        t.setCardNumberMasked(r.get(CARD_NUM_MASKED.ordinal()));
        t.setDateTime(dateTime);
        t.setProcessedDate(processedDate);
        t.setAmount(amount);
        t.setCurrencyCode(r.get(CCY.ordinal()));
        t.setAmountInAccountCurrency(amountInAccountCurrency);
        t.setAccountCurrencyCode(r.get(CCY_ACC.ordinal()));
        t.setDescription(r.get(DESCRIPTION.ordinal()));
        t.setStatus(r.get(STATUS.ordinal()));

        return t;
    }
}
