package com.fasterxml.jackson.core.util;

import org.junit.Test;
import static org.junit.Assert.assertSame;

/**
 * Unit tests for the {@link DefaultIndenter} class, focusing on its configuration methods.
 */
public class DefaultIndenterTest {

    /**
     * Tests that calling {@code withLinefeed()} with the same linefeed string
     * returns the original instance, confirming it avoids unnecessary object creation.
     */
    @Test
    public void withLinefeed_shouldReturnSameInstance_whenLinefeedIsUnchanged() {
        // Arrange: Create an indenter with a specific, known linefeed.
        // This approach removes the original test's dependency on the system's
        // default line separator, making the test more robust and reliable.
        final String linefeed = "\n";
        DefaultIndenter indenter = new DefaultIndenter("  ", linefeed);

        // Act: Call withLinefeed() with the exact same linefeed string.
        DefaultIndenter result = indenter.withLinefeed(linefeed);

        // Assert: The method should return the identical instance, not a new one.
        assertSame("Expected withLinefeed() to return the same instance for an identical linefeed",
                indenter, result);
    }
}