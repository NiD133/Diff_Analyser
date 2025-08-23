package com.google.gson.internal.bind;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

/**
 * Unit tests for the {@link JsonTreeWriter} class.
 */
public class JsonTreeWriterTest {

    /**
     * Verifies that writing a value into a JSON object without first calling {@code name()}
     * throws an {@link IllegalStateException}. In a valid JSON object, every value must
     * be part of a name/value pair.
     */
    @Test
    public void writeValueInObjectWithoutNameShouldThrowIllegalStateException() {
        // Arrange: Create a writer and start a JSON object.
        JsonTreeWriter writer = new JsonTreeWriter();
        try {
            writer.beginObject();
        } catch (IOException e) {
            fail("Test setup failed: " + e.getMessage());
        }

        // Act & Assert: Attempting to write a value without a name should fail.
        try {
            writer.value(108L);
            fail("Expected an IllegalStateException to be thrown when writing a value "
                + "inside an object without a preceding name call.");
        } catch (IllegalStateException expected) {
            // This is the correct behavior.
            // The specific implementation throws an exception with a null message.
            assertNull("The exception message should be null.", expected.getMessage());
        } catch (IOException e) {
            fail("An unexpected IOException was thrown: " + e.getMessage());
        }
    }
}