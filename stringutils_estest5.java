package com.itextpdf.text.pdf;

import org.junit.Test;

/**
 * Unit tests for the {@link StringUtils} class.
 */
public class StringUtilsTest {

    /**
     * Verifies that the escapeString method throws a NullPointerException
     * when the input byte array is null. This ensures the method correctly
     * handles invalid null input.
     */
    @Test(expected = NullPointerException.class)
    public void escapeString_withNullInput_shouldThrowNullPointerException() {
        // When the escapeString method is called with a null argument,
        // a NullPointerException is expected.
        StringUtils.escapeString(null);
    }
}