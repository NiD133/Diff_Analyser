package org.apache.commons.io.input;

import java.io.IOException;
import java.io.Reader;
import java.nio.CharBuffer;
import org.junit.jupiter.api.Test;

public class ProxyReaderTestTest1 {

    /**
     * Custom NullReader implementation.
     */
    private static final class CustomNullReader extends NullReader {

        CustomNullReader(final int len) {
            super(len);
        }

        @Override
        public int read(final char[] chars) throws IOException {
            return chars == null ? 0 : super.read(chars);
        }

        @Override
        public int read(final CharBuffer target) throws IOException {
            return target == null ? 0 : super.read(target);
        }
    }

    /**
     * ProxyReader implementation.
     */
    private static final class ProxyReaderImpl extends ProxyReader {

        ProxyReaderImpl(final Reader proxy) {
            super(proxy);
        }
    }

    @Test
    void testNullCharArray() throws Exception {
        try (ProxyReader proxy = new ProxyReaderImpl(new CustomNullReader(0))) {
            proxy.read((char[]) null);
            proxy.read(null, 0, 0);
        }
    }
}
