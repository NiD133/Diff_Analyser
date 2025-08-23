package com.fasterxml.jackson.core.util;

import org.junit.Test;

/**
 * Unit tests for the {@link ByteArrayBuilder} class, focusing on exception handling.
 */
public class ByteArrayBuilderTest {

    /**
     * Verifies that the write(byte[], int, int) method throws an
     * IndexOutOfBoundsException when provided with a negative length argument.
     * This behavior is consistent with the contract of java.io.OutputStream.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void writeWithNegativeLengthShouldThrowException() {
        // Given a ByteArrayBuilder instance
        ByteArrayBuilder builder = new ByteArrayBuilder();
        byte[] anyData = new byte[10];
        int validOffset = 0;
        int invalidNegativeLength = -1;

        // When writing with a negative length
        builder.write(anyData, validOffset, invalidNegativeLength);

        // Then an IndexOutOfBoundsException is expected
    }
}