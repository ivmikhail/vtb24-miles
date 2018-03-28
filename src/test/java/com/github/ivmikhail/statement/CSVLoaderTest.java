package com.github.ivmikhail.statement;

import com.github.ivmikhail.app.Settings;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

import static org.junit.Assert.assertEquals;

/**
 * Created by ivmikhail on 01/07/2017.
 */
public class CSVLoaderTest {
    private static final Logger LOG = Logger.getLogger(CSVLoaderTest.class.getName());

    private Settings settings;

    @Before
    public void setUp() {
        String pathToCsv = getClass().getClassLoader().getResource("statement-example.csv").getPath();

        settings = new Settings();
        settings.setPathsToStatement(new String[]{pathToCsv});
        settings.setProperties(new Properties());
        settings.setMinDate(LocalDate.MIN);
        settings.setMaxDate(LocalDate.MAX);
    }

    @Test
    public void test() {
        List<Operation> ops = CSVLoader.load(settings);

        assertEquals(4, ops.size());
/*
        Номер карты/счета/договора;Дата операции;Дата обработки;Сумма операции;Валюта операции;Сумма пересчитанная в валюту счета;Валюта счета;Основание;Статус
        '123456XXXXXX7890;2017-06-01 21:53:09;;-177,00;RUR;-177,00;RUR;MINI?-MARKET;В обработке
        '123456XXXXXX7890;2017-06-02 21:53:09;2017-06-03;-177,00;RUR;-177,00;RUR;MINI-MARKET;Исполнено
        '123456XXXXXX7890;2017-06-03 12:43:22;2017-06-04;-267,00;RUR;-267,00;RUR;UBER RU JUN29 ABCD HELP;Исполнено
        '123456XXXXXX7890;2017-06-04 11:21:06;2017-06-05;-41,00;RUR;-41,00;RUR;kassa 6;Исполнено
        '462235XXXXXX4741;2017-07-15 00:00:00;2017-07-16;-3 200,00;RUR;-3 200,00;RUR;"MUZEY-ZAPOVEDNIK &quot;PETERGO";Исполнено
*/
        Operation o = ops.get(2);

        LOG.info("Loaded operation: " + o.toString());

        assertEquals("'123456XXXXXX7890", o.getAccountNumberMasked());
        assertEquals(LocalDateTime.of(2017, Month.JUNE, 4, 11, 21, 6), o.getDateTime());
        assertEquals(LocalDate.of(2017, Month.JUNE, 5), o.getProcessedDate());
        assertEquals(0, o.getAmount().compareTo(new BigDecimal("-41.00")));
        assertEquals("RUR", o.getCurrencyCode());
        assertEquals(0, o.getAmountInAccountCurrency().compareTo(new BigDecimal("-41.00")));
        assertEquals("RUR", o.getAccountCurrencyCode());
        assertEquals("kassa 6", o.getDescription());
        assertEquals("Исполнено", o.getStatus());
    }


    @Test(expected = IllegalStateException.class)
    public void testNonExistingPath() {
        settings.setPathsToStatement(new String[]{"statement-not-exist.csv"});
        CSVLoader.load(settings);
    }
}