package com.fasterxml.jackson.core.util;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link DefaultIndenter} class.
 */
public class DefaultIndenterTest {

    /**
     * Verifies that the default constructor of {@link DefaultIndenter} initializes
     * the end-of-line (EOL) sequence to the system's default line separator.
     */
    @Test
    public void should_useSystemLineFeed_when_usingDefaultConstructor() {
        // Arrange
        // The DefaultIndenter's static initializer uses System.getProperty("line.separator")
        // with a fallback to "\n". We replicate that logic for a robust check.
        final String expectedEol = System.getProperty("line.separator", "\n");
        final DefaultIndenter indenter = new DefaultIndenter();

        // Act
        final String actualEol = indenter.getEol();

        // Assert
        assertEquals("The default EOL should match the system's line separator.", expectedEol, actualEol);
    }
}