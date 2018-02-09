package com.github.ivmikhail.fx.vtb;

import com.github.ivmikhail.fx.FxProvider;
import com.github.ivmikhail.fx.vtb.dto.ExchangeRate;
import com.github.ivmikhail.fx.vtb.dto.RatesWrapper;
import com.github.ivmikhail.fx.vtb.util.BigDecimalDeserializer;
import com.github.ivmikhail.fx.vtb.util.LocalDateTimeDeserializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import okhttp3.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;

/**
 * Retrieves rates from
 * https://www.vtb24.ru/banking/currency/rate-of-conversion/?NoMobileRedirect=true
 */
public class VTBFxProvider implements FxProvider {

    private static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(BigDecimal.class, new BigDecimalDeserializer())
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeDeserializer())
            .create();

    private static final MediaType JSON = MediaType.parse("application/json");
    private static final String USER_AGENT = "vtb24-miles (https://github.com/ivmikhail/vtb24-miles)";

    private String url;
    private OkHttpClient httpClient;
    private String[][] supportedPairs;
    private List<ExchangeRate> rates;

    public VTBFxProvider(Properties properties) {
        url = properties.getProperty("fx.provider.vtb.url");
        supportedPairs = new String[][]{
                {"USD", "RUR"},
                {"EUR", "RUR"},
                {"EUR", "USD"}
        };

        httpClient = new OkHttpClient();
        rates = new ArrayList<>();
    }

    @Override
    public BigDecimal getRate(final String baseCurrency, final String quoteCurrency, final LocalDate date) {
        throwExceptionIfNotSupported(baseCurrency, quoteCurrency);

        //transaction processed date without time, also exchange rate may not exist on given date
        //so let's take nearest rate by date
        Optional<ExchangeRate> closestRateByDate = rates
                .stream()
                .filter(rate -> rate.getDateActiveFrom().toLocalDate().isEqual(date) ||
                        rate.getDateActiveFrom().toLocalDate().isAfter(date))
                .filter(rate -> (baseCurrency.equals(rate.getCurrencyAbbr()) && "RUR".equals(quoteCurrency)) ||
                        (rate.getCurrencyAbbr().startsWith(baseCurrency) && rate.getCurrencyAbbr().endsWith(quoteCurrency))
                )
                .min(Comparator.comparing(ExchangeRate::getDateActiveFrom, (d1, d2) -> {
                    long dateMillis = date.atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli();

                    long diff1 = Math.abs(d1.toInstant(ZoneOffset.UTC).toEpochMilli() - dateMillis);
                    long diff2 = Math.abs(d2.toInstant(ZoneOffset.UTC).toEpochMilli() - dateMillis);
                    return Long.compare(diff1, diff2);
                }));

        ExchangeRate exchangeRate = closestRateByDate.orElseThrow(() ->
                new IllegalStateException("FX rate " + baseCurrency + quoteCurrency + " not found on " + date));

        return exchangeRate.getBuy();
    }

    @Override
    public FxProvider load() {
        try {
            RatesWrapper ratesWrapper = loadFromRemote();
            rates = ratesWrapper.getGetHalfYearCardsRatesJsonResult();
        } catch (IOException e) {
            throw new IllegalStateException("Failed to retrieve FX rates from url " + url, e);
        }
        return this;
    }

    @Override
    public void clear() {
        rates.clear();
    }

    private void throwExceptionIfNotSupported(String baseCurrency, String quoteCurrency) {
        for (String[] pair : supportedPairs) {
            if (pair[0].equals(baseCurrency) && pair[1].equals(quoteCurrency)) return;
            if (pair[1].equals(baseCurrency) && pair[0].equals(quoteCurrency)) return;
        }
        throw new IllegalArgumentException("Currency pair is not supported " + baseCurrency + quoteCurrency);
    }

    private RatesWrapper loadFromRemote() throws IOException {
        RequestBody requestBody = RequestBody.create(JSON, GSON.toJson(PayloadFactory.create()));

        Request request = new Request.Builder()
                .url(url)
                .header("Accept-Language", "en-US;en")
                .header("User-Agent", USER_AGENT)
                .header("Accept-Encoding", "gzip, deflate")
                .post(requestBody)
                .build();
        Response response = httpClient
                .newCall(request)
                .execute();

        String responseBody = response.body().string();

        RatesWrapper result = GSON.fromJson(
                responseBody,
                RatesWrapper.class);

        if (!result.isValid()) {
            throw new IllegalStateException(
                    String.format("Failed to get FX rates from %s, response: %s ", url, responseBody)
            );
        }

        return result;
    }
}