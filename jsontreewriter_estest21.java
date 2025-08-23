package com.google.gson.internal.bind;

import com.google.gson.Strictness;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link JsonTreeWriter}.
 */
public class JsonTreeWriterTest {

    /**
     * Verifies that a newly instantiated JsonTreeWriter defaults to LEGACY_STRICT mode.
     * This is an inherited behavior from the parent JsonWriter class.
     */
    @Test
    public void newJsonTreeWriter_shouldHaveLegacyStrictnessByDefault() {
        // Arrange: Create a new JsonTreeWriter instance.
        JsonTreeWriter writer = new JsonTreeWriter();

        // Act: Get the strictness level of the new writer.
        Strictness actualStrictness = writer.getStrictness();

        // Assert: The strictness level should be the default legacy value.
        assertEquals("A new JsonTreeWriter should default to LEGACY_STRICT.",
                Strictness.LEGACY_STRICT, actualStrictness);
    }
}