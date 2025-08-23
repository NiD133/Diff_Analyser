package com.itextpdf.text.pdf;

import org.junit.Test;

/**
 * Unit tests for the {@link StringUtils} class.
 */
public class StringUtilsTest {

    /**
     * Verifies that calling escapeString with a null ByteBuffer argument
     * results in a NullPointerException.
     */
    @Test(expected = NullPointerException.class)
    public void escapeString_withNullByteBuffer_throwsNullPointerException() {
        // Arrange: Create a non-null byte array. Its content is irrelevant for this test.
        byte[] anyBytes = new byte[1];
        ByteBuffer nullBuffer = null;

        // Act & Assert: This call is expected to throw a NullPointerException.
        StringUtils.escapeString(anyBytes, nullBuffer);
    }
}