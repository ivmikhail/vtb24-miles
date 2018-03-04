package com.github.ivmikhail.util;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

public final class LoadProperties {

    private LoadProperties() { /* helper static class */}

    /**
     * / means root of classpath
     */
    public static Properties fromClasspath(String resourceName) {
        InputStream is = LoadProperties.class.getResourceAsStream(resourceName);
        return loadAndClose(is);
    }

    public static Properties fromFile(String path) {
        InputStream is = getFileAsStream(path);
        return loadAndClose(is);
    }

    private static InputStream getFileAsStream(String path) {
        try {
            return Files.newInputStream(Paths.get(path));
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private static Properties loadAndClose(InputStream is) {
        Properties properties = new Properties();

        if (is == null) return properties;

        try (InputStreamReader isr = new InputStreamReader(is, StandardCharsets.UTF_8)) {
            properties.load(isr);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        } finally {
            close(is);
        }
        return properties;
    }

    private static void close(InputStream is) {
        if (is == null) return;

        try {
            is.close();
        } catch (IOException e) {
            throw new IllegalStateException("Failed to close input stream", e);
        }
    }
}