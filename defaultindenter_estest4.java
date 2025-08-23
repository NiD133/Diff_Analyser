package com.fasterxml.jackson.core.util;

import org.junit.Test;
import static org.junit.Assert.assertNull;

/**
 * Unit tests for the {@link DefaultIndenter} class.
 */
public class DefaultIndenterTest {

    /**
     * Verifies that the getEol() method returns null if the indenter was
     * constructed with a null end-of-line (EOL) string.
     */
    @Test
    public void getEolShouldReturnNullWhenConstructedWithNullEol() {
        // Arrange: Create an indenter with a standard indent but a null EOL string.
        // The specific indent string used here is not relevant to this test's outcome.
        DefaultIndenter indenter = new DefaultIndenter("  ", null);

        // Act: Retrieve the configured end-of-line string.
        String actualEol = indenter.getEol();

        // Assert: The returned EOL string should be null, matching the value
        // passed to the constructor.
        assertNull(actualEol);
    }
}