package com.github.ivmikhail.util;

import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

public final class PropsUtil {
    private PropsUtil() {/* helper static class */}

    public static Set<String> getAsSet(Properties properties, String key, String delimiter) {
        String[] arr = properties.getProperty(key, "").split(delimiter);
        Set<String> set = new HashSet<>();
        for (String s : arr) {
            if (!s.trim().isEmpty()) {
                set.add(s.trim());
            }
        }
        return set;
    }
}
