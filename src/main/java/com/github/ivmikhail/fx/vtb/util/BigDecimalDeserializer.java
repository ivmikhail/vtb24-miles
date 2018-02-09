package com.github.ivmikhail.fx.vtb.util;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.math.BigDecimal;

public class BigDecimalDeserializer implements JsonDeserializer<BigDecimal> {
    @Override
    public BigDecimal deserialize(JsonElement element,
                                  Type type,
                                  JsonDeserializationContext context) throws JsonParseException {
        return new BigDecimal(element.getAsString().replace(',', '.'));
    }
}