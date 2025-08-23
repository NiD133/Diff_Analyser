package com.google.gson.internal.bind;

import com.google.gson.JsonArray;
import org.junit.Test;

import java.io.IOException;
import java.util.ConcurrentModificationException;

// The test class name and inheritance are kept from the original to maintain context.
public class JsonTreeReader_ESTestTest31 extends JsonTreeReader_ESTest_scaffolding {

    /**
     * Verifies that modifying the underlying JsonArray while it is being read
     * causes a ConcurrentModificationException.
     *
     * The JsonTreeReader uses an iterator internally. Modifying the collection
     * after the iterator has been created invalidates it, and subsequent
     * operations on the reader should fail fast.
     */
    @Test(expected = ConcurrentModificationException.class)
    public void modifyingArrayWhileReadingThrowsConcurrentModificationException() throws IOException {
        // Arrange: Create a JsonTreeReader for an empty JsonArray and begin reading.
        JsonArray jsonArray = new JsonArray();
        JsonTreeReader reader = new JsonTreeReader(jsonArray);

        // This call creates an internal iterator over the array's elements.
        reader.beginArray();

        // Act: Modify the underlying JsonArray *after* the reader has started iterating.
        jsonArray.add(false);

        // Assert: Attempting to read the next element is expected to throw a
        // ConcurrentModificationException because the iterator has been invalidated.
        reader.nextBoolean();
    }
}