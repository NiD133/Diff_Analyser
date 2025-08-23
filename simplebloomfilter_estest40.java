package org.apache.commons.collections4.bloomfilter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

/**
 * Contains tests for the {@link SimpleBloomFilter}.
 */
public class SimpleBloomFilterTest {

    /**
     * Tests that merging a Hasher into a SimpleBloomFilter throws an
     * IllegalArgumentException if the Hasher produces an index that is outside the
     * valid range defined by the filter's Shape.
     */
    @Test
    public void mergeHasherWithIndexOutOfBoundsShouldThrowException() {
        // Arrange: Create a Bloom filter with a shape that has only one bit (m=1).
        // This means the only valid index is 0.
        final int numberOfBits = 1;
        final int numberOfHashFunctions = 655; // An arbitrary high number for k
        final Shape shape = Shape.fromKM(numberOfHashFunctions, numberOfBits);
        final SimpleBloomFilter filter = new SimpleBloomFilter(shape);

        // Arrange: Create a hasher that is known to produce indices greater than 0.
        // The merge operation should fail as any index >= 1 is out of the valid range [0, 1).
        final Hasher hasher = new EnhancedDoubleHasher(655, 655);

        // Act & Assert
        try {
            filter.merge(hasher);
            fail("Expected an IllegalArgumentException to be thrown because the hasher produces out-of-bounds indices.");
        } catch (final IllegalArgumentException e) {
            // Assert: Verify that the correct exception was thrown with the expected message.
            final String expectedMessage = "IndexExtractor should only send values in the range[0,1)";
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}