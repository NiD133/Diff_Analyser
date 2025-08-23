package com.google.gson.internal.bind;

import com.google.gson.JsonArray;
import org.junit.Test;
import java.io.IOException;
import static org.junit.Assert.assertTrue;

/**
 * This class contains tests for JsonTreeReader.
 * The original test class name and inheritance are kept to match the provided context.
 */
public class JsonTreeReader_ESTestTest92 extends JsonTreeReader_ESTest_scaffolding {

    /**
     * Tests that hasNext() returns true for a JsonTreeReader initialized
     * with an empty JsonArray.
     * <p>
     * This is the expected behavior because the reader must first consume
     * the BEGIN_ARRAY token before it can determine that the array is empty.
     */
    @Test(timeout = 4000)
    public void hasNext_onEmptyArray_returnsTrueInitially() throws IOException {
        // Arrange: Create a reader for an empty JSON array.
        JsonArray emptyJsonArray = new JsonArray();
        JsonTreeReader jsonTreeReader = new JsonTreeReader(emptyJsonArray);

        // Act: Check if there are more tokens to be read.
        boolean result = jsonTreeReader.hasNext();

        // Assert: hasNext() should be true because the BEGIN_ARRAY token is available.
        assertTrue("Expected hasNext() to be true for an empty array before reading any tokens", result);
    }
}