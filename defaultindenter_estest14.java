package com.fasterxml.jackson.core.util;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link DefaultIndenter} class.
 */
public class DefaultIndenterTest {

    /**
     * Verifies that the default constructor of {@link DefaultIndenter} correctly
     * initializes the end-of-line (EOL) sequence to the system's default line separator.
     */
    @Test
    public void defaultConstructorShouldUseSystemLineFeedAsEndOfLine() {
        // Arrange
        // The DefaultIndenter is expected to use the system's line separator by default.
        final String expectedEol = System.getProperty("line.separator");
        final DefaultIndenter indenter = new DefaultIndenter();

        // Act
        final String actualEol = indenter.getEol();

        // Assert
        assertEquals("The EOL should match the system's default line separator.", expectedEol, actualEol);
    }
}