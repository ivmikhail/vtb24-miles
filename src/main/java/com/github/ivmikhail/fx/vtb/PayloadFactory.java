package com.github.ivmikhail.fx.vtb;

import com.github.ivmikhail.fx.vtb.dto.Payload;

public final class PayloadFactory {
    private PayloadFactory() { /* helper class, no need to initialize it */}

    public static Payload create() {
        Payload payload = new Payload();
        payload.setAction("{\"action\": \"currency\"}");
        payload.setScopeData("{\"method\": \"HalfYearCardsRates2\"}");
        return payload;
    }
}