package com.google.gson.internal.bind;

import com.google.gson.JsonObject;
import org.junit.Test;
import java.io.IOException;
import static org.junit.Assert.assertFalse;

/**
 * Test for {@link JsonTreeReader}.
 * This class contains an improved version of an auto-generated test case.
 */
public class JsonTreeReaderUnderstandabilityTest {

    /**
     * Verifies that hasNext() returns false when called on an empty JSON object
     * after beginObject() has been invoked.
     */
    @Test
    public void hasNext_shouldReturnFalseForEmptyObject() throws IOException {
        // Arrange: Create a reader for an empty JSON object.
        JsonObject emptyObject = new JsonObject(); // Represents {}
        JsonTreeReader reader = new JsonTreeReader(emptyObject);

        // Position the reader inside the object, as if consuming the opening '{'.
        reader.beginObject();

        // Act & Assert: Verify that the reader correctly reports no further elements.
        assertFalse("An empty JSON object should not have a next element.", reader.hasNext());
    }
}