package org.apache.commons.collections4.bloomfilter;

import org.junit.Test;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.function.LongPredicate;

/**
 * Contains tests for the {@link SimpleBloomFilter#merge(BitMapExtractor)} method.
 */
public class SimpleBloomFilterMergeTest {

    /**
     * Tests that merging a SimpleBloomFilter with a BitMapExtractor fails if the extractor
     * provides more bitmap longs than the filter's shape can accommodate.
     *
     * A SimpleBloomFilter's internal storage is a long array whose size is determined by its Shape.
     * The merge operation should reject any BitMapExtractor that attempts to write past the end
     * of this array.
     */
    @Test
    public void merge_throwsIllegalArgumentException_whenBitMapExtractorIsLargerThanShape() {
        // Arrange
        // 1. Create a filter with a shape that requires exactly one 'long' for its bitmap.
        // A shape with 64 bits fits perfectly into a single long.
        Shape shape = Shape.fromKM(17, 64); // k=17, m=64 bits
        SimpleBloomFilter filter = new SimpleBloomFilter(shape);

        // 2. Create a misbehaving BitMapExtractor that provides two longs, which is one more
        // than the filter's shape allows.
        BitMapExtractor oversizedExtractor = new BitMapExtractor() {
            @Override
            public boolean processBitMaps(LongPredicate consumer) {
                // The first call to the consumer will succeed.
                consumer.test(1L);
                // The second call should cause the merge method to throw an exception
                // because the filter's internal bitmap array has a size of 1.
                consumer.test(2L);
                return true;
            }
        };

        // Act & Assert
        // 3. Assert that attempting the merge throws an IllegalArgumentException.
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            filter.merge(oversizedExtractor);
        });

        // 4. Verify the exception message to confirm the failure is for the expected reason.
        String expectedMessage = "BitMapExtractor should send at most 1 maps";
        assertTrue(
            "Exception message should indicate the size mismatch.",
            exception.getMessage().contains(expectedMessage)
        );
    }
}