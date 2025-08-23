package org.jsoup.nodes;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Tests for the {@link Attribute} class, focusing on constructor validation.
 */
@DisplayName("Attribute Creation")
class AttributeTest {

    @Test
    @DisplayName("should throw IllegalArgumentException for a blank key")
    void constructor_whenKeyIsBlank_throwsIllegalArgumentException() {
        // The Attribute constructor trims the key and then validates that it's not empty.
        // This test ensures that a key containing only whitespace is correctly rejected.
        assertThrows(IllegalArgumentException.class, () -> new Attribute(" ", "any-value"));
    }
}