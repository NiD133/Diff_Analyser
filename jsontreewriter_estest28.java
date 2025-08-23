package com.google.gson.internal.bind;

import com.google.gson.stream.JsonWriter;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Tests for {@link JsonTreeWriter}.
 */
public class JsonTreeWriterTest {

    /**
     * Tests that the {@code serializeNulls} property is enabled by default on a new
     * {@code JsonTreeWriter} instance.
     */
    @Test
    public void serializeNullsShouldBeEnabledByDefault() {
        // Arrange
        JsonWriter jsonTreeWriter = new JsonTreeWriter();

        // Act & Assert
        // The JsonWriter.serializeNulls property is true by default and should not be
        // affected by other operations.
        assertTrue("Expected serializeNulls to be true for a new JsonTreeWriter", jsonTreeWriter.getSerializeNulls());
    }
}