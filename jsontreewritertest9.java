package com.google.gson.internal.bind;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertThrows;

import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import org.junit.Test;

/**
 * Tests for {@link JsonTreeWriter}.
 */
public class JsonTreeWriterTest {

    @Test
    public void name_whenNameIsPending_throwsIllegalStateException() throws IOException {
        // Arrange
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginObject();
        writer.name("a"); // The first name is valid; a value is now expected.

        // Act & Assert
        // In a JSON object, a name must be followed by a value.
        // Attempting to write a second consecutive name is an error.
        IllegalStateException e = assertThrows(
            IllegalStateException.class,
            () -> writer.name("b")
        );

        assertThat(e).hasMessageThat().isEqualTo("Did not expect a name");
    }
}