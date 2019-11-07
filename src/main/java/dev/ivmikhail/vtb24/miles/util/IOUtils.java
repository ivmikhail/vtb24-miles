package dev.ivmikhail.vtb24.miles.util;

import java.io.Closeable;
import java.io.IOException;

public final class IOUtils {
    private IOUtils() { /* util class */}

    public static void close(Closeable closeable) {
        if (closeable == null) return;

        try {
            closeable.close();
        } catch (IOException e) {
            throw new IllegalStateException("Failed to close Closeable object", e);
        }
    }
}
