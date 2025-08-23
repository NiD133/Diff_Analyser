package com.google.gson.internal.bind;

import com.google.gson.JsonArray;
import org.junit.Test;

import java.io.IOException;
import java.util.ConcurrentModificationException;

import static org.junit.Assert.fail;

// The class name and inheritance are kept from the original test structure.
public class JsonTreeReader_ESTestTest33 extends JsonTreeReader_ESTest_scaffolding {

    /**
     * Verifies that modifying a JsonArray while it is being read
     * causes a ConcurrentModificationException. This confirms the reader's
     * iterator is "fail-fast", which is the expected behavior.
     */
    @Test
    public void hasNext_whenArrayIsModifiedDuringIteration_throwsConcurrentModificationException() throws IOException {
        // Arrange: Create a reader for an array and start consuming it.
        // This initializes the internal iterator for the array's elements.
        JsonArray jsonArray = new JsonArray();
        JsonTreeReader reader = new JsonTreeReader(jsonArray);
        reader.beginArray();

        // Act: Modify the underlying JsonArray after the reader has started iterating.
        // This violates the iterator's contract.
        jsonArray.add(true);

        // Assert: The next attempt to use the reader should fail.
        try {
            reader.hasNext();
            fail("Expected ConcurrentModificationException to be thrown due to structural modification.");
        } catch (ConcurrentModificationException expected) {
            // This is the correct, expected behavior. The test passes.
        }
    }
}