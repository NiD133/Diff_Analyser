package com.fasterxml.jackson.core.io;

import org.junit.Test;

/**
 * Unit tests for the {@link BigDecimalParser} class.
 */
public class BigDecimalParserTest {

    /**
     * Verifies that passing a null String to the {@code parse} method
     * results in a {@link NullPointerException}, as expected for invalid input.
     */
    @Test(expected = NullPointerException.class)
    public void parseStringShouldThrowNullPointerExceptionForNullInput() {
        // Act & Assert
        BigDecimalParser.parse((String) null);
    }
}