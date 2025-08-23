package com.google.gson.internal.bind;

import static org.junit.Assert.assertEquals;

import com.google.gson.JsonObject;
import org.junit.Test;

/**
 * Tests for {@link JsonTreeReader}.
 */
public class JsonTreeReaderTest {

    @Test
    public void newReaderHasDefaultNestingLimit() {
        // Arrange
        JsonObject emptyObject = new JsonObject();
        JsonTreeReader reader = new JsonTreeReader(emptyObject);

        // Act
        int nestingLimit = reader.getNestingLimit();

        // Assert
        // The default nesting limit (255) is inherited from the superclass JsonReader.
        assertEquals(255, nestingLimit);
    }
}