package com.google.gson.internal.bind;

import com.google.gson.JsonArray;
import java.io.IOException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Tests for {@link JsonTreeReader}.
 */
// The original class name "JsonTreeReader_ESTestTest87" was renamed for clarity.
public class JsonTreeReaderTest {

    @Test
    public void hasNext_whenReaderIsClosed_throwsIllegalStateException() throws IOException {
        // Arrange
        JsonArray emptyJsonArray = new JsonArray();
        JsonTreeReader jsonTreeReader = new JsonTreeReader(emptyJsonArray);
        jsonTreeReader.close();

        // Act & Assert
        try {
            jsonTreeReader.hasNext();
            fail("Expected an IllegalStateException because the reader is closed.");
        } catch (IllegalStateException expected) {
            assertEquals("JsonReader is closed", expected.getMessage());
        }
    }
}