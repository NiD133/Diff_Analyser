package com.google.gson.internal.bind;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertThrows;

import com.google.gson.JsonArray;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import org.junit.Test;

/**
 * Tests for {@link JsonTreeWriter}.
 */
public class JsonTreeWriterTest {

    /**
     * Verifies that attempting to write a property name inside a JSON array
     * is not allowed and throws an IllegalStateException.
     */
    @Test
    public void name_whenInArray_throwsIllegalStateException() throws IOException {
        // Arrange
        JsonWriter writer = new JsonTreeWriter();
        writer.beginArray();

        // Act & Assert: Calling name() immediately after beginArray() should fail.
        IllegalStateException e1 = assertThrows(
            IllegalStateException.class,
            () -> writer.name("a")
        );
        assertThat(e1).hasMessageThat().isEqualTo("Please begin an object before writing a name.");

        // Add a value to the array to ensure the state remains invalid for names.
        writer.value(12);

        // Act & Assert: Calling name() after a value in an array should also fail.
        IllegalStateException e2 = assertThrows(
            IllegalStateException.class,
            () -> writer.name("b")
        );
        assertThat(e2).hasMessageThat().isEqualTo("Please begin an object before writing a name.");

        // Finalize the array and verify its content.
        writer.endArray();

        // Assert: The final JSON should be a valid array with the successfully written value.
        JsonArray expected = new JsonArray();
        expected.add(12);
        assertThat(((JsonTreeWriter) writer).get()).isEqualTo(expected);
    }
}