package com.google.gson.internal.bind;

import com.google.gson.JsonPrimitive;
import org.junit.Test;
import java.io.IOException;
import static org.junit.Assert.assertEquals;

/**
 * Test suite for {@link JsonTreeReader}.
 */
public class JsonTreeReaderTest {

    /**
     * Tests that {@link JsonTreeReader#nextLong()} correctly reads and returns
     * the long value from a {@link JsonPrimitive}.
     */
    @Test
    public void nextLong_whenReadingLongPrimitive_returnsCorrectValue() throws IOException {
        // Arrange
        long expectedValue = 0L;
        JsonPrimitive jsonPrimitive = new JsonPrimitive(expectedValue);
        JsonTreeReader reader = new JsonTreeReader(jsonPrimitive);

        // Act
        long actualValue = reader.nextLong();

        // Assert
        assertEquals("The reader should return the correct long value.", expectedValue, actualValue);
    }
}