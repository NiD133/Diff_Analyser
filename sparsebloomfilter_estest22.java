package org.apache.commons.collections4.bloomfilter;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Tests for the {@link SparseBloomFilter} class.
 */
public class SparseBloomFilterTest {

    /**
     * Tests that calling merge() with a null BitMapExtractor throws a NullPointerException.
     * This is the expected behavior for methods that require non-null arguments.
     */
    @Test
    public void mergeWithNullBitMapExtractorShouldThrowNullPointerException() {
        // Arrange: Create a filter with an arbitrary shape.
        Shape shape = Shape.fromKM(10, 100);
        SparseBloomFilter filter = new SparseBloomFilter(shape);
        BitMapExtractor nullExtractor = null;

        // Act & Assert: Verify that a NullPointerException is thrown with a specific message.
        try {
            filter.merge(nullExtractor);
            fail("Expected a NullPointerException to be thrown.");
        } catch (final NullPointerException e) {
            // The source code uses Objects.requireNonNull("bitMapExtractor", ...),
            // which results in this specific message.
            assertEquals("bitMapExtractor", e.getMessage());
        }
    }
}