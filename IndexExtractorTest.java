package org.apache.commons.collections4.bloomfilter;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.function.LongPredicate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class IndexExtractorTest {

    /**
     * A simple implementation of BitMapExtractor for testing purposes.
     */
    private static final class TestingBitMapExtractor implements BitMapExtractor {
        private final long[] values;

        TestingBitMapExtractor(final long[] values) {
            this.values = values;
        }

        @Override
        public boolean processBitMaps(final LongPredicate consumer) {
            for (final long value : values) {
                if (!consumer.test(value)) {
                    return false;
                }
            }
            return true;
        }
    }

    /**
     * Test the asIndexArray method of IndexExtractor with different input sizes.
     * 
     * @param n the number of indices to test
     */
    @ParameterizedTest
    @ValueSource(ints = {32, 33})
    void testAsIndexArray(final int n) {
        final IndexExtractor indexExtractor = index -> {
            for (int j = 0; j < n; j++) {
                // Always test index zero
                index.test(0);
            }
            return true;
        };
        int[] expectedArray = new int[n];
        Assertions.assertArrayEquals(expectedArray, indexExtractor.asIndexArray());
    }

    /**
     * Test the IndexExtractor created from a BitMapExtractor.
     */
    @Test
    void testFromBitMapExtractor() {
        // Test with a simple bitmap
        TestingBitMapExtractor simpleExtractor = new TestingBitMapExtractor(new long[] {1L, 2L, 3L});
        IndexExtractor indexExtractor = IndexExtractor.fromBitMapExtractor(simpleExtractor);
        List<Integer> indices = new ArrayList<>();

        indexExtractor.processIndices(indices::add);

        // Verify the expected indices
        assertEquals(4, indices.size());
        assertEquals(Integer.valueOf(0), indices.get(0));
        assertEquals(Integer.valueOf(1 + 64), indices.get(1));
        assertEquals(Integer.valueOf(0 + 128), indices.get(2));
        assertEquals(Integer.valueOf(1 + 128), indices.get(3));

        // Test with a full bitmap
        TestingBitMapExtractor fullExtractor = new TestingBitMapExtractor(new long[] {0xFFFFFFFFFFFFFFFFL});
        indexExtractor = IndexExtractor.fromBitMapExtractor(fullExtractor);
        indices.clear();

        indexExtractor.processIndices(indices::add);

        // Verify all 64 bits are set
        assertEquals(64, indices.size());
        for (int i = 0; i < 64; i++) {
            assertEquals(Integer.valueOf(i), indices.get(i));
        }
    }
}