package com.google.gson.internal.bind;

import com.google.gson.JsonArray;
import org.junit.Test;

import java.io.IOException;
import java.util.ConcurrentModificationException;

/**
 * Test suite for {@link JsonTreeReader}.
 */
public class JsonTreeReaderTest {

    /**
     * Verifies that modifying the underlying JsonArray after starting to read it
     * causes a ConcurrentModificationException on subsequent read operations.
     * This is the expected behavior for iterators over standard Java collections,
     * which JsonTreeReader uses internally.
     */
    @Test(expected = ConcurrentModificationException.class)
    public void readingFromAnArrayModifiedAfterIterationBeganThrowsException() throws IOException {
        // Arrange: Create a reader for an empty JSON array and begin reading it.
        JsonArray jsonArray = new JsonArray();
        JsonTreeReader reader = new JsonTreeReader(jsonArray);
        
        // This call creates an internal iterator for the array's elements.
        reader.beginArray();

        // Act: Modify the underlying array *after* the reader's iterator has been created.
        jsonArray.add("new element");

        // Assert: Attempting to continue reading from the array is expected to throw
        // a ConcurrentModificationException because the underlying data structure changed.
        // The call to nextName() triggers the check.
        reader.nextName();
    }
}