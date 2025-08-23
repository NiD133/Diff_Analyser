package com.google.gson.internal.bind;

import com.google.gson.internal.bind.JsonTreeWriter;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Tests for {@link JsonTreeWriter}.
 */
public class JsonTreeWriterTest {

    /**
     * Verifies that calling {@link JsonTreeWriter#name(String)} is not allowed when the writer
     * is not in an object scope. A name can only be written after {@code beginObject()} has been called.
     */
    @Test
    public void name_whenNotInObject_throwsIllegalStateException() {
        // Arrange
        JsonTreeWriter writer = new JsonTreeWriter();
        String expectedErrorMessage = "Did not expect a name";

        // Act & Assert
        try {
            writer.name("test.name");
            fail("Expected an IllegalStateException because name() was called outside of an object.");
        } catch (IllegalStateException e) {
            // This is the expected outcome
            assertEquals(expectedErrorMessage, e.getMessage());
        } catch (IOException e) {
            // The method signature includes IOException, but it's not expected in this scenario.
            fail("An unexpected IOException was thrown: " + e.getMessage());
        }
    }
}