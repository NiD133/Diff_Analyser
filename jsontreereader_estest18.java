package com.google.gson.internal.bind;

import com.google.gson.JsonArray;
import org.junit.Test;

import java.io.IOException;
import java.util.ConcurrentModificationException;

/**
 * This test verifies the behavior of JsonTreeReader when the underlying
 * JsonElement is modified during iteration.
 */
public class JsonTreeReaderTest {

    /**
     * Tests that modifying a JsonArray while it is being read by a JsonTreeReader
     * causes a ConcurrentModificationException.
     */
    @Test(expected = ConcurrentModificationException.class)
    public void modifyingArrayDuringReadThrowsConcurrentModificationException() throws IOException {
        // Arrange: Create a JsonTreeReader for an empty JsonArray.
        JsonArray jsonArray = new JsonArray();
        JsonTreeReader jsonTreeReader = new JsonTreeReader(jsonArray);

        // Start reading the array, which implicitly creates an internal iterator.
        jsonTreeReader.beginArray();

        // Act: Modify the underlying JsonArray after iteration has started.
        jsonArray.add("new element");

        // Assert: The next attempt to read from the reader is expected to throw
        // a ConcurrentModificationException due to the modification.
        jsonTreeReader.nextNull();
    }
}