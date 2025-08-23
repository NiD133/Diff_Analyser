package com.google.gson.internal.bind;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Unit tests for the {@link JsonTreeWriter} class.
 */
public class JsonTreeWriterTest {

    /**
     * Verifies that calling the name() method with a null argument
     * results in a NullPointerException, as the name of a JSON property cannot be null.
     */
    @Test
    public void name_whenNameIsNull_throwsNullPointerException() {
        // Arrange
        JsonTreeWriter writer = new JsonTreeWriter();

        // Act & Assert
        try {
            writer.name(null);
            fail("Expected a NullPointerException to be thrown, but no exception was thrown.");
        } catch (NullPointerException e) {
            // The underlying implementation uses Objects.requireNonNull, which provides this specific message.
            assertEquals("name == null", e.getMessage());
        }
    }
}