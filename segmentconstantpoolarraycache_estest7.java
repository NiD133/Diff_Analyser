package org.apache.commons.compress.harmony.unpack200;

import org.junit.Test;

/**
 * Unit tests for the {@link SegmentConstantPoolArrayCache} class.
 */
public class SegmentConstantPoolArrayCacheTest {

    /**
     * Verifies that calling cacheArray() with a null argument
     * correctly throws a NullPointerException.
     */
    @Test(expected = NullPointerException.class)
    public void cacheArrayShouldThrowNullPointerExceptionForNullInput() {
        // Arrange: Create an instance of the class under test.
        final SegmentConstantPoolArrayCache cache = new SegmentConstantPoolArrayCache();

        // Act: Call the method with a null input.
        // This action is expected to throw the exception.
        cache.cacheArray(null);

        // Assert: The test framework verifies that a NullPointerException was thrown.
        // If no exception is thrown, the test will fail.
    }
}