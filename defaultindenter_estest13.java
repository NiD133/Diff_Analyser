package com.fasterxml.jackson.core.util;

import org.junit.Test;

/**
 * Unit tests for the {@link DefaultIndenter} class.
 */
public class DefaultIndenterTest {

    /**
     * Verifies that the constructor throws a NullPointerException
     * when the indentation string provided is null. The end-of-line
     * string is irrelevant in this case, as the indentation string
     * is validated first.
     */
    @Test(expected = NullPointerException.class)
    public void constructor_whenIndentIsNull_shouldThrowNullPointerException() {
        // Act & Assert
        // Attempting to create an indenter with a null 'indent' string
        // is expected to fail immediately with a NullPointerException.
        new DefaultIndenter(null, DefaultIndenter.SYS_LF);
    }
}