package com.google.gson;

import org.junit.Test;

/**
 * Test suite for the {@link JsonArray} class, focusing on its behavior as a single-element view.
 */
public class JsonArrayTest {

    /**
     * Tests that calling getAsBigDecimal() on a JsonArray containing a single JsonNull element
     * throws an UnsupportedOperationException.
     *
     * This behavior occurs because getAsBigDecimal() on a JsonArray delegates the call to its
     * single element. For a JsonNull element, this operation is not supported.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void getAsBigDecimal_whenArrayContainsSingleJsonNull_throwsUnsupportedOperationException() {
        // Arrange: Create a JsonArray and add a null Number.
        // According to the JsonArray documentation, adding a null value results in a
        // JsonNull element being added to the array.
        JsonArray jsonArray = new JsonArray();
        jsonArray.add((Number) null);

        // Act: Attempt to get the single element as a BigDecimal.
        // This should throw an exception because the operation is not supported on JsonNull.
        jsonArray.getAsBigDecimal();

        // Assert: The test passes if an UnsupportedOperationException is thrown,
        // which is handled by the @Test(expected=...) annotation.
    }
}