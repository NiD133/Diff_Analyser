package org.apache.commons.compress.harmony.unpack200;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Unit tests for {@link SegmentConstantPoolArrayCache}.
 */
public class SegmentConstantPoolArrayCacheTest {

    /**
     * Verifies that attempting to cache the same array instance twice
     * results in an IllegalArgumentException. The cache should not
     * allow duplicate entries for the same array object.
     */
    @Test
    public void shouldThrowExceptionWhenCachingSameArrayTwice() {
        // Arrange: Create a cache and an array to be cached.
        final SegmentConstantPoolArrayCache cache = new SegmentConstantPoolArrayCache();
        final String[] arrayToCache = new String[8];

        // Pre-condition: Cache the array for the first time, which should succeed.
        cache.cacheArray(arrayToCache);

        // Act & Assert: Attempt to cache the same array again and expect an exception.
        try {
            cache.cacheArray(arrayToCache);
            fail("Expected an IllegalArgumentException because the array is already cached.");
        } catch (final IllegalArgumentException e) {
            // Verify that the exception has the expected message.
            assertEquals("Trying to cache an array that already exists", e.getMessage());
        }
    }
}