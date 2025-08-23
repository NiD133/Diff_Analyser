package org.apache.commons.io.input;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.io.IOException;
import java.io.Reader;
import java.nio.CharBuffer;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link ProxyReader}.
 */
// Renamed from ProxyReaderTestTest1 to the conventional ProxyReaderTest
public class ProxyReaderTest {

    /**
     * A minimal concrete implementation of the abstract ProxyReader for testing.
     */
    private static class TestProxyReader extends ProxyReader {
        TestProxyReader(final Reader proxy) {
            super(proxy);
        }
    }

    /**
     * A specialized NullReader that gracefully handles null arguments in its read
     * methods, which would typically cause a NullPointerException in a standard Reader.
     * <p>
     * This allows us to test that ProxyReader correctly delegates calls without
     * performing intermediate operations that could trigger an NPE.
     * </p>
     */
    private static final class NullArgumentHandlingReader extends NullReader {

        NullArgumentHandlingReader() {
            // The size argument to NullReader is not relevant for these tests.
            super(0);
        }

        /**
         * Returns 0 if the char array is null, otherwise delegates to the superclass.
         * This behavior is key to testing that ProxyReader directly calls this method,
         * unlike its superclass FilterReader, which would cause an NPE.
         */
        @Override
        public int read(final char[] chars) throws IOException {
            return chars == null ? 0 : super.read(chars);
        }

        /**
         * Returns 0 if the CharBuffer is null, otherwise delegates to the superclass.
         * Note: This method was present in the original test's helper class but
         * was not explicitly tested. It is kept for completeness.
         */
        @Override
        public int read(final CharBuffer target) throws IOException {
            return target == null ? 0 : super.read(target);
        }

        // Note: We don't need to override read(char[], int, int) because the
        // base NullReader implementation already handles a null array gracefully
        // when the requested read length is 0.
    }

    @Test
    void readWithNullCharArrayShouldDelegateAndReturnZero() throws IOException {
        // This test verifies an important distinction between ProxyReader and its
        // superclass, FilterReader. FilterReader's read(char[]) implementation
        // calls read(cbuf, 0, cbuf.length), which would throw a NullPointerException
        // if the buffer `cbuf` is null.
        //
        // ProxyReader, however, is designed to delegate the call directly. We test
        // this by using a wrapped reader that is programmed to return 0 for a null
        // buffer. The test passes if no exception is thrown and the return value is 0.
        try (final ProxyReader proxy = new TestProxyReader(new NullArgumentHandlingReader())) {
            final int result = assertDoesNotThrow(() -> proxy.read((char[]) null));
            assertEquals(0, result, "read(null) should return 0 as dictated by the wrapped reader.");
        }
    }

    @Test
    void readWithNullCharArrayAndZeroLengthShouldReturnZero() throws IOException {
        // This test ensures that ProxyReader correctly delegates calls to
        // read(char[], int, int). The underlying Apache Commons IO NullReader
        // is implemented to not access the buffer if the requested length is 0,
        // thus avoiding a NullPointerException even if the buffer is null.
        //
        // We expect the proxy to simply delegate the call and return the underlying
        // reader's result, which in this case is 0.
        try (final ProxyReader proxy = new TestProxyReader(new NullArgumentHandlingReader())) {
            final int result = assertDoesNotThrow(() -> proxy.read(null, 0, 0));
            assertEquals(0, result, "read(null, 0, 0) should return 0.");
        }
    }
}