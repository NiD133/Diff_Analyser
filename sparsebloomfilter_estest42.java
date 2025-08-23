package org.apache.commons.collections4.bloomfilter;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Contains tests for the {@link SparseBloomFilter}.
 */
public class SparseBloomFilterTest {

    /**
     * Tests that the characteristics of the filter remain {@code SPARSE} even after
     * the {@code clear()} method is called.
     *
     * <p>The {@code characteristics()} method indicates the optimal way to process the filter's
     * contents. For a {@code SparseBloomFilter}, this should always be {@code SPARSE},
     * regardless of its state.</p>
     */
    @Test
    public void testCharacteristicsIsSparseAfterClear() {
        // Arrange: Create a new, empty SparseBloomFilter.
        // The specific shape parameters are not critical for this test.
        Shape shape = Shape.fromKM(10, 100);
        SparseBloomFilter filter = new SparseBloomFilter(shape);

        // Act: Clear the filter. This should have no effect on a new filter,
        // but we call it to ensure the characteristic is not altered by this operation.
        filter.clear();

        // Assert: The filter should still report its characteristic as SPARSE.
        // The BloomFilter.SPARSE constant (value of 1) indicates that processing
        // by indices is most efficient for this filter type.
        int expectedCharacteristics = BloomFilter.SPARSE;
        int actualCharacteristics = filter.characteristics();
        
        assertEquals("A SparseBloomFilter should always have the SPARSE characteristic",
                expectedCharacteristics, actualCharacteristics);
    }
}