package com.fasterxml.jackson.core.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the {@link DefaultIndenter} class, focusing on its immutability.
 */
// Renamed from DefaultIndenterTestTest1 for clarity and convention.
class DefaultIndenterTest {

    @Test
    @DisplayName("withLinefeed should create a new instance for a new linefeed, but return the same instance if the linefeed is unchanged")
    void withLinefeed_shouldBehaveImmutably() {
        // Arrange
        final String customLinefeed = "-XG'#x";
        // Start with an indenter using the default system linefeed.
        final DefaultIndenter originalIndenter = new DefaultIndenter();

        // Act: Create a new indenter by changing the linefeed.
        final DefaultIndenter updatedIndenter = originalIndenter.withLinefeed(customLinefeed);

        // Assert: A new, distinct instance should be created.
        assertNotSame(originalIndenter, updatedIndenter,
                "A new instance should be created when the linefeed is changed.");
        assertEquals(customLinefeed, updatedIndenter.getEol(),
                "The new instance should have the specified custom linefeed.");

        // Act: Call withLinefeed again with the *same* custom linefeed.
        final DefaultIndenter sameIndenter = updatedIndenter.withLinefeed(customLinefeed);

        // Assert: The method should be idempotent, returning the same instance.
        assertSame(updatedIndenter, sameIndenter,
                "The same instance should be returned when the linefeed is not changed.");
        assertEquals(customLinefeed, sameIndenter.getEol(),
                "The linefeed should remain unchanged.");
    }
}