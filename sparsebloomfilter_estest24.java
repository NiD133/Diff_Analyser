package org.apache.commons.collections4.bloomfilter;

import org.junit.Test;
import static org.junit.Assert.assertThrows;

/**
 * Contains an improved test case for the {@link SparseBloomFilter}.
 * The original test was auto-generated and has been rewritten for clarity.
 */
public class SparseBloomFilterTest {

    /**
     * Tests that {@code contains(IndexExtractor)} throws an OutOfMemoryError when the
     * extractor is derived from a filter with a very large shape (number of bits).
     *
     * <p>This scenario is designed to fail during the creation of an internal bitmap
     * representation, which would require an array size exceeding available memory.
     * This verifies the behavior under extreme memory pressure conditions.</p>
     */
    @Test
    public void contains_WithExtractorFromHugeFilter_ShouldThrowOutOfMemoryError() {
        // Arrange: Create a shape with a number of bits close to Integer.MAX_VALUE.
        // This size is chosen specifically to trigger an OutOfMemoryError when a
        // corresponding bitmap array is allocated.
        final int hugeNumberOfBits = Integer.MAX_VALUE - 8; // 2,147,483,639
        final int numberOfHashFunctions = hugeNumberOfBits;
        final Shape hugeShape = Shape.fromKM(numberOfHashFunctions, hugeNumberOfBits);

        final SparseBloomFilter filterWithHugeShape = new SparseBloomFilter(hugeShape);
        final IndexExtractor extractor = IndexExtractor.fromBitMapExtractor(filterWithHugeShape);

        // Act & Assert: The 'contains' method, when processing an IndexExtractor
        // derived from a BitMapExtractor, may attempt to create a large backing array.
        // With a shape this large, it is expected to throw an OutOfMemoryError.
        assertThrows(OutOfMemoryError.class, () -> {
            filterWithHugeShape.contains(extractor);
        });
    }
}