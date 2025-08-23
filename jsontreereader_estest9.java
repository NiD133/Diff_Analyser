package com.google.gson.internal.bind;

import com.google.gson.JsonObject;
import org.junit.Test;

import java.io.IOException;
import java.util.ConcurrentModificationException;

import static org.junit.Assert.assertThrows;

/**
 * Tests for {@link JsonTreeReader}.
 * This class focuses on specific behaviors, such as handling concurrent modifications.
 */
public class JsonTreeReaderTest {

    @Test
    public void readingFromObjectAfterConcurrentModificationShouldThrowException() throws IOException {
        // Arrange: Create a JsonObject and a JsonTreeReader, and begin reading the object.
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("key1", "value1");
        JsonTreeReader reader = new JsonTreeReader(jsonObject);
        
        // Position the reader to start iterating over the object's properties.
        // This creates an internal iterator for the JsonObject.
        reader.beginObject();

        // Act: Modify the underlying JsonObject while the reader is iterating over it.
        // This action invalidates the reader's internal iterator.
        jsonObject.addProperty("key2", "value2");

        // Assert: Any subsequent read operation on the reader should fail.
        // We use skipValue() as a representative read operation.
        assertThrows(ConcurrentModificationException.class, reader::skipValue);
    }
}