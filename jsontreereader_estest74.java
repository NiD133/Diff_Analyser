package com.google.gson.internal.bind;

import com.google.gson.JsonArray;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

/**
 * This class contains tests for {@link JsonTreeReader}.
 * The original test class name `JsonTreeReader_ESTestTest74` is preserved
 * to maintain context from the original auto-generated suite.
 */
public class JsonTreeReader_ESTestTest74 extends JsonTreeReader_ESTest_scaffolding {

    /**
     * Tests that {@link JsonTreeReader#nextLong()} can correctly read a number
     * that was added to a JsonArray as a Byte, and return it as a long.
     * This verifies the type coercion from a smaller numeric type.
     */
    @Test
    public void nextLong_whenArrayContainsByte_returnsLongValue() throws IOException {
        // Arrange: Create a JsonArray containing a single byte value.
        JsonArray jsonArray = new JsonArray();
        jsonArray.add((byte) 3);
        JsonTreeReader reader = new JsonTreeReader(jsonArray);

        // Act: Navigate into the array and read the number as a long.
        reader.beginArray();
        long actualValue = reader.nextLong();

        // Assert: Verify the value was read correctly.
        long expectedValue = 3L;
        assertEquals(expectedValue, actualValue);
    }
}