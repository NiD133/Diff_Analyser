package org.apache.commons.compress.harmony.unpack200;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link SegmentConstantPoolArrayCache}.
 */
// Renamed class for clarity and to follow standard naming conventions (from SegmentConstantPoolArrayCacheTestTest2).
class SegmentConstantPoolArrayCacheTest {

    @Test
    @DisplayName("indexesForArrayKey() should return all indices for a key that appears multiple times")
    // Renamed test method to clearly describe the scenario and expected outcome.
    void shouldFindAllIndexesForRepeatedKey() {
        // --- Arrange ---
        // Set up the test data and expected results.
        final SegmentConstantPoolArrayCache arrayCache = new SegmentConstantPoolArrayCache();

        // Use a constant for the search key to avoid "magic strings" and improve maintainability.
        final String searchKey = "OneThreeFour";

        final String[] sourceArray = {
            "Zero",
            searchKey, // Expected at index 1
            "Two",
            searchKey, // Expected at index 3
            searchKey  // Expected at index 4
        };

        // Clearly define the expected outcome.
        final List<Integer> expectedIndexes = List.of(1, 3, 4);

        // --- Act ---
        // Call the method under test.
        // The variable name 'actualIndexes' is more descriptive than 'list'.
        final List<Integer> actualIndexes = arrayCache.indexesForArrayKey(sourceArray, searchKey);

        // --- Assert ---
        // Verify the result is as expected.
        // assertIterableEquals is more concise and expressive than checking size and each element individually.
        assertIterableEquals(expectedIndexes, actualIndexes,
            "The returned list of indexes should match the expected list.");
    }
}