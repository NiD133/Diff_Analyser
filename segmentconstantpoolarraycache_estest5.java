package org.apache.commons.compress.harmony.unpack200;

import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

// The original test class name is kept for context. 
// In a real-world scenario, it would be renamed to SegmentConstantPoolArrayCacheTest.
public class SegmentConstantPoolArrayCache_ESTestTest5 extends SegmentConstantPoolArrayCache_ESTest_scaffolding {

    /**
     * Tests that an array becomes cached after being accessed for the first time
     * via the {@link SegmentConstantPoolArrayCache#indexesForArrayKey(String[], String)} method.
     */
    @Test(timeout = 4000)
    public void testIndexesForArrayKeyCachesTheArrayOnFirstAccess() {
        // Given: A cache and an uncached string array.
        SegmentConstantPoolArrayCache cache = new SegmentConstantPoolArrayCache();
        String[] stringArray = {"alpha", "beta", "gamma"};

        // Verify the precondition: the array should not be cached initially.
        assertFalse("Precondition failed: Array should not be cached before first access.", cache.arrayIsCached(stringArray));

        // When: The array is accessed to find the indexes of a key.
        // The return value is not needed for this test, as we are only verifying the caching side-effect.
        cache.indexesForArrayKey(stringArray, "beta");

        // Then: The array should now be cached.
        assertTrue("Array should be cached after being accessed.", cache.arrayIsCached(stringArray));
    }
}