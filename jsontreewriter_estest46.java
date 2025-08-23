package com.google.gson.internal.bind;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;

/**
 * This class contains tests for JsonTreeWriter.
 * This particular test was improved for clarity.
 */
// The original test class name and inheritance are preserved.
public class JsonTreeWriter_ESTestTest46 extends JsonTreeWriter_ESTest_scaffolding {

    /**
     * Verifies that closing a writer with an unclosed array (an incomplete document)
     * throws an IOException.
     */
    @Test
    public void close_withOpenArray_throwsIOException() {
        // Arrange: Create a writer and start an array, but do not close it.
        JsonTreeWriter writer = new JsonTreeWriter();
        try {
            writer.beginArray();
        } catch (IOException e) {
            // This is part of the setup and should not fail.
            fail("Test setup failed: beginArray() should not throw an exception here.");
        }

        // Act & Assert: Attempting to close the writer should fail.
        try {
            writer.close();
            fail("Expected an IOException because the JSON document is incomplete, but no exception was thrown.");
        } catch (IOException expected) {
            // Verify that the exception message clearly states the reason for the failure.
            assertEquals("Incomplete document", expected.getMessage());
        }
    }
}