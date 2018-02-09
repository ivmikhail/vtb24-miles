package com.github.ivmikhail.fx.vtb.util;

import com.google.gson.JsonPrimitive;
import org.junit.Test;

import java.time.LocalDateTime;
import java.time.Month;

import static org.junit.Assert.assertEquals;

public class LocalDateTimeDeserializerTest {

    @Test
    public void test() {
        LocalDateTimeDeserializer deserializer = new LocalDateTimeDeserializer();
        JsonPrimitive element = new JsonPrimitive("/Date(1518108300000)/");
        LocalDateTime ldt = deserializer.deserialize(element, LocalDateTime.class, null);

        assertEquals(LocalDateTime.of(2018, Month.FEBRUARY, 8, 19, 45, 0), ldt);
    }
}