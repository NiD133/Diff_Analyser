package org.apache.commons.compress.harmony.unpack200;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link SegmentConstantPoolArrayCache}.
 */
// Renamed class for clarity and to follow standard conventions.
class SegmentConstantPoolArrayCacheTest {

    /**
     * Tests that the cache can correctly find the index of a unique element
     * within an array on the first lookup.
     */
    @Test
    // Renamed method to describe the behavior under test.
    void shouldFindIndexForUniqueElement() {
        // Arrange: Set up the test objects and data.
        final SegmentConstantPoolArrayCache arrayCache = new SegmentConstantPoolArrayCache();
        final String[] inputArray = {"Zero", "One", "Two", "Three", "Four"};
        final String keyToFind = "Three";
        final int expectedIndex = 3;

        // Act: Call the method being tested.
        final List<Integer> foundIndexes = arrayCache.indexesForArrayKey(inputArray, keyToFind);

        // Assert: Verify the results are correct.
        assertNotNull(foundIndexes, "The result list should never be null.");
        assertEquals(1, foundIndexes.size(), "Should find exactly one occurrence of the key.");
        assertEquals(expectedIndex, foundIndexes.get(0), "The index of the found element should be correct.");
    }
}