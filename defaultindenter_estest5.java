package com.fasterxml.jackson.core.util;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link DefaultIndenter} class, focusing on its configuration methods.
 */
public class DefaultIndenterTest {

    /**
     * Tests that the {@code withLinefeed()} method correctly creates a new
     * DefaultIndenter instance with the specified line feed string.
     */
    @Test
    public void withLinefeed_shouldCreateNewInstanceWithSpecifiedLinefeed() {
        // Arrange
        final DefaultIndenter originalIndenter = new DefaultIndenter();
        final String customLinefeed = "";

        // Act
        // The 'withLinefeed' method is a "wither" that returns a new, configured instance.
        DefaultIndenter updatedIndenter = originalIndenter.withLinefeed(customLinefeed);
        String actualLinefeed = updatedIndenter.getEol();

        // Assert
        assertEquals("The new indenter instance should have the specified line feed.",
                customLinefeed, actualLinefeed);
    }
}