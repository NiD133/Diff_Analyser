package com.google.gson.internal.bind;

import com.google.gson.JsonArray;
import org.junit.Test;

import java.util.ConcurrentModificationException;

/**
 * This test class focuses on the behavior of JsonTreeReader.
 * The original name, JsonTreeReader_ESTestTest15, suggests auto-generation.
 * A more conventional name would be JsonTreeReaderTest.
 */
public class JsonTreeReader_ESTestTest15 extends JsonTreeReader_ESTest_scaffolding {

    /**
     * Verifies that modifying the underlying JsonArray while it is being read
     * causes a ConcurrentModificationException.
     *
     * The JsonTreeReader uses an iterator internally to traverse the elements of a JsonArray.
     * Modifying the array after the iterator has been created but before traversal is complete
     * should fail-fast, as per the standard Java collections contract.
     */
    @Test(expected = ConcurrentModificationException.class)
    public void modifyingArrayWhileReadingThrowsConcurrentModificationException() {
        // Arrange: Create a JsonTreeReader for an empty array and begin reading it.
        JsonArray jsonArray = new JsonArray();
        JsonTreeReader reader = new JsonTreeReader(jsonArray);

        // This call obtains an internal iterator for the array.
        reader.beginArray();

        // Act: Modify the underlying array *after* the reader has started processing it.
        jsonArray.add("new element");

        // Assert: The next operation on the reader, which uses the now-invalidated
        // iterator, is expected to throw a ConcurrentModificationException.
        reader.peek();
    }
}