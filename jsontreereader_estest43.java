package com.google.gson.internal.bind;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import com.google.gson.JsonElement;
import java.io.IOException;
import org.junit.Test;

/**
 * This test class contains tests for the JsonTreeReader.
 * The original test was auto-generated and has been refactored for clarity.
 */
public class JsonTreeReader_ESTestTest43 extends JsonTreeReader_ESTest_scaffolding {

    /**
     * Verifies that calling peek() on a closed reader throws an IllegalStateException.
     */
    @Test
    public void peekOnClosedReaderShouldThrowIllegalStateException() throws IOException {
        // Arrange: Create a JsonTreeReader and immediately close it.
        // The initial JsonElement is null, but this is irrelevant to the test's purpose,
        // which is to check the behavior of a closed reader.
        JsonTreeReader reader = new JsonTreeReader((JsonElement) null);
        reader.close();

        // Act & Assert: Expect an IllegalStateException when peek() is called.
        try {
            reader.peek();
            fail("Expected an IllegalStateException to be thrown, but no exception occurred.");
        } catch (IllegalStateException expected) {
            // Verify that the exception message is correct.
            assertEquals("JsonReader is closed", expected.getMessage());
        }
    }
}