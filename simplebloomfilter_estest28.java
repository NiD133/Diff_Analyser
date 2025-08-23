package org.apache.commons.collections4.bloomfilter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

/**
 * Unit tests for {@link SimpleBloomFilter}.
 */
public class SimpleBloomFilterTest {

    /**
     * Tests that {@code merge(BitMapExtractor)} throws a {@code NullPointerException}
     * when the provided extractor is null. This ensures the method correctly handles
     * invalid input through argument validation.
     */
    @Test
    public void mergeWithNullBitMapExtractorShouldThrowException() {
        // Arrange: Create a simple bloom filter. The specific shape is not important for this test.
        Shape shape = Shape.fromKM(1, 1);
        SimpleBloomFilter filter = new SimpleBloomFilter(shape);

        // Act & Assert: Execute the merge operation with a null argument and verify
        // that the expected exception is thrown.
        NullPointerException exception = assertThrows(NullPointerException.class, () -> {
            filter.merge((BitMapExtractor) null);
        });

        // Optionally, assert on the exception message to confirm the source of the error.
        // This is useful for ensuring the correct parameter validation failed.
        assertEquals("bitMapExtractor", exception.getMessage());
    }
}