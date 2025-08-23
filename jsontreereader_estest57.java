package com.google.gson.internal.bind;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonToken;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

// Note: The original test class name and structure are preserved as requested.
public class JsonTreeReader_ESTestTest57 extends JsonTreeReader_ESTest_scaffolding {

    /**
     * Tests that calling {@link JsonTreeReader#nextJsonElement()} when inside an array
     * correctly consumes and returns the element, advancing the reader's position.
     */
    @Test(timeout = 4000)
    public void nextJsonElement_whenInArray_consumesAndReturnsElement() throws IOException {
        // Arrange: Create a JsonTreeReader for a JSON array: [true]
        JsonArray jsonArray = new JsonArray();
        jsonArray.add(true);
        JsonTreeReader reader = new JsonTreeReader(jsonArray);

        // Position the reader inside the array
        reader.beginArray();

        // Act: Consume the next element from the array
        JsonElement consumedElement = reader.nextJsonElement();

        // Assert: Verify the correct element was returned and the reader advanced
        assertEquals(new JsonPrimitive(true), consumedElement);
        
        // Verify the reader is now positioned at the end of the array
        assertEquals(JsonToken.END_ARRAY, reader.peek());

        // Cleanup: Finish reading the array to ensure the stream is fully consumed
        reader.endArray();
    }
}