package com.google.gson.internal.bind;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.IOException;

/**
 * Tests for {@link JsonTreeWriter}.
 */
public class JsonTreeWriterTest {

    @Rule
    public final ExpectedException thrown = ExpectedException.none();

    @Test
    public void beginObject_onClosedWriter_throwsIllegalStateException() throws IOException {
        // Arrange: Create a writer and close it immediately.
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.close();

        // Assert: Expect an IllegalStateException when any write operation is attempted.
        thrown.expect(IllegalStateException.class);
        // The original exception has no message, so we don't check for one.

        // Act: Attempt to start a new JSON object.
        writer.beginObject();
    }
}