package com.google.gson.internal.bind;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import org.junit.Test;

import java.util.ConcurrentModificationException;

/**
 * Contains tests for the {@link JsonTreeReader} class.
 */
public class JsonTreeReaderTest {

    /**
     * Verifies that modifying the underlying JsonArray after starting to read it
     * causes a ConcurrentModificationException when trying to advance the reader.
     * This confirms the fail-fast behavior of the underlying iterator.
     */
    @Test(expected = ConcurrentModificationException.class)
    public void skipValue_whenArrayIsModifiedAfterReadingBegins_throwsConcurrentModificationException() throws Exception {
        // Arrange: Create a reader for an empty JSON array.
        JsonArray jsonArray = new JsonArray();
        JsonTreeReader reader = new JsonTreeReader(jsonArray);

        // Start reading the array, which creates an internal iterator over its elements.
        reader.beginArray();

        // Act: Modify the underlying array *after* the iterator has been created.
        // This invalidates the iterator's state.
        jsonArray.add((JsonElement) null);

        // Assert: Attempting to advance the reader by skipping a value should now fail.
        // The @Test(expected=...) annotation asserts that a ConcurrentModificationException is thrown.
        reader.skipValue();
    }
}