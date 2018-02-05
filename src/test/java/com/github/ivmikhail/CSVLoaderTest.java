package com.github.ivmikhail;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * Created by ivmikhail on 01/07/2017.
 */
public class CSVLoaderTest {
    private static final Logger LOG =  Logger.getLogger(CSVLoaderTest.class.getName());

    private Settings settings;

    @Before
    public void setUp(){
        String pathToCsv = getClass().getClassLoader().getResource("statement-example.csv").getPath();

        settings = new Settings();
        settings.setPathsToStatement(new String[] { pathToCsv });
        settings.setProperties(new Properties());
        settings.setMinDate(LocalDate.MIN);
        settings.setMaxDate(LocalDate.MAX);
    }

    @Test
    public void test() throws IOException, ParseException {
        List<Transaction> transactionList = CSVLoader.load(settings);

        assertEquals(4, transactionList.size());
/*
        Номер карты/счета/договора;Дата операции;Дата обработки;Сумма операции;Валюта операции;Сумма пересчитанная в валюту счета;Валюта счета;Основание;Статус
        '123456XXXXXX7890;2017-06-01 21:53:09;;-177,00;RUR;-177,00;RUR;MINI?-MARKET;В обработке
        '123456XXXXXX7890;2017-06-02 21:53:09;2017-06-03;-177,00;RUR;-177,00;RUR;MINI-MARKET;Исполнено
        '123456XXXXXX7890;2017-06-03 12:43:22;2017-06-04;-267,00;RUR;-267,00;RUR;UBER RU JUN29 ABCD HELP;Исполнено
        '123456XXXXXX7890;2017-06-04 11:21:06;2017-06-05;-41,00;RUR;-41,00;RUR;kassa 6;Исполнено
        '462235XXXXXX4741;2017-07-15 00:00:00;2017-07-16;-3 200,00;RUR;-3 200,00;RUR;"MUZEY-ZAPOVEDNIK &quot;PETERGO";Исполнено
*/
        Transaction t = transactionList.get(2);
        LOG.info(t.toString());

        assertEquals("'123456XXXXXX7890", t.getCardNumberMasked());
        assertEquals(LocalDateTime.of(2017, Month.JUNE,4,11,21,6), t.getDateTime());
        assertEquals(LocalDate.of(2017,Month.JUNE, 5), t.getProcessedDate());
        assertEquals(0, t.getAmount().compareTo(new BigDecimal("-41.00")));
        assertEquals("RUR", t.getCurrencyCode());
        assertEquals(0, t.getAmountInAccountCurrency().compareTo(new BigDecimal("-41.00")));
        assertEquals("RUR", t.getAccountCurrencyCode());
        assertEquals("kassa 6", t.getDescription());
        assertEquals("Исполнено", t.getStatus());
    }
}
