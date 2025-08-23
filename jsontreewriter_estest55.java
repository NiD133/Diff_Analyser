package com.google.gson.internal.bind;

import java.io.IOException;
import org.junit.Test;

/**
 * Contains tests for the {@link JsonTreeWriter} class.
 */
public class JsonTreeWriterTest {

    /**
     * Verifies that attempting to close a JSON object immediately after writing a name
     * (but before writing its corresponding value) throws an IllegalStateException.
     * This is often referred to as a "dangling name" scenario.
     */
    @Test(expected = IllegalStateException.class)
    public void endObject_withDanglingName_throwsIllegalStateException() throws IOException {
        // Arrange
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginObject();
        writer.name("some_property"); // Set a name, creating a "dangling name" state.

        // Act: Attempt to close the object while in an invalid state.
        // The @Test(expected) annotation asserts that an IllegalStateException is thrown.
        writer.endObject();
    }
}