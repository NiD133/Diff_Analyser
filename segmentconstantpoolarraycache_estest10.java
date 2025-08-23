package org.apache.commons.compress.harmony.unpack200;

import org.junit.Test;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Unit tests for the inner class {@link SegmentConstantPoolArrayCache.CachedArray}.
 */
public class SegmentConstantPoolCachedArrayTest {

    /**
     * Tests that `indexesForKey` correctly returns a list with a single index
     * when the searched key appears only once in the source array.
     */
    @Test
    public void indexesForKeyShouldReturnSingleIndexForUniqueKey() {
        // --- Arrange ---
        // The key we are searching for and its expected position in the array.
        final String searchKey = "";
        final int expectedIndex = 1;

        // Create an array where the key exists at a single, known index.
        // The other elements are null to ensure they don't interfere with the test.
        final String[] sourceArray = new String[8];
        sourceArray[expectedIndex] = searchKey;

        // The CachedArray is an inner class, so we need an instance of the outer class to create it.
        final SegmentConstantPoolArrayCache outerCache = new SegmentConstantPoolArrayCache();
        final SegmentConstantPoolArrayCache.CachedArray cachedArray = outerCache.new CachedArray(sourceArray);

        // --- Act ---
        // Perform the search for the key's indexes.
        final List<Integer> foundIndexes = cachedArray.indexesForKey(searchKey);

        // --- Assert ---
        // Verify that the returned list is not null and contains exactly one element.
        assertNotNull("The list of indexes should not be null.", foundIndexes);
        assertEquals("Should find exactly one index for the key.", 1, foundIndexes.size());

        // Verify that the single element in the list is the correct index.
        final Integer actualIndex = foundIndexes.get(0);
        assertEquals("The found index should match the expected index.", Integer.valueOf(expectedIndex), actualIndex);
    }
}