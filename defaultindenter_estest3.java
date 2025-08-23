package com.fasterxml.jackson.core.util;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link DefaultIndenter} class.
 */
public class DefaultIndenterTest {

    /**
     * Verifies that the {@link DefaultIndenter#getEol()} method returns the
     * exact end-of-line (EOL) string that was provided in the constructor.
     */
    @Test
    public void shouldReturnEolStringProvidedInConstructor() {
        // Arrange: Create an indenter with an empty indent and a custom EOL string.
        final String customEol = "As4M!C";
        final String emptyIndent = "";
        DefaultIndenter indenter = new DefaultIndenter(emptyIndent, customEol);

        // Act: Retrieve the EOL string from the indenter.
        String actualEol = indenter.getEol();

        // Assert: The retrieved EOL string should match the one used during construction.
        assertEquals(customEol, actualEol);
    }
}