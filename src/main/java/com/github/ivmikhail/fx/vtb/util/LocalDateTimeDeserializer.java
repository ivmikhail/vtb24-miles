package com.github.ivmikhail.fx.vtb.util;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class LocalDateTimeDeserializer implements JsonDeserializer<LocalDateTime> {
    private static final String PREFIX = "/Date(";
    private static final String POSTFIX = ")/";

    @Override
    public LocalDateTime deserialize(JsonElement element,
                                     Type type,
                                     JsonDeserializationContext context) throws JsonParseException {
        String stringDate = element.getAsString(); // /Date(1518108300000)/
        String stringTimestamp = stringDate.substring(PREFIX.length(), stringDate.length() - POSTFIX.length());
        long timestamp = Long.parseLong(stringTimestamp);

        return LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.systemDefault());
    }
}