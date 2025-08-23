package com.google.gson.internal.bind;

import com.google.gson.JsonObject;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * This class contains an improved version of an auto-generated test for JsonTreeReader.
 * The original test has been refactored for better understandability.
 */
public class JsonTreeReaderImprovedTest {

    /**
     * Verifies that calling nextBoolean() when the reader is positioned at the end
     * of an empty JSON object throws an IllegalStateException.
     */
    @Test
    public void nextBoolean_atEndOfEmptyObject_throwsIllegalStateException() throws IOException {
        // Arrange: Create a reader for an empty JSON object "{}" and advance it
        // past the opening brace.
        JsonObject emptyObject = new JsonObject();
        JsonTreeReader reader = new JsonTreeReader(emptyObject);
        reader.beginObject();

        // Act & Assert: Verify that calling nextBoolean() throws an exception
        // because the next token is END_OBJECT, not a boolean.
        try {
            reader.nextBoolean();
            fail("Expected an IllegalStateException to be thrown.");
        } catch (IllegalStateException e) {
            assertEquals("Expected BOOLEAN but was END_OBJECT at path $.", e.getMessage());
        }
    }
}