package com.google.gson.internal.bind;

import com.google.gson.JsonArray;
import org.junit.Test;

import java.io.IOException;
import java.util.ConcurrentModificationException;

/**
 * This test class contains tests for the JsonTreeReader.
 * Note: The original class name "JsonTreeReader_ESTestTest39" suggests it was
 * auto-generated. A more conventional name would be "JsonTreeReaderTest".
 */
public class JsonTreeReader_ESTestTest39 {

    /**
     * Verifies that modifying the underlying JsonArray while a JsonTreeReader is
     * actively reading it results in a ConcurrentModificationException. This aligns
     * with the fail-fast behavior of iterators in the Java Collections Framework.
     */
    @Test(expected = ConcurrentModificationException.class)
    public void modifyingArrayDuringReadThrowsConcurrentModificationException() throws IOException {
        // Arrange: Create a reader for an empty JSON array.
        JsonArray jsonArray = new JsonArray();
        JsonTreeReader reader = new JsonTreeReader(jsonArray);

        // Begin reading the array. This action obtains an internal iterator for the array's elements.
        reader.beginArray();

        // Act: Directly modify the underlying JsonArray after the reader has started iterating.
        // This modification invalidates the reader's internal iterator.
        jsonArray.add("a new element");

        // Assert: The next read operation on the reader should detect the concurrent
        // modification and throw the expected exception.
        reader.hasNext();
    }
}