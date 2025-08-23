package com.fasterxml.jackson.core.util;

import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link ByteArrayBuilder} class.
 */
public class ByteArrayBuilderTest {

    /**
     * Tests that calling {@code completeAndCoalesce(0)} on a newly created,
     * empty builder returns an empty byte array. This simulates the scenario
     * where no content has been added.
     */
    @Test
    public void completeAndCoalesce_onEmptyBuilder_shouldReturnEmptyArray() {
        // Arrange: Create a new, empty ByteArrayBuilder.
        ByteArrayBuilder builder = new ByteArrayBuilder();

        // Act: Finalize the builder, indicating zero bytes were written to the current buffer.
        byte[] result = builder.completeAndCoalesce(0);

        // Assert: The resulting byte array should be empty.
        // We can assert this by checking the length or by comparing to a known empty array.
        assertEquals(0, result.length);
        // A more explicit check is to compare against an empty byte array instance.
        assertArrayEquals(new byte[0], result);
    }
}