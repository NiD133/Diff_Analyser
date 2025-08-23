package org.apache.commons.collections4.bloomfilter;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

// The EvoSuite runner and scaffolding are kept as they might be required by the test execution environment.
import org.evosuite.runtime.EvoRunner;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class)
public class SparseBloomFilter_ESTestTest31 extends SparseBloomFilter_ESTest_scaffolding {

    /**
     * Tests that merging an IndexExtractor into an empty filter correctly modifies the filter
     * and returns true, indicating that the filter's state has changed.
     */
    @Test(timeout = 4000)
    public void testMergeIndexExtractorWhenFilterChanges() {
        // Arrange: Create an empty SparseBloomFilter and an IndexExtractor with indices to add.
        Shape shape = Shape.fromNM(100, 1000); // A reasonable shape for the filter
        SparseBloomFilter filter = new SparseBloomFilter(shape);
        assertTrue("Filter should be empty before the merge", filter.isEmpty());

        int[] indicesToMerge = {15, 78};
        IndexExtractor indexExtractor = IndexExtractor.fromIndexArray(indicesToMerge);

        // Act: Merge the indices from the extractor into the filter.
        boolean wasChanged = filter.merge(indexExtractor);

        // Assert: Verify that the merge operation was successful and the filter state is correct.
        
        // 1. The merge operation should report that the filter was changed.
        assertTrue("merge() should return true when new indices are added", wasChanged);

        // 2. The filter's cardinality should reflect the number of unique indices added.
        assertEquals("Cardinality should be 2 after merging two unique indices", 2, filter.cardinality());

        // 3. The filter should now contain the indices that were merged.
        assertTrue("Filter should contain the first merged index", filter.contains(IndexExtractor.fromIndexArray(new int[]{15})));
        assertTrue("Filter should contain the second merged index", filter.contains(IndexExtractor.fromIndexArray(new int[]{78})));
        
        // 4. The filter should not contain an index that was not part of the merge.
        assertFalse("Filter should not contain an unmerged index", filter.contains(IndexExtractor.fromIndexArray(new int[]{42})));
    }
}