package com.google.gson.internal.bind;

import com.google.gson.JsonNull;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Unit tests for {@link JsonTreeReader}.
 */
public class JsonTreeReaderTest {

    /**
     * Verifies that calling endArray() when the current token is JSON null
     * results in an IllegalStateException. This tests the reader's state validation.
     */
    @Test
    public void endArray_whenTokenIsNull_throwsIllegalStateException() {
        // Arrange: Create a JsonTreeReader positioned at a JsonNull element.
        JsonTreeReader reader = new JsonTreeReader(JsonNull.INSTANCE);
        String expectedMessage = "Expected END_ARRAY but was NULL at path $";

        // Act & Assert: Attempting to end an array should fail.
        try {
            reader.endArray();
            fail("Expected an IllegalStateException to be thrown, but no exception was thrown.");
        } catch (IllegalStateException actualException) {
            assertEquals(expectedMessage, actualException.getMessage());
        }
    }
}