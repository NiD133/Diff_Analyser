package com.google.gson.internal.bind;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import com.google.gson.internal.bind.JsonTreeWriter;
import java.io.IOException;
import org.junit.Test;

/**
 * Test suite for {@link JsonTreeWriter}.
 */
public class JsonTreeWriterTest {

    @Test
    public void name_whenInArrayContext_throwsIllegalStateException() throws IOException {
        // Arrange: Create a writer and start an array.
        // In an array context, only values can be written, not names.
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginArray();

        // Act & Assert: Attempting to write a name should fail.
        try {
            writer.name("someName");
            fail("Expected an IllegalStateException because a name cannot be written inside an array.");
        } catch (IllegalStateException expected) {
            // Verify the exception message is helpful and correct.
            assertEquals("Please begin an object before writing a name.", expected.getMessage());
        }
    }
}