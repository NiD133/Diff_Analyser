package com.fasterxml.jackson.core.util;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for the {@link DefaultIndenter} class, focusing on its configuration and immutability.
 */
public class DefaultIndenterTest {

    /**
     * Verifies that the {@code withLinefeed} method creates a new {@link DefaultIndenter}
     * instance with the specified line feed string, while the original instance remains unchanged.
     */
    @Test
    public void withLinefeed_shouldCreateNewInstanceWithUpdatedEol() {
        // Given: An initial DefaultIndenter with a specific indentation and line feed.
        final String originalIndent = "  ";
        final String originalEol = "\r\n"; // Windows-style line feed
        DefaultIndenter originalIndenter = new DefaultIndenter(originalIndent, originalEol);

        // When: A new indenter is created by changing the line feed.
        final String newEol = "\n"; // Unix-style line feed
        DefaultIndenter updatedIndenter = originalIndenter.withLinefeed(newEol);

        // Then: The new indenter should have the updated line feed, while the original
        // indenter and other properties remain unchanged.
        assertNotSame("A new instance should be created to ensure immutability.", originalIndenter, updatedIndenter);

        assertEquals("The updated indenter should use the new line feed.", newEol, updatedIndenter.getEol());
        assertEquals("The indentation string should be carried over to the new instance.", originalIndent, updatedIndenter.getIndent());

        assertEquals("The original indenter's line feed should not be modified.", originalEol, originalIndenter.getEol());
    }
}