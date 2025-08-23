package com.google.gson.internal.bind;

import com.google.gson.JsonArray;
import org.junit.Test;
import java.io.IOException;
import static org.junit.Assert.assertFalse;

/**
 * Unit tests for {@link JsonTreeReader}.
 */
public class JsonTreeReaderTest {

    @Test
    public void hasNext_whenReadingAnEmptyArray_returnsFalse() throws IOException {
        // Arrange: Create a reader for an empty JSON array.
        JsonArray emptyArray = new JsonArray();
        JsonTreeReader reader = new JsonTreeReader(emptyArray);

        // Act: Start reading the array.
        reader.beginArray();

        // Assert: Verify that the reader reports it has no more elements.
        assertFalse("hasNext() should return false for an empty array", reader.hasNext());
    }
}