package com.google.gson.internal.bind;

import com.google.gson.JsonNull;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link JsonTreeReader}.
 */
public class JsonTreeReaderTest {

    /**
     * Verifies that the toString() method correctly reports the reader's position
     * at the root of the JSON structure.
     */
    @Test
    public void toString_whenAtRoot_returnsPathAsString() {
        // Arrange
        JsonNull rootElement = JsonNull.INSTANCE;
        JsonTreeReader reader = new JsonTreeReader(rootElement);
        String expectedString = "JsonTreeReader at path $";

        // Act
        String actualString = reader.toString();

        // Assert
        assertEquals(expectedString, actualString);
    }
}