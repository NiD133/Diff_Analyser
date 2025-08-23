package com.google.gson.internal.bind;

import com.google.gson.JsonArray;
import org.junit.Test;

import java.io.IOException;
import java.util.ConcurrentModificationException;

/**
 * Test for {@link JsonTreeReader} focusing on concurrent modification scenarios.
 */
public class JsonTreeReaderTest {

    /**
     * Verifies that modifying the underlying JsonArray while the JsonTreeReader is
     * iterating over it results in a ConcurrentModificationException. This is the
     * expected behavior for iterators in the Java Collections Framework.
     */
    @Test(expected = ConcurrentModificationException.class)
    public void readingFromArrayAfterConcurrentModificationThrowsException() throws IOException {
        // Arrange: Create a JsonTreeReader for an empty JsonArray.
        JsonArray jsonArray = new JsonArray();
        JsonTreeReader jsonTreeReader = new JsonTreeReader(jsonArray);

        // Start iterating the array. This creates an internal iterator over the array's elements.
        jsonTreeReader.beginArray();

        // Act: Modify the underlying JsonArray *after* the reader has started iterating.
        // From the iterator's perspective, this is a concurrent modification.
        jsonArray.add(false);

        // Assert: The next attempt to read from the reader is expected to throw
        // a ConcurrentModificationException because the underlying list was changed.
        // The @Test(expected=...) annotation handles the assertion.
        jsonTreeReader.nextString();
    }
}