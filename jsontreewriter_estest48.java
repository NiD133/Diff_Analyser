package com.google.gson.internal.bind;

import org.junit.Test;
import static org.junit.Assert.assertFalse;

import java.io.IOException;

/**
 * Tests for the {@link JsonTreeWriter} class.
 */
public class JsonTreeWriterTest {

    /**
     * Verifies that the writer is not in "HTML-safe" mode by default,
     * and that writing a value does not alter this state.
     */
    @Test
    public void writerIsNotHtmlSafeByDefaultAfterWritingValue() throws IOException {
        // Arrange: Create a new JsonTreeWriter instance.
        JsonTreeWriter writer = new JsonTreeWriter();

        // Act: Write a primitive value. This action ensures the writer is in an
        // active state before the assertion.
        writer.value(-1.0);

        // Assert: Verify that the writer's isHtmlSafe property is false.
        // This is the default behavior inherited from the JsonWriter superclass.
        assertFalse("A new writer should not be HTML-safe by default.", writer.isHtmlSafe());
    }
}