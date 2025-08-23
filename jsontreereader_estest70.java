package com.google.gson.internal.bind;

import static org.junit.Assert.assertSame;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.io.IOException;
import org.junit.Test;

/**
 * Unit tests for {@link JsonTreeReader}.
 */
public class JsonTreeReaderTest {

    /**
     * Verifies that calling {@link JsonTreeReader#nextJsonElement()} on a reader
     * initialized with a JsonObject returns that same object instance.
     */
    @Test
    public void nextJsonElement_whenReadingJsonObject_returnsSameInstance() throws IOException {
        // Arrange: Create a JsonObject and a reader for it.
        JsonObject jsonObject = new JsonObject();
        JsonTreeReader reader = new JsonTreeReader(jsonObject);

        // Act: Read the next element from the reader.
        JsonElement resultElement = reader.nextJsonElement();

        // Assert: The returned element should be the exact same object instance.
        assertSame("The returned JsonElement should be the same instance as the input",
            jsonObject, resultElement);
    }
}