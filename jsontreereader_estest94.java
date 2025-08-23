package com.google.gson.internal.bind;

import com.google.gson.JsonObject;
import com.google.gson.stream.JsonToken;
import org.junit.Test;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class JsonTreeReaderTest {

    /**
     * Verifies that calling skipValue() on an empty JSON object consumes the entire
     * object, advancing the reader to the end of the document.
     */
    @Test
    public void skipValueOnEmptyObjectConsumesTheObject() throws IOException {
        // Arrange
        JsonObject emptyObject = new JsonObject();
        JsonTreeReader reader = new JsonTreeReader(emptyObject);

        // Pre-condition check: The reader should be at the start of the object.
        assertEquals(JsonToken.BEGIN_OBJECT, reader.peek());

        // Act
        reader.skipValue();

        // Assert
        // After skipping the object, the reader should be at the end of the document.
        assertEquals(JsonToken.END_DOCUMENT, reader.peek());
        
        // The reader's default configuration (non-lenient) should not be affected.
        assertFalse(reader.isLenient());
    }
}