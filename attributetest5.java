package org.jsoup.nodes;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Tests for the {@link Attribute} class.
 */
// Class name improved from AttributeTestTest5 to the standard convention.
class AttributeTest {

    @Test
    // Method name improved to clearly state the behavior under test:
    // what method is called (setKey), with what kind of input (a blank key),
    // and what the expected outcome is (throws an exception).
    void setKeyWithBlankKeyThrowsIllegalArgumentException() {
        // Arrange: Create a valid attribute instance.
        Attribute attribute = new Attribute("validKey", "someValue");
        String blankKey = "   "; // A key with only whitespace is invalid.

        // Act & Assert: Verify that attempting to set a blank key throws an exception.
        // The setKey method is expected to trim the input, find it to be empty, and then throw.
        assertThrows(IllegalArgumentException.class, () -> {
            attribute.setKey(blankKey);
        });
    }
}