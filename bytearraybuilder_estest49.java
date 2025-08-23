package com.fasterxml.jackson.core.util;

import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Unit tests for the {@link ByteArrayBuilder} class, focusing on its core functionality.
 */
public class ByteArrayBuilderTest {

    /**
     * Verifies that calling {@code toByteArray()} on a newly instantiated,
     * empty builder returns an empty byte array.
     */
    @Test
    public void toByteArray_onNewBuilder_returnsEmptyArray() {
        // Arrange: Create a new, empty ByteArrayBuilder.
        ByteArrayBuilder builder = new ByteArrayBuilder();

        // Act: Retrieve the contents as a byte array.
        byte[] result = builder.toByteArray();

        // Assert: The result should be a non-null, empty byte array.
        assertNotNull("The result of toByteArray() should never be null.", result);
        assertArrayEquals("A new builder should produce an empty byte array.",
                          new byte[]{}, result);
    }
}