package com.google.gson.internal.bind;

import com.google.gson.JsonArray;
import org.junit.Test;

import java.io.IOException;
import java.util.ConcurrentModificationException;

/**
 * Test for {@link JsonTreeReader} to ensure it fails fast when the underlying
 * JSON structure is modified during iteration.
 */
public class JsonTreeReaderTest {

    /**
     * Verifies that modifying a JsonArray while a JsonTreeReader is iterating over it
     * results in a ConcurrentModificationException. This is the expected behavior for
     * standard Java iterators.
     */
    @Test(expected = ConcurrentModificationException.class)
    public void readingArrayAfterConcurrentModificationThrowsException() throws IOException {
        // Arrange: Create a reader for an array and begin reading it.
        // This implicitly creates an iterator over the array's elements.
        JsonArray jsonArray = new JsonArray();
        JsonTreeReader reader = new JsonTreeReader(jsonArray);
        reader.beginArray();

        // Act: Modify the underlying array *after* the reader's iterator has been created.
        jsonArray.add("new element");

        // Assert: The next operation on the reader, which uses the now-invalidated
        // iterator, should fail. The @Test(expected=...) annotation asserts that a
        // ConcurrentModificationException is thrown.
        reader.promoteNameToValue();
    }
}