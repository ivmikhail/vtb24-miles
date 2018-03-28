package com.github.ivmikhail.fx;

import com.github.ivmikhail.fx.vtb.VTBFxProvider;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.Properties;
import java.util.Scanner;

import static org.junit.Assert.assertEquals;

public class FxProviderTest {
    private static final String RATES_JSON_PATH = "/fx/vtb/rates.json";

    private MockWebServer webServer;
    private FxProvider fxProvider;

    @Before
    public void setUp() throws IOException {
        String ratesJson = loadResourceAsString(RATES_JSON_PATH);
        webServer = new MockWebServer();
        webServer.start();

        webServer.enqueue(new MockResponse().setBody(ratesJson));

        Properties properties = new Properties();
        properties.setProperty("fx.provider.vtb.url", webServer.url("/").toString());

        fxProvider = new VTBFxProvider(properties);
    }

    @Test
    public void testRateOnExactDateUSDRUB() {
        FxRate rate = fxProvider.getRate("USD", "RUR", LocalDate.of(2018, Month.FEBRUARY, 8));
        assertEquals(0, rate.getValue().compareTo(new BigDecimal("56.6000")));
        assertEquals("USD", rate.getBaseCurrency());
        assertEquals("RUR", rate.getQuoteCurrency());
        assertEquals(LocalDate.of(2018, Month.FEBRUARY, 8), rate.getDate());

    }

    @Test
    public void testRateOnNearestDateUSDRUB() {
        FxRate rate = fxProvider.getRate("USD", "RUR", LocalDate.of(2017, Month.DECEMBER, 2));
        assertEquals(0, rate.getValue().compareTo(new BigDecimal("57.7100")));
        assertEquals("USD", rate.getBaseCurrency());
        assertEquals("RUR", rate.getQuoteCurrency());
        assertEquals(LocalDate.of(2017, Month.DECEMBER, 4), rate.getDate());
    }

    @Test
    public void testSameCurrency() {
        FxRate rate = fxProvider.getRate("RUR", "RUR", LocalDate.of(2017, Month.DECEMBER, 2));
        assertEquals(0, rate.getValue().compareTo(BigDecimal.ONE));
        assertEquals("RUR", rate.getBaseCurrency());
        assertEquals("RUR", rate.getQuoteCurrency());
        assertEquals(LocalDate.of(2017, Month.DECEMBER, 2), rate.getDate());
    }

    @After
    public void after() throws IOException {
        fxProvider.clear();
        webServer.shutdown();
    }

    private String loadResourceAsString(String fileName) {
        Scanner scanner = new Scanner(getClass().getResourceAsStream(fileName));
        String content = scanner.useDelimiter("\\A").next();
        scanner.close();
        return content;
    }
}