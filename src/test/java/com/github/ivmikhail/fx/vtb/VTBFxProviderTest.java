package com.github.ivmikhail.fx.vtb;

import com.github.ivmikhail.fx.FxProvider;
import okhttp3.HttpUrl;
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

public class VTBFxProviderTest {
    private static final String VTB_API_URL = "https://www.vtb24.ru/services/ExecuteAction";
    private static final String RATES_JSON_PATH = "/fx/vtb/rates.json";

    private MockWebServer webServer;
    private FxProvider fxProvider;

    @Before
    public void setUp() throws IOException {
        String ratesJson = loadResourceAsString(RATES_JSON_PATH);
        webServer = new MockWebServer();
        webServer.enqueue(new MockResponse().setBody(ratesJson));

        webServer.start();
        HttpUrl baseUrl = webServer.url(VTB_API_URL);

        Properties properties = new Properties();
        properties.setProperty("fx.provider.vtb.url", baseUrl.toString());

        fxProvider = new VTBFxProvider(properties);
        fxProvider.load();
    }

    @Test
    public void testRateOnExactDate_USDRUB_on_20180208() {
        BigDecimal rate = fxProvider.getRate("USD", "RUR", LocalDate.of(2018, Month.FEBRUARY, 8));
        assertEquals(0, rate.compareTo(new BigDecimal("56.6000")));
    }

    @Test
    public void testRateOnNearestDate_USDRUB_on_20171202() {
        BigDecimal rate = fxProvider.getRate("USD", "RUR", LocalDate.of(2017, Month.DECEMBER, 2));
        assertEquals(0, rate.compareTo(new BigDecimal("57.7100")));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUSDJPY() {
        fxProvider.getRate("USD", "JPY", LocalDate.MIN);
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