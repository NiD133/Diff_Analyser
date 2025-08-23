package com.google.gson.internal.bind;

import com.google.gson.JsonArray;
import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * This class contains tests for the {@link JsonTreeReader}.
 * The original test was auto-generated and has been refactored for clarity.
 */
public class JsonTreeReader_ESTestTest82 extends JsonTreeReader_ESTest_scaffolding {

    /**
     * Verifies that {@link JsonTreeReader#nextBoolean()} correctly reads a 'true'
     * boolean value from a JSON array.
     */
    @Test
    public void nextBoolean_whenArrayContainsTrue_returnsTrue() throws Exception {
        // Arrange: Create a JsonTreeReader for a JSON array: [true]
        JsonArray jsonArray = new JsonArray();
        jsonArray.add(true);
        JsonTreeReader jsonTreeReader = new JsonTreeReader(jsonArray);

        // Act: Navigate into the array and read the boolean value.
        jsonTreeReader.beginArray();
        boolean actualValue = jsonTreeReader.nextBoolean();

        // Assert: The read value should be true.
        assertTrue(actualValue);
    }
}