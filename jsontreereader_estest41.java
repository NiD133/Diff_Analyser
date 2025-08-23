package com.google.gson.internal.bind;

import com.google.gson.JsonArray;
import java.io.IOException;
import java.util.ConcurrentModificationException;
import org.junit.Test;

/**
 * Tests for {@link JsonTreeReader} focusing on its behavior when the underlying JSON structure
 * is modified during iteration.
 */
public class JsonTreeReaderTest {

    /**
     * Verifies that modifying a JsonArray while a JsonTreeReader is iterating over it
     * results in a ConcurrentModificationException. This is the expected behavior for
     * Java collections and their iterators.
     */
    @Test(expected = ConcurrentModificationException.class)
    public void modifyingArrayWhileReadingThrowsConcurrentModificationException() throws IOException {
        // Arrange: Create a reader for an empty JSON array.
        JsonArray jsonArray = new JsonArray();
        JsonTreeReader reader = new JsonTreeReader(jsonArray);

        // Start reading the array. This action creates an internal iterator over the array's elements.
        reader.beginArray();

        // Act: Modify the underlying JsonArray *after* the reader has started processing it.
        // This invalidates the internal iterator.
        jsonArray.add("a new element");

        // Assert: The next attempt to read from the array will fail.
        // The hasNext() call uses the invalid iterator, which is expected to throw
        // a ConcurrentModificationException. The @Test(expected=...) annotation handles the assertion.
        reader.hasNext();
    }
}