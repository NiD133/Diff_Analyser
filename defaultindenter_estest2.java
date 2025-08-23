package com.fasterxml.jackson.core.util;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for the {@link DefaultIndenter} class.
 */
public class DefaultIndenterTest {

    /**
     * Verifies that the `withLinefeed` method correctly creates a new indenter
     * instance with the specified linefeed, while leaving the original instance unchanged.
     */
    @Test
    public void withLinefeed_shouldCreateNewInstanceWithSpecifiedLinefeed() {
        // Arrange: Create an initial indenter with a specific indent and linefeed (CRLF).
        final String originalIndent = "  ";
        final String originalLinefeed = "\r\n";
        DefaultIndenter originalIndenter = new DefaultIndenter(originalIndent, originalLinefeed);

        // Act: Call withLinefeed to create a new indenter with a different linefeed (LF).
        final String newLinefeed = "\n";
        DefaultIndenter updatedIndenter = originalIndenter.withLinefeed(newLinefeed);

        // Assert: Verify the state of both the new and original indenters.
        // 1. The new instance should have the updated linefeed.
        assertEquals("The new indenter should have the specified linefeed.", newLinefeed, updatedIndenter.getEol());

        // 2. The new instance should retain the original indentation string.
        assertEquals("Indentation should be preserved.", originalIndent, updatedIndenter.getIndent());

        // 3. The original instance should remain unchanged, confirming immutability.
        assertEquals("The original indenter's linefeed should not be modified.", originalLinefeed, originalIndenter.getEol());

        // 4. The method should return a new object instance.
        assertNotSame("A new instance should be returned.", originalIndenter, updatedIndenter);
    }
}