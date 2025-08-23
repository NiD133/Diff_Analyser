package com.itextpdf.text.pdf;

import org.junit.Test;

/**
 * Unit tests for the {@link StringUtils} class, focusing on edge cases and invalid inputs.
 */
public class StringUtilsTest {

    /**
     * Verifies that calling {@link StringUtils#convertCharsToBytes(char[])} with a null
     * input array correctly throws a {@link NullPointerException}. This is the expected
     * behavior for methods that do not explicitly handle null inputs.
     */
    @Test(expected = NullPointerException.class)
    public void convertCharsToBytes_shouldThrowNullPointerException_whenInputArrayIsNull() {
        // This call is expected to throw a NullPointerException, which is caught and
        // verified by the @Test(expected=...) annotation.
        StringUtils.convertCharsToBytes(null);
    }
}