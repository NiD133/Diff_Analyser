package org.apache.commons.io;

import static org.junit.Assert.assertFalse;

import org.junit.Test;

/**
 * Unit tests for {@link ByteOrderMark}.
 */
public class ByteOrderMarkTest {

    /**
     * Tests that the matches() method correctly returns false for a null input array.
     * This ensures the method is robust and handles null arguments gracefully without
     * throwing a NullPointerException.
     */
    @Test
    public void matchesShouldReturnFalseForNullInput() {
        // Arrange: Get a ByteOrderMark instance to test against.
        // Any predefined BOM constant will work for this test case.
        final ByteOrderMark bom = ByteOrderMark.UTF_32BE;

        // Act: Call the matches method with a null array.
        final boolean result = bom.matches(null);

        // Assert: Verify that the result is false.
        assertFalse("matches(null) should return false.", result);
    }
}