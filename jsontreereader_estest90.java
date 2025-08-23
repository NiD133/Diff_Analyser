package com.google.gson.internal.bind;

import com.google.gson.JsonArray;
import org.junit.Test;
import java.io.IOException;
import java.util.ConcurrentModificationException;

/**
 * This class contains tests for {@link JsonTreeReader}.
 * This particular test focuses on concurrent modification scenarios.
 */
public class JsonTreeReader_ESTestTest90 extends JsonTreeReader_ESTest_scaffolding {

    /**
     * Verifies that modifying the underlying JsonArray while it is being read
     * by a JsonTreeReader results in a ConcurrentModificationException. This is the
     * expected behavior for iterators in the Java Collections Framework.
     */
    @Test(expected = ConcurrentModificationException.class)
    public void readingAfterModifyingArrayThrowsConcurrentModificationException() throws IOException {
        // Arrange: Create a reader for an empty JSON array and begin reading it.
        // This action creates an internal iterator over the array's elements.
        JsonArray jsonArray = new JsonArray();
        JsonTreeReader reader = new JsonTreeReader(jsonArray);
        reader.beginArray();

        // Act: Modify the underlying array directly. This invalidates the reader's internal iterator.
        jsonArray.add("new element");

        // Assert: The next attempt to use the reader (e.g., by checking hasNext())
        // is expected to throw a ConcurrentModificationException.
        reader.hasNext();
    }
}