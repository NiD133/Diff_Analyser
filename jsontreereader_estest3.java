package com.google.gson.internal.bind;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import org.junit.Test;

import static org.junit.Assert.assertSame;

/**
 * Unit tests for {@link JsonTreeReader}.
 */
public class JsonTreeReaderTest {

    @Test
    public void nextJsonElement_whenReaderIsCreatedWithArray_returnsSameArrayInstance() throws Exception {
        // Arrange
        JsonArray rootArray = new JsonArray();
        JsonTreeReader reader = new JsonTreeReader(rootArray);

        // Act
        JsonElement resultElement = reader.nextJsonElement();

        // Assert
        // The reader should return the exact same JsonArray instance it was initialized with.
        assertSame(rootArray, resultElement);
    }
}