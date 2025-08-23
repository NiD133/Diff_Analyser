package com.google.gson;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Unit tests for the {@link JsonArray} class.
 */
public class JsonArrayTest {

    /**
     * Tests that calling getAsFloat() on a JsonArray containing a single JsonNull element
     * throws an UnsupportedOperationException.
     *
     * The getAs...() methods on JsonArray are convenience wrappers that operate on the
     * array's single element. The test verifies that the exception from the underlying
     * JsonNull element is correctly propagated.
     */
    @Test
    public void getAsFloat_shouldThrowUnsupportedOperationException_whenArrayContainsSingleJsonNull() {
        // Arrange: Create a JsonArray and add a null value.
        // Per JsonArray's contract, adding a null reference results in a JsonNull element.
        JsonArray jsonArray = new JsonArray();
        jsonArray.add((Number) null);

        // Act & Assert: Verify that calling getAsFloat() throws the expected exception.
        try {
            jsonArray.getAsFloat();
            fail("Expected an UnsupportedOperationException to be thrown, but it was not.");
        } catch (UnsupportedOperationException e) {
            // The exception is thrown by JsonElement.getAsFloat(), and its message
            // is the simple name of the subclass, which is "JsonNull" in this case.
            assertEquals("JsonNull", e.getMessage());
        }
    }
}