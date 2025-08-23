package org.apache.commons.collections4.bloomfilter;

import org.junit.Test;

/**
 * Contains tests for the SparseBloomFilter, focusing on behavior under
 * extreme resource constraints.
 */
public class SparseBloomFilterResourceTest {

    /**
     * Tests that calling processBitMaps() on a filter with a huge shape
     * throws an OutOfMemoryError.
     *
     * <p>The processBitMaps() method internally creates a long[] bitmap
     * representation of the filter. When the shape specifies a very large number of
     * bits (e.g., Integer.MAX_VALUE), the required array size can exceed
     * available heap memory. This test verifies that this allocation attempt fails
     * as expected with an OutOfMemoryError.</p>
     *
     * <p>Note: Testing for OutOfMemoryError is generally discouraged, but it is
     * appropriate here to confirm the behavior of the system at its memory limits.</p>
     */
    @Test(expected = OutOfMemoryError.class)
    public void processBitMapsWithHugeShapeShouldThrowOutOfMemoryError() {
        // Arrange: Create a Shape with the maximum possible number of bits.
        // This theoretical shape is too large to be instantiated in memory as a
        // dense bit array.
        final Shape hugeShape = Shape.fromKM(Integer.MAX_VALUE, Integer.MAX_VALUE);
        final SparseBloomFilter filter = new SparseBloomFilter(hugeShape);

        // Act: Attempt to process the filter's bit maps. This will trigger the
        // allocation of a giant long[], which is expected to fail. The predicate
        // itself is trivial (val -> true) as the error occurs before it is ever used.
        filter.processBitMaps(val -> true);
    }
}