package com.github.ivmikhail.util;

import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

public class IOUtilsTest {

    @Test(expected = IllegalStateException.class)
    public void testCloseFail() {
        InputStream is = new InputStream() {
            @Override
            public int read() {
                return 0;
            }

            @Override
            public void close() throws IOException {
                throw new IOException();
            }
        };

        IOUtils.close(is);
    }
}