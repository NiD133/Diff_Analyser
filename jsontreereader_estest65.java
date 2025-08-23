package com.google.gson.internal.bind;

import com.google.gson.JsonArray;
import com.google.gson.stream.JsonToken;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

/**
 * Contains tests for the {@link JsonTreeReader} class, focusing on specific behaviors.
 */
public class JsonTreeReaderTest {

    /**
     * Verifies that calling skipValue() on an element within a JsonArray
     * correctly advances the reader's position to the next token.
     */
    @Test
    public void skipValueSkipsArrayElement() throws IOException {
        // Arrange: Create a JsonTreeReader for a JSON array with one element: [true]
        JsonArray jsonArray = new JsonArray();
        jsonArray.add(true);
        JsonTreeReader reader = new JsonTreeReader(jsonArray);

        // Position the reader inside the array, before the 'true' element
        reader.beginArray();

        // Act: Skip the value of the current element
        reader.skipValue();

        // Assert: The reader should now be positioned at the end of the array.
        // This confirms that the 'true' value was successfully skipped.
        assertEquals("After skipping the element, the next token should be END_ARRAY",
                     JsonToken.END_ARRAY, reader.peek());
    }
}