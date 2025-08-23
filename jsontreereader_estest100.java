package com.google.gson.internal.bind;

import static org.junit.Assert.assertThrows;

import com.google.gson.JsonElement;
import org.junit.Test;

/**
 * Unit tests for {@link JsonTreeReader}.
 */
public class JsonTreeReaderTest {

    @Test
    public void beginArray_shouldThrowNullPointerException_whenInitializedWithNull() {
        // Arrange: Create a JsonTreeReader with a null JsonElement.
        // This simulates a scenario where the reader has no content to parse.
        JsonTreeReader reader = new JsonTreeReader((JsonElement) null);

        // Act & Assert: Verify that attempting to start reading an array
        // throws a NullPointerException because there is no underlying element to inspect.
        assertThrows(NullPointerException.class, reader::beginArray);
    }
}