package com.google.gson.internal.bind;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.junit.Test;

import java.io.IOException;
import java.util.ConcurrentModificationException;

import static org.junit.Assert.fail;

/**
 * Contains tests for {@link JsonTreeReader}, focusing on its behavior when the
 * underlying JSON structure is modified during iteration.
 */
public class JsonTreeReaderImprovedTest {

    /**
     * Verifies that modifying a JsonObject while a JsonTreeReader is iterating over it
     * correctly throws a ConcurrentModificationException.
     * <p>
     * This test ensures the reader fails fast, which is the expected behavior for
     * iterators when their underlying collection is modified unexpectedly.
     */
    @Test
    public void promoteNameToValue_whenObjectIsModifiedDuringIteration_throwsConcurrentModificationException() {
        // Arrange: Create a JsonObject and a reader that has started iterating over it.
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("initialProperty", "value");
        JsonTreeReader reader = new JsonTreeReader(jsonObject);

        try {
            // This call prepares the reader and its internal iterator to read the object's members.
            reader.beginObject();

            // Act: Modify the underlying JsonObject after the iterator has been created.
            // This action invalidates the reader's internal state.
            jsonObject.add("anotherProperty", (JsonElement) null);

            // Assert: The next operation that uses the iterator should fail.
            reader.promoteNameToValue();
            fail("Expected a ConcurrentModificationException to be thrown, but it was not.");
        } catch (ConcurrentModificationException expected) {
            // Success: This is the expected behavior.
        } catch (IOException e) {
            // The method signature includes IOException, but it's not expected in this test.
            fail("Test threw an unexpected IOException: " + e.getMessage());
        }
    }
}