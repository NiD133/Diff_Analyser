package org.apache.commons.compress.harmony.unpack200;

import org.junit.Test;
import static org.junit.Assert.assertNull;
import java.util.List;

/**
 * This class contains tests for {@link SegmentConstantPoolArrayCache}.
 * The original test was auto-generated and has been rewritten for clarity.
 */
public class SegmentConstantPoolArrayCache_ESTestTest3 extends SegmentConstantPoolArrayCache_ESTest_scaffolding {

    /**
     * Tests that indexesForArrayKey returns null if the fast-path cache is triggered
     * before the cached indexes have been populated.
     *
     * This scenario is simulated by manually setting the internal `lastArray` field
     * to prime the cache, then calling the method with a key that matches the
     * cache's initial `lastKey` state (null).
     */
    @Test(timeout = 4000)
    public void shouldReturnNullWhenFastPathCacheIsHitWithUninitializedResult() {
        // Arrange: Create a cache and simulate a specific internal state.
        // The goal is to have `lastArray` match the input array, while `lastIndexes`
        // (the cached result) remains in its initial null state.
        SegmentConstantPoolArrayCache cache = new SegmentConstantPoolArrayCache();
        String[] testArray = new String[]{"value1", "value2"};

        // Manually prime the fast-path cache with the array. The corresponding
        // `lastKey` and `lastIndexes` fields remain in their default null state.
        cache.lastArray = testArray;

        // Act: Call the method with the same array and a null key.
        // This matches the initial null state of `lastKey`, triggering the fast-path cache.
        List<Integer> resultIndexes = cache.indexesForArrayKey(testArray, null);

        // Assert: The method should return the value of `lastIndexes`, which is null
        // because it was never populated.
        assertNull("Expected null when the fast-path cache is hit before results are stored", resultIndexes);
    }
}