package com.google.gson.internal.bind;

import com.google.gson.JsonPrimitive;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link JsonTreeReader} class.
 */
public class JsonTreeReaderTest {

    @Test
    public void nextString_whenReadingEmptyStringPrimitive_returnsEmptyString() throws IOException {
        // Arrange: Create a JsonTreeReader for a JSON primitive containing an empty string.
        JsonPrimitive emptyStringPrimitive = new JsonPrimitive("");
        JsonTreeReader reader = new JsonTreeReader(emptyStringPrimitive);

        // Act: Read the string value from the reader.
        String result = reader.nextString();

        // Assert: Verify that the returned string is empty.
        assertEquals("", result);
    }
}