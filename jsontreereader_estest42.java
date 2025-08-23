package com.google.gson.internal.bind;

import com.google.gson.JsonNull;
import org.junit.Test;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Tests for {@link JsonTreeReader}.
 */
public class JsonTreeReaderTest {

    /**
     * Verifies that calling beginArray() on a reader pointing to a JsonNull
     * element throws an IllegalStateException.
     */
    @Test
    public void beginArray_whenCurrentElementIsJsonNull_throwsIllegalStateException() throws IOException {
        // Arrange: Create a JsonTreeReader for a JsonNull element.
        JsonTreeReader reader = new JsonTreeReader(JsonNull.INSTANCE);
        String expectedMessage = "Expected BEGIN_ARRAY but was NULL at path $";

        // Act & Assert: Attempt to start reading an array and verify the exception.
        try {
            reader.beginArray();
            fail("Expected an IllegalStateException to be thrown, but no exception was thrown.");
        } catch (IllegalStateException e) {
            assertEquals("The exception message did not match the expected value.",
                    expectedMessage, e.getMessage());
        }
    }
}