package com.google.gson.internal.bind;

import com.google.gson.JsonPrimitive;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertFalse;

/**
 * Tests for {@link JsonTreeReader}.
 */
public class JsonTreeReaderTest {

    @Test
    public void nextBoolean_whenReadingFalsePrimitive_returnsFalse() throws IOException {
        // Arrange
        JsonPrimitive falseJsonPrimitive = new JsonPrimitive(false);
        JsonTreeReader jsonTreeReader = new JsonTreeReader(falseJsonPrimitive);

        // Act
        boolean result = jsonTreeReader.nextBoolean();

        // Assert
        assertFalse("The reader should return false for a JSON false primitive.", result);
    }
}