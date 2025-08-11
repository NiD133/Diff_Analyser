package com.fasterxml.jackson.core.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for class {@link DefaultIndenter}.
 *
 * @date 2017-07-31
 * @see DefaultIndenter
 **/
class DefaultIndenterTest {

    @Test
    @DisplayName("withLinefeed() should return a new instance for a new linefeed, but the same instance if the linefeed is unchanged")
    void withLinefeed_shouldBehaveImmutably() {
        // Arrange
        final String customLineFeed = "\r\n";
        DefaultIndenter originalIndenter = new DefaultIndenter();
        // Ensure the original linefeed is different from our custom one for the test to be meaningful
        assertNotEquals(customLineFeed, originalIndenter.getEol());

        // Act: Create an indenter with a new, custom linefeed
        DefaultIndenter updatedIndenter = originalIndenter.withLinefeed(customLineFeed);

        // Act: Call withLinefeed again with the same linefeed to test the caching mechanism
        DefaultIndenter cachedIndenter = updatedIndenter.withLinefeed(customLineFeed);

        // Assert
        // 1. The linefeed property is correctly updated on the new instance.
        assertEquals(customLineFeed, updatedIndenter.getEol(),
                "The new indenter should have the specified linefeed.");

        // 2. The class is immutable: a new instance is returned when the linefeed changes.
        assertNotSame(originalIndenter, updatedIndenter,
                "Should return a new instance when the linefeed changes.");

        // 3. The method is optimized: the same instance is returned if the linefeed is unchanged.
        assertSame(updatedIndenter, cachedIndenter,
                "Should return the same instance when the linefeed is unchanged.");
    }

    @Test
    @DisplayName("withIndent() should return a new instance for a new indent, but the same instance if the indent is unchanged")
    void withIndent_shouldBehaveImmutably() {
        // Arrange
        final String customIndent = "    "; // 4 spaces
        DefaultIndenter originalIndenter = new DefaultIndenter();
        // The default indent is "  " (2 spaces), so our custom one is different.

        // Act: Create an indenter with a new, custom indent string.
        DefaultIndenter updatedIndenter = originalIndenter.withIndent(customIndent);

        // Act: Call withIndent again with the same indent to test the caching mechanism.
        DefaultIndenter cachedIndenter = updatedIndenter.withIndent(customIndent);

        // Assert
        // 1. The linefeed remains the default, as it was not meant to be changed.
        assertEquals(System.lineSeparator(), updatedIndenter.getEol(),
                "The linefeed should not be affected by withIndent().");

        // 2. The class is immutable: a new instance is returned when the indent changes.
        assertNotSame(originalIndenter, updatedIndenter,
                "Should return a new instance when the indent changes.");

        // 3. The method is optimized: the same instance is returned if the indent is unchanged.
        assertSame(updatedIndenter, cachedIndenter,
                "Should return the same instance when the indent is unchanged.");
    }
}