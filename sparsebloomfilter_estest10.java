package org.apache.commons.collections4.bloomfilter;

import org.junit.Test;
import java.util.function.IntPredicate;

/**
 * Contains tests for the {@link SparseBloomFilter} class.
 */
public class SparseBloomFilterTest {

    /**
     * Tests that {@code processIndices} correctly propagates a runtime exception thrown by the IntPredicate consumer.
     *
     * <p>This test simulates a scenario where the consumer processing the indices from the Bloom filter
     * throws an exception. This can occur if the consumer has internal constraints that are violated by an
     * index it receives (e.g., an array-based consumer receiving an out-of-bounds index).</p>
     *
     * <p>The test ensures that {@code SparseBloomFilter} does not suppress such exceptions and allows
     * them to propagate to the caller, which is the correct and expected behavior.</p>
     */
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void processIndicesShouldPropagateExceptionFromConsumer() {
        // Arrange
        // Define a shape where the number of bits is 989. Valid indices are in the range [0, 988].
        final int numberOfBits = 989;
        final int numberOfHashFunctions = 989;
        final Shape shape = Shape.fromKM(numberOfHashFunctions, numberOfBits);
        final SparseBloomFilter bloomFilter = new SparseBloomFilter(shape);

        // Use a specific hasher configuration that is known to produce indices that can
        // cause issues for consumers strictly bound by the shape's number of bits.
        final Hasher hasher = new EnhancedDoubleHasher(989, 989);
        bloomFilter.merge(hasher);

        // The ArrayTracker is an IntPredicate that validates and tracks indices. It is expected
        // to throw an exception if it receives an index outside the bounds defined by the shape.
        final IntPredicate faultyConsumer = new IndexFilter.ArrayTracker(shape);

        // Act
        // This call is expected to throw an exception because the consumer (ArrayTracker)
        // will fail while processing an index from the filter.
        bloomFilter.processIndices(faultyConsumer);

        // Assert is handled by the 'expected' attribute of the @Test annotation.
    }
}