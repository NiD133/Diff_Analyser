package com.google.gson;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

public class JsonArrayTest {

    @Test
    public void getAsShort_onArrayWithSingleJsonNull_throwsUnsupportedOperationException() {
        // Arrange: Create a JsonArray containing a single JsonNull element.
        // The add(Number) method converts a null input into a JsonNull instance.
        JsonArray jsonArray = new JsonArray();
        jsonArray.add((Number) null);

        // Act & Assert: Verify that calling getAsShort() throws the correct exception.
        try {
            // The getAsShort() method on a single-element array delegates to the element's
            // own getAsShort() method. For a JsonNull element, this is an unsupported operation.
            jsonArray.getAsShort();
            fail("Expected an UnsupportedOperationException to be thrown.");
        } catch (UnsupportedOperationException expected) {
            // The exception message from JsonElement for this operation is the element's class name.
            assertEquals("JsonNull", expected.getMessage());
        }
    }
}