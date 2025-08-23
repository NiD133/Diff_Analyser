package com.google.gson.internal.bind;

import com.google.gson.internal.bind.JsonTreeWriter;
import java.io.IOException;
import org.junit.Test;

// The original test class name and inheritance are preserved for context.
public class JsonTreeWriter_ESTestTest59 extends JsonTreeWriter_ESTest_scaffolding {

    /**
     * Verifies that calling endArray() when the writer is not in an array context
     * (e.g., at the top level after an object has been closed) throws an IllegalStateException.
     */
    @Test(expected = IllegalStateException.class)
    public void endArray_whenNotInArrayContext_throwsIllegalStateException() throws IOException {
        // Arrange: Create a writer and write a complete, closed object.
        // This leaves the writer at the top level, not inside an array.
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginObject();
        writer.endObject();

        // Act: Attempt to close an array, which was never opened.
        // Assert: The @Test(expected) annotation asserts that an IllegalStateException is thrown.
        writer.endArray();
    }
}