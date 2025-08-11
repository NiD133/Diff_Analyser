package org.apache.commons.collections4.bloomfilter;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.LongPredicate;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Tests for IndexExtractor.
 *
 * Focus:
 * - asIndexArray: verifies it returns all provided indices in encounter order (including duplicates).
 * - fromBitMapExtractor: verifies conversion from 64-bit blocks to absolute bit indices.
 */
class IndexExtractorTest {

    /**
     * Simple stub that feeds fixed bitmaps to a consumer.
     */
    private static final class StubBitMapExtractor implements BitMapExtractor {
        private final long[] bitMaps;

        StubBitMapExtractor(final long... bitMaps) {
            this.bitMaps = bitMaps;
        }

        @Override
        public boolean processBitMaps(final LongPredicate consumer) {
            for (final long bm : bitMaps) {
                if (!consumer.test(bm)) {
                    return false;
                }
            }
            return true;
        }
    }

    private static IndexExtractor extractorFromBitMaps(final long... bitMaps) {
        return IndexExtractor.fromBitMapExtractor(new StubBitMapExtractor(bitMaps));
    }

    private static List<Integer> toIndexList(final IndexExtractor extractor) {
        final List<Integer> out = new ArrayList<>();
        extractor.processIndices(out::add);
        return out;
    }

    @ParameterizedTest
    @ValueSource(ints = {32, 33})
    void asIndexArray_returnsAllProvidedIndices_inEncounterOrder(final int n) {
        // Provide the index 0 exactly n times.
        final IndexExtractor extractor = predicate -> {
            for (int i = 0; i < n; i++) {
                predicate.test(0);
            }
            return true;
        };

        // Expect an array of length n, all zeros (duplicates preserved).
        assertArrayEquals(new int[n], extractor.asIndexArray(),
                "Expected n copies of 0 when extractor yields index 0 n times");
    }

    @Test
    void fromBitMapExtractor_translatesSetBitsToAbsoluteIndices() {
        // Bitmaps: [1, 2, 3]
        // Explanation of expected indices:
        //   1L  -> bit 0 in block 0        => 0
        //   2L  -> bit 1 in block 1 (64)   => 64 + 1 = 65
        //   3L  -> bits 0,1 in block 2 (128) => 128, 129
        IndexExtractor extractor = extractorFromBitMaps(1L, 2L, 3L);
        List<Integer> indices = toIndexList(extractor);
        assertIterableEquals(Arrays.asList(0, 65, 128, 129), indices);

        // All bits set in a single 64-bit block => indices 0..63
        extractor = extractorFromBitMaps(0xFFFFFFFFFFFFFFFFL);
        indices = toIndexList(extractor);

        assertEquals(Long.SIZE, indices.size());
        for (int i = 0; i < Long.SIZE; i++) {
            assertEquals(i, indices.get(i));
        }
    }
}