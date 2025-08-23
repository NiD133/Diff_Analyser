package com.google.gson;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.util.Iterator;
import org.junit.Test;

/**
 * Unit tests for the {@link JsonArray} class.
 */
public class JsonArrayTest {

    @Test
    public void iterator_onEmptyArray_returnsNonNullAndEmptyIterator() {
        // Arrange: Create an empty JsonArray instance.
        JsonArray emptyArray = new JsonArray();

        // Act: Get the iterator from the empty array.
        Iterator<JsonElement> iterator = emptyArray.iterator();

        // Assert: Verify that the iterator is not null and has no elements.
        assertNotNull("The iterator for an empty array should never be null.", iterator);
        assertFalse("The iterator for an empty array should not have any elements.", iterator.hasNext());
    }
}