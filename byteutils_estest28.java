package org.apache.commons.compress.utils;

import org.junit.Test;

import java.io.InputStream;

/**
 * Unit tests for the {@link ByteUtils} class, focusing on exception handling.
 */
public class ByteUtilsTest {

    /**
     * Tests that the fromLittleEndian method throws a NullPointerException
     * when the provided InputStream is null.
     *
     * The method under test, {@link ByteUtils#fromLittleEndian(InputStream, int)},
     * is deprecated, but its contract regarding null inputs should still be upheld.
     */
    @Test(expected = NullPointerException.class)
    public void fromLittleEndianWithNullInputStreamShouldThrowNullPointerException() {
        // Define a valid length for the second argument to isolate the test's focus
        // on the null InputStream.
        final int validLength = 8;

        // This call is expected to throw a NullPointerException because the input stream is null.
        ByteUtils.fromLittleEndian((InputStream) null, validLength);
    }
}