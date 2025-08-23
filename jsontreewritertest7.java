package com.google.gson.internal.bind;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertThrows;

import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import org.junit.Test;

/**
 * Tests for edge cases when calling {@link JsonTreeWriter#name(String)}.
 */
public class JsonTreeWriterTest {

    /**
     * Verifies that a JSON document cannot start with a property name.
     * The top-level element must be a value (e.g., an object, array, or primitive).
     */
    @Test
    public void name_atTopLevel_throwsException() {
        // Arrange
        JsonWriter writer = new JsonTreeWriter();

        // Act & Assert
        IllegalStateException e = assertThrows(IllegalStateException.class, () -> writer.name("hello"));
        assertThat(e).hasMessageThat().isEqualTo("Did not expect a name");
    }

    /**
     * Verifies that no new properties can be written after the writer has been closed.
     */
    @Test
    public void name_afterWriterIsClosed_throwsException() throws IOException {
        // Arrange
        JsonWriter writer = new JsonTreeWriter();
        writer.value(12); // Write a complete top-level value
        writer.close();   // Close the writer

        // Act & Assert
        IllegalStateException e = assertThrows(IllegalStateException.class, () -> writer.name("hello"));
        assertThat(e).hasMessageThat().isEqualTo("Please begin an object before writing a name.");
    }
}