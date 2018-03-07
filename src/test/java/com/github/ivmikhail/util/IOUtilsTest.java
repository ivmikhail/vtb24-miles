package com.github.ivmikhail.util;

import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class IOUtilsTest {

    @Test
    public void test() {
        FakeInputStream is = new FakeInputStream(true);
        IOUtils.close(is);

        assertEquals(true, is.isClosed());
    }

    @Test
    public void testNull() {
        InputStream is = null;
        IOUtils.close(is);//do nothing

        assertNull(is);
    }

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

    private static class FakeInputStream extends InputStream {
        private boolean closed;

        FakeInputStream(boolean open) {
            super();
            this.closed = !open;
        }

        public boolean isClosed() {
            return closed;
        }

        @Override
        public int read() {
            return 0;
        }

        @Override
        public void close() {
            closed = true;
        }
    }
}