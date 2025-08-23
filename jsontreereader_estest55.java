package com.google.gson.internal.bind;

import com.google.gson.JsonArray;
import com.google.gson.stream.JsonToken;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link JsonTreeReader}.
 */
public class JsonTreeReaderTest {

    /**
     * Verifies that calling skipValue() on a reader for an empty JSON array
     * consumes the array and advances the reader to the end of the document.
     */
    @Test
    public void skipValueOnEmptyArray_advancesToEndOfDocument() throws Exception {
        // Arrange: Create a JsonTreeReader for an empty JSON array.
        JsonArray emptyArray = new JsonArray();
        JsonTreeReader reader = new JsonTreeReader(emptyArray);

        // Act: Skip the entire root value (the empty array).
        reader.skipValue();
        JsonToken nextToken = reader.peek();

        // Assert: The reader should now be at the end of the document.
        assertEquals("After skipping the root element, the reader should be at the end of the document.",
                JsonToken.END_DOCUMENT, nextToken);
    }
}