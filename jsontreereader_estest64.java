package com.google.gson.internal.bind;

import com.google.gson.JsonArray;
import org.junit.Test;
import java.io.IOException;
import static org.junit.Assert.assertEquals;

/**
 * Contains tests for the {@link JsonTreeReader} class.
 */
public class JsonTreeReaderTest {

    /**
     * Verifies that getPreviousPath() returns the JSON path of the first element's index
     * immediately after beginArray() is called.
     */
    @Test
    public void getPreviousPath_afterBeginArray_returnsPathOfFirstElementIndex() throws IOException {
        // Arrange
        JsonArray emptyJsonArray = new JsonArray();
        JsonTreeReader reader = new JsonTreeReader(emptyJsonArray);

        // Consume the BEGIN_ARRAY token to enter the array context.
        // The reader is now positioned just before the first element.
        reader.beginArray();

        // Act
        String path = reader.getPreviousPath();

        // Assert
        String expectedPath = "$[0]";
        assertEquals("After calling beginArray(), getPreviousPath() should return the path to the first element's index.",
                expectedPath, path);
    }
}