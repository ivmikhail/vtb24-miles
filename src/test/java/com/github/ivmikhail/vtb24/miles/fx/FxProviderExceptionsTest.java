package com.github.ivmikhail.vtb24.miles.fx;

import com.github.ivmikhail.vtb24.miles.fx.vtb.VTBFxProvider;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.time.LocalDate;
import java.time.Month;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class FxProviderExceptionsTest {

    private MockWebServer webServer;
    private FxProvider fxProvider;

    @Before
    public void setUp() throws IOException {
        webServer = new MockWebServer();
        webServer.start();

        Properties properties = new Properties();
        properties.setProperty("fx.provider.vtb.url", webServer.url("/").toString());
        properties.setProperty("fx.provider.vtb.readTimeoutMillis", "100");
        fxProvider = new VTBFxProvider(properties);
    }

    @Test
    public void testResponseCode() {
        webServer.enqueue(new MockResponse().setResponseCode(500));

        try {
            fxProvider.getRate("USD", "RUR", LocalDate.of(2018, Month.MARCH, 8));
            fail("Should throw IllegalStateException");
        } catch (IllegalStateException e) {
            assertEquals("Failed to get FX rates, response code is 500", e.getMessage());
        }
    }

    @Test(expected = IllegalStateException.class)
    public void testEmptyData() {
        webServer.enqueue(new MockResponse().setBody(""));
        fxProvider.getRate("USD", "RUR", LocalDate.of(2018, Month.MARCH, 8));
    }

    @Test
    public void testNotFoundRate() {
        webServer.enqueue(new MockResponse().setBody("" +
                "{\n" +
                "\"items\": null,\n" +
                "\"getHalfYearCardsRatesJsonResult\": [\n" +
                "   {\n" +
                "     \"zone\": 0,\n" +
                "     \"currencyGroupAbbr\": \"pp_cards\",\n" +
                "     \"currencyAbbr\": \"EUR\",\n" +
                "     \"title\": \"Евро\",\n" +
                "     \"quantity\": 1.0,\n" +
                "     \"buy\": \"70,1000\",\n" +
                "     \"buyArrow\": \"1\",\n" +
                "     \"sell\": \"74,1600\",\n" +
                "     \"sellArrow\": \"1\",\n" +
                "     \"gradation\": 1,\n" +
                "     \"dateActiveFrom\": \"\\/Date(1518123300000)\\/\",\n" +
                "     \"isMetal\": false\n" +
                "   }],\n" +
                "\"setting\": null\n" +
                "}"));

        try {
            fxProvider.getRate("USD", "RUR", LocalDate.of(2018, Month.MARCH, 8));
            fail("Should throw IllegalStateException");
        } catch (IllegalStateException e) {
            assertEquals("FX rate USDRUR not found on 2018-03-08", e.getMessage());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUnsupportedPair() {
        fxProvider.getRate("USD", "JPY", LocalDate.MIN);
    }

    @Test
    public void testUnreachableServer() {
        webServer.enqueue(new MockResponse()
                .setBody("123")
                .setBodyDelay(101, TimeUnit.MILLISECONDS));

        try {
            fxProvider.getRate("USD", "RUR", LocalDate.of(2018, Month.MARCH, 8));
            fail("Should throw IllegalStateException caused by SocketTimeoutException");
        } catch (IllegalStateException e) {
            assertEquals(SocketTimeoutException.class, e.getCause().getClass());
        }
    }

    @Test(expected = IllegalStateException.class)
    public void testResponseWithError() {
        webServer.enqueue(new MockResponse()
                .setBody("{\"items\": null, \"error\": \"some error\"}"));

        fxProvider.getRate("USD", "RUR", LocalDate.of(2018, Month.MARCH, 8));
    }

    @After
    public void after() throws IOException {
        fxProvider.clear();
        webServer.shutdown();
    }
}