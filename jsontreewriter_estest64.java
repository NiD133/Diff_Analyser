package com.google.gson.internal.bind;

import org.junit.Test;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * This class contains tests for JsonTreeWriter.
 * This particular test was refactored for clarity from an auto-generated test case.
 */
public class JsonTreeWriter_ESTestTest64 extends JsonTreeWriter_ESTest_scaffolding {

    /**
     * Verifies that calling get() on a writer with an unclosed array
     * results in an IllegalStateException.
     */
    @Test
    public void getShouldThrowIllegalStateExceptionForUnclosedArray() throws IOException {
        // Arrange: Create a writer and start an array without closing it.
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginArray();

        // Act & Assert: Attempting to get the final JsonElement should fail.
        try {
            writer.get();
            fail("Expected an IllegalStateException because the JSON document is not complete.");
        } catch (IllegalStateException expected) {
            // Verify the exception message clearly states the problem.
            assertEquals("Expected one JSON element but was [[]]", expected.getMessage());
        }
    }
}