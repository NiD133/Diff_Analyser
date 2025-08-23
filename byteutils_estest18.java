package org.apache.commons.compress.utils;

import org.junit.Test;

/**
 * Unit tests for exception-throwing scenarios in the {@link ByteUtils} class.
 */
public class ByteUtilsTest {

    /**
     * Verifies that calling {@code toLittleEndian} with a null byte array
     * throws a {@code NullPointerException}. The method cannot write to a
     * non-existent array, so this exception is the correct and expected behavior.
     */
    @Test(expected = NullPointerException.class)
    public void toLittleEndianShouldThrowNullPointerExceptionWhenArrayIsNull() {
        // Arrange: The other parameters are arbitrary, as the null check on the array
        // is expected to happen before they are used.
        final long arbitraryValue = -2448L;
        final int arbitraryOffset = 8;
        final int arbitraryLength = 8;

        // Act & Assert: This call should throw a NullPointerException.
        ByteUtils.toLittleEndian(null, arbitraryValue, arbitraryOffset, arbitraryLength);
    }
}