package com.google.gson.internal.bind;

import static org.junit.Assert.assertFalse;
import org.junit.Test;

/**
 * Unit tests for the {@link JsonTreeWriter} class.
 */
public class JsonTreeWriterTest {

    /**
     * Verifies that a new {@link JsonTreeWriter} instance is configured to be strict
     * (the opposite of lenient) by default. Strict behavior ensures that the writer
     * adheres closely to the JSON specification.
     */
    @Test
    public void newWriterIsStrictByDefault() {
        // Arrange: Create a new JsonTreeWriter instance.
        JsonTreeWriter writer = new JsonTreeWriter();

        // Assert: Verify that the writer's default leniency setting is false.
        assertFalse("A new JsonTreeWriter should be strict (not lenient) by default.", writer.isLenient());
    }
}