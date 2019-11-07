package dev.ivmikhail.vtb24.miles.fx.vtb.util;

import com.google.gson.JsonPrimitive;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;

public class BigDecimalDeserializerTest {

    @Test
    public void test() {
        BigDecimalDeserializer deserializer = new BigDecimalDeserializer();
        JsonPrimitive element = new JsonPrimitive("73,8200");
        BigDecimal value = deserializer.deserialize(element, BigDecimal.class, null);

        assertEquals(0, new BigDecimal("73.8200").compareTo(value));
    }
}