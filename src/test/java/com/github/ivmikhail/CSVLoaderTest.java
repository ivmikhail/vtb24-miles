package com.github.ivmikhail;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * Created by ivmikhail on 01/07/2017.
 */
public class CSVLoaderTest {
    private static final Logger LOG =  Logger.getLogger(CSVLoaderTest.class.getName());
    private static final String PATH_TO_CSV = "/statement-example.csv";

    private Settings settings;

    @Before
    public void setUp(){
        settings = new Settings();
        settings.setStatementFile(getFileFromClasspath(PATH_TO_CSV));
        settings.setProperties(new Properties());
        settings.setMinDate(LocalDate.MIN);
        settings.setMaxDate(LocalDate.MAX);
    }

    @Test
    public void test() throws IOException, ParseException {
        List<Transaction> transactionList = CSVLoader.load(settings);

        assertEquals(3, transactionList.size());

//      Номер карты/счета/договора;Дата операции;Дата обработки;Сумма операции;Валюта операции;Сумма пересчитанная в валюту счета;Валюта счета;Основание;Статус
//      '123456XXXXXX7890;2017-06-29 21:53:09;;-177,00;RUR;-177,00;RUR;MINI-MARKET;В обработке
//      '123456XXXXXX7890;2017-06-29 12:43:22;;-267,00;RUR;-267,00;RUR;UBER RU JUN29 EUIEH HELP;В обработке
//      '123456XXXXXX7890;2017-06-28 11:21:06;2017-06-29;-41,00;RUR;-41,00;RUR;kassa 6;Исполнено

        Transaction t = transactionList.get(2);
        LOG.info(t.toString());

        assertEquals("'123456XXXXXX7890", t.getCardNumberMasked());
        assertEquals(LocalDateTime.of(2017,6,28,11,21,6), t.getDateTime());
        assertEquals(LocalDate.of(2017,6, 29), t.getProcessedDate());
        assertEquals(0, t.getAmount().compareTo(new BigDecimal("-41.00")));
        assertEquals("RUR", t.getCurrencyCode());
        assertEquals(0, t.getAmountInAccountCurrency().compareTo(new BigDecimal("-41.00")));
        assertEquals("RUR", t.getAccountCurrencyCode());
        assertEquals("kassa 6", t.getDescription());
        assertEquals("Исполнено", t.getStatus());

    }

    private File getFileFromClasspath(String path) {
        URL url = this.getClass().getResource(path);
        return new File(url.getFile());
    }
}
