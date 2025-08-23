package com.google.gson.internal.bind;

import com.google.gson.JsonArray;
import org.junit.Test;

import java.util.ConcurrentModificationException;

/**
 * Test suite for {@link JsonTreeReader}.
 */
public class JsonTreeReaderTest {

    /**
     * Verifies that modifying the underlying JsonArray while it is being read
     * results in a ConcurrentModificationException. This is the expected "fail-fast"
     * behavior of iterators.
     */
    @Test(expected = ConcurrentModificationException.class)
    public void readingAnArrayThatIsModifiedConcurrentlyThrowsException() throws Exception {
        // Arrange: Create a reader for an empty JSON array and begin reading it.
        // This implicitly creates an iterator over the array's elements.
        JsonArray jsonArray = new JsonArray();
        JsonTreeReader reader = new JsonTreeReader(jsonArray);
        reader.beginArray();

        // Act: Modify the underlying array *after* the reader has started processing it.
        jsonArray.add("a new element");

        // Assert: The next operation on the reader, which relies on the now-invalidated
        // iterator, is expected to throw a ConcurrentModificationException.
        reader.endArray();
    }
}