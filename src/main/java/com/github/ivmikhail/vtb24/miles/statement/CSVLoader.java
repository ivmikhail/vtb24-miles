package com.github.ivmikhail.vtb24.miles.statement;

import com.github.ivmikhail.vtb24.miles.app.Settings;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static com.github.ivmikhail.vtb24.miles.statement.CSVLoader.Column.*;

/**
 * Created by ivmikhail on 01/07/2017.
 */
public final class CSVLoader {
    private static final Logger LOG = LoggerFactory.getLogger(CSVLoader.class);

    private static final Character DELIMITER = ';';
    private static final String CHARSET_NAME = "windows-1251";
    private static final long SKIP_FIRST_ROWS = 12;
    private static final CSVFormat CSV_FORMAT = CSVFormat.DEFAULT
            .withDelimiter(DELIMITER)
            .withQuote('"');

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

    private CSVLoader() { /* static helper class */}

    public static List<Operation> load(Settings settings) {
        List<Operation> result = new ArrayList<>();

        LocalDate min = settings.getMinDate();
        LocalDate max = settings.getMaxDate();
        for (String path : settings.getPathsToStatement()) {
            try {
                result.addAll(load(path, min, max));
            } catch (IOException | ParseException e) {
                throw new IllegalStateException(e);
            }
        }
        result.sort(
                Comparator
                        .comparing(Operation::getProcessedDate)
                        .thenComparing(Operation::getDateTime)
                        .thenComparing(Operation::getDescription)
        );
        return result;
    }

    private static List<Operation> load(String path, LocalDate min, LocalDate max) throws IOException, ParseException {

        List<Operation> operations = new ArrayList<>();
        try (
                Reader reader = new InputStreamReader(new FileInputStream(path), CHARSET_NAME);
                CSVParser parser = new CSVParser(reader, CSV_FORMAT)
        ) {
            for (CSVRecord r : parser) {
                if (parser.getCurrentLineNumber() <= SKIP_FIRST_ROWS) continue;
                if (r.size() != values().length) {
                    LOG.warn("Skip row, expected size {}, row {}", values().length, r);
                    continue;
                }

                Operation o = mapRecord(r);
                LocalDate date = o.getProcessedDate();
                if (date == null) {
                    LOG.info("Operation not processed by Bank, skip {}", r);
                    continue;
                }
                boolean isInRange = (date.isEqual(min) || date.isAfter(min)) && (date.isEqual(max) || date.isBefore(max));

                if (isInRange) operations.add(o);
            }
        }

        return operations;
    }

    private static Operation mapRecord(CSVRecord r) throws ParseException {
        Operation o = new Operation();

        String rawProcDate = r.get(PROCESSED_DATE.ordinal());
        LocalDate processedDate = rawProcDate.isEmpty() ? null : LocalDate.parse(rawProcDate, CSV_DATE_FORMAT);
        LocalDateTime dateTime = LocalDateTime.parse(r.get(DATETIME.ordinal()), CSV_DATETIME_FORMAT);
        BigDecimal amount = (BigDecimal) DF.parseObject(r.get(AMOUNT.ordinal()));
        BigDecimal amountInAccountCurrency = (BigDecimal) DF.parseObject(r.get(AMOUNT_IN_ACC_CCY.ordinal()));

        o.setAccountNumberMasked(r.get(CARD_NUM_MASKED.ordinal()));
        o.setDateTime(dateTime);
        o.setProcessedDate(processedDate);
        o.setAmount(amount);
        o.setCurrencyCode(r.get(CCY.ordinal()));
        o.setAmountInAccountCurrency(amountInAccountCurrency);
        o.setAccountCurrencyCode(r.get(CCY_ACC.ordinal()));
        o.setDescription(r.get(DESCRIPTION.ordinal()));
        o.setStatus(r.get(STATUS.ordinal()));

        return o;
    }
}