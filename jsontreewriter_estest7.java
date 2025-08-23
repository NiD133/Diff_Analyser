package com.google.gson.internal.bind;

import static org.junit.Assert.assertFalse;
import org.junit.Test;

/**
 * Tests for {@link JsonTreeWriter}.
 */
public class JsonTreeWriterTest {

    /**
     * Verifies that a new JsonTreeWriter instance is not HTML-safe by default.
     * The isHtmlSafe property is inherited from the parent JsonWriter class,
     * where its default value is false.
     */
    @Test
    public void isHtmlSafe_isFalseByDefault() {
        // Arrange: Create a new JsonTreeWriter instance.
        JsonTreeWriter writer = new JsonTreeWriter();

        // Act & Assert: Check the default state of the isHtmlSafe property.
        assertFalse("A new JsonTreeWriter should not be HTML-safe by default", writer.isHtmlSafe());
    }
}