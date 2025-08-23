package com.google.gson.internal.bind;

import com.google.gson.JsonPrimitive;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

/**
 * Test suite for {@link JsonTreeReader}.
 * This class contains the refactored test case.
 */
public class JsonTreeReader_ESTestTest76 extends JsonTreeReader_ESTest_scaffolding {

    /**
     * Verifies that {@link JsonTreeReader#nextLong()} correctly reads and returns
     * the long value from a {@link JsonPrimitive} containing a number.
     */
    @Test
    public void nextLong_whenReadingJsonPrimitiveNumber_returnsLongValue() throws IOException {
        // Arrange
        long expectedValue = -1977L;
        JsonPrimitive jsonNumber = new JsonPrimitive(expectedValue);
        JsonTreeReader reader = new JsonTreeReader(jsonNumber);

        // Act
        long actualValue = reader.nextLong();

        // Assert
        assertEquals(expectedValue, actualValue);
    }
}