package org.apache.commons.collections4.bloomfilter;

import org.junit.Test;

/**
 * This test verifies the behavior of the SparseBloomFilter's processIndices method
 * when interacting with an index consumer that has a mismatched (smaller) shape.
 */
public class SparseBloomFilter_ESTestTest12 extends SparseBloomFilter_ESTest_scaffolding {

    /**
     * Tests that {@code processIndices} throws an {@code ArrayIndexOutOfBoundsException}
     * when the provided IntPredicate consumer is not large enough to handle all the
     * indices from the Bloom filter.
     *
     * This scenario occurs when the consumer is backed by a data structure (like an array)
     * sized according to a Shape with fewer bits (m) than the Bloom filter itself.
     */
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void processIndicesShouldThrowExceptionWhenConsumerShapeIsTooSmall() {
        // Arrange
        // 1. Create a Bloom filter with a shape that allows for indices up to 993.
        final Shape filterShape = Shape.fromKM(10, 994); // k=10 hash functions, m=994 bits
        final SparseBloomFilter bloomFilter = new SparseBloomFilter(filterShape);

        // 2. Merge a hasher to populate the filter with some indices. The specific
        //    hasher content is not important, only that it generates indices within
        //    the filter's shape [0, 993].
        final Hasher hasher = new EnhancedDoubleHasher(new byte[]{1, 2, 3, 4});
        bloomFilter.merge(hasher);

        // 3. Create an IndexTracker (the consumer) with a much smaller shape.
        //    This tracker's internal state will be too small to handle large indices
        //    produced by the filter.
        final Shape consumerShape = Shape.fromKM(5, 1); // m=1 bit, so it can only handle index 0.
        final IndexFilter.ArrayTracker consumerWithSmallCapacity = new IndexFilter.ArrayTracker(consumerShape);

        // Act
        // Attempt to process the filter's indices using the undersized consumer.
        // This will cause an ArrayIndexOutOfBoundsException because the filter will
        // inevitably produce an index greater than 0, which is out of bounds for
        // the consumer's internal array.
        bloomFilter.processIndices(consumerWithSmallCapacity);

        // Assert (handled by the @Test(expected=...) annotation)
        // The test will pass only if an ArrayIndexOutOfBoundsException is thrown.
    }
}