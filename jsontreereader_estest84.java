package com.google.gson.internal.bind;

import com.google.gson.JsonPrimitive;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

/**
 * Test suite for {@link JsonTreeReader}.
 */
public class JsonTreeReaderTest {

    @Test
    public void nextString_onNumberPrimitive_returnsStringRepresentation() throws IOException {
        // Arrange: Create a JsonTreeReader with a JSON primitive representing a number.
        JsonPrimitive numberPrimitive = new JsonPrimitive(-1977L);
        JsonTreeReader jsonTreeReader = new JsonTreeReader(numberPrimitive);

        // Act: Read the value as a string.
        String result = jsonTreeReader.nextString();

        // Assert: The returned string should be the correct string representation of the number.
        assertEquals("-1977", result);
    }
}