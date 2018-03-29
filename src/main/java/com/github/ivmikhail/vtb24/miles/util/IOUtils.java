package com.github.ivmikhail.vtb24.miles.util;

import java.io.IOException;
import java.io.InputStream;

public final class IOUtils {
    private IOUtils() { /* util class */}

    public static void close(InputStream is) {
        if (is == null) return;

        try {
            is.close();
        } catch (IOException e) {
            throw new IllegalStateException("Failed to close input stream", e);
        }
    }
}
