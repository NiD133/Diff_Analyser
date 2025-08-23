package com.google.gson;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class JsonArrayTest {

    @Test
    public void getAsString_whenArrayContainsSingleJsonNull_throwsUnsupportedOperationException() {
        // Arrange: Create a JsonArray and add a null value.
        // The JsonArray class converts any null input into a JsonNull element.
        JsonArray jsonArray = new JsonArray();
        jsonArray.add((Character) null);

        // Act & Assert: Verify that calling getAsString() throws the expected exception.
        // The getAsString() method on a JsonArray with one element delegates the call
        // to that single element. For a JsonNull element, this is an unsupported operation.
        try {
            jsonArray.getAsString();
            fail("Expected an UnsupportedOperationException to be thrown, but it was not.");
        } catch (UnsupportedOperationException expectedException) {
            // The default exception message from JsonElement.getAsString() is the class's simple name.
            assertEquals("JsonNull", expectedException.getMessage());
        }
    }
}