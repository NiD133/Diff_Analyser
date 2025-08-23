package com.google.gson.internal.bind;

import org.junit.Test;
import java.io.IOException;

/**
 * Tests for {@link JsonTreeWriter} focusing on state validation.
 */
// The original test class name and inheritance are kept for consistency.
public class JsonTreeWriter_ESTestTest57 extends JsonTreeWriter_ESTest_scaffolding {

    /**
     * Verifies that calling endArray() when the current context is an object
     * throws an IllegalStateException. This ensures the writer enforces a valid
     * JSON structure by preventing mismatched begin/end calls.
     */
    @Test(expected = IllegalStateException.class)
    public void endArray_whenInObjectContext_throwsIllegalStateException() throws IOException {
        // Arrange: Create a writer and open an object context.
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginObject();

        // Act: Attempt to close an array, which is an invalid state transition.
        // The @Test(expected=...) annotation will assert that an IllegalStateException is thrown.
        writer.endArray();
    }
}