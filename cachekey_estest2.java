package org.apache.ibatis.cache;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Test suite for the CacheKey class.
 */
public class CacheKeyTest {

    /**
     * Verifies that calling the update() method correctly increments the internal update count.
     */
    @Test
    public void update_shouldIncrementUpdateCount() {
        // Arrange: Create a new CacheKey. Its initial count should be 0.
        CacheKey cacheKey = new CacheKey();
        Object anObject = new Object();

        // Act: Update the key with an object.
        cacheKey.update(anObject);

        // Assert: Verify that the update count is now 1.
        int actualUpdateCount = cacheKey.getUpdateCount();
        assertEquals("The update count should be 1 after a single update.", 1, actualUpdateCount);
    }
}