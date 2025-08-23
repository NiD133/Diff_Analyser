package com.google.gson.internal.bind;

import com.google.gson.internal.bind.JsonTreeWriter;
import java.io.IOException;
import org.junit.Test;

/**
 * Test suite for {@link JsonTreeWriter}.
 */
public class JsonTreeWriterTest {

    /**
     * Verifies that attempting to write to a JsonTreeWriter after it has been closed
     * results in an IllegalStateException.
     */
    @Test(expected = IllegalStateException.class)
    public void beginArray_afterClose_throwsIllegalStateException() throws IOException {
        // Arrange: Create a writer and immediately close it.
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.close();

        // Act: Attempt to write to the closed writer.
        // This action is expected to throw the IllegalStateException.
        writer.beginArray();
    }
}