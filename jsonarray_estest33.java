package com.google.gson;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

/**
 * Tests for {@link JsonArray} focusing on its convenience getter methods.
 */
public class JsonArrayGetterTest {

    /**
     * Verifies that calling getAsInt() on a JsonArray containing a single JsonNull element
     * throws an UnsupportedOperationException. This is the expected behavior because a JSON
     * null value cannot be converted to a primitive integer.
     */
    @Test
    public void getAsInt_whenArrayContainsSingleJsonNull_throwsUnsupportedOperationException() {
        // Arrange: Create a JsonArray and add a null value.
        // The JsonArray class converts null inputs into JsonNull elements internally.
        JsonArray jsonArray = new JsonArray();
        jsonArray.add((Number) null);

        // Act & Assert: Verify that calling getAsInt() throws the expected exception.
        UnsupportedOperationException exception = assertThrows(
            UnsupportedOperationException.class,
            () -> jsonArray.getAsInt()
        );

        // The exception originates from JsonNull and its message is expected to be "JsonNull".
        assertEquals("JsonNull", exception.getMessage());
    }
}