package com.google.gson.internal.bind;

import static org.junit.Assert.assertEquals;

import com.google.gson.JsonPrimitive;
import java.io.IOException;
import org.junit.Test;

/**
 * Unit tests for {@link JsonTreeReader}.
 */
public class JsonTreeReaderTest {

    /**
     * Tests that {@link JsonTreeReader#nextInt()} correctly reads an integer
     * value from a {@link JsonPrimitive}.
     */
    @Test
    public void nextInt_whenReadingNumericPrimitive_returnsIntegerValue() throws IOException {
        // Arrange
        int expectedValue = -1977;
        JsonPrimitive numberPrimitive = new JsonPrimitive(expectedValue);
        JsonTreeReader jsonTreeReader = new JsonTreeReader(numberPrimitive);

        // Act
        int actualValue = jsonTreeReader.nextInt();

        // Assert
        assertEquals(expectedValue, actualValue);
    }
}