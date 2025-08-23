package org.apache.commons.collections4.bloomfilter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

public class SparseBloomFilterTestTest1 extends AbstractBloomFilterTest<SparseBloomFilter> {

    @Override
    protected SparseBloomFilter createEmptyFilter(final Shape shape) {
        return new SparseBloomFilter(shape);
    }

    @Test
    void testBitMapExtractorEdgeCases() {
        int[] values = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 65, 66, 67, 68, 69, 70, 71 };
        BloomFilter bf = createFilter(getTestShape(), IndexExtractor.fromIndexArray(values));
        // verify exit early before bitmap boundary
        final int[] passes = new int[1];
        assertFalse(bf.processBitMaps(l -> {
            passes[0]++;
            return false;
        }));
        assertEquals(1, passes[0]);
        // verify exit early at bitmap boundary
        bf = createFilter(getTestShape(), IndexExtractor.fromIndexArray(values));
        passes[0] = 0;
        assertFalse(bf.processBitMaps(l -> {
            final boolean result = passes[0] == 0;
            if (result) {
                passes[0]++;
            }
            return result;
        }));
        assertEquals(1, passes[0]);
        // verify add extra if all values in first bitmap
        values = new int[] { 1, 2, 3, 4 };
        bf = createFilter(getTestShape(), IndexExtractor.fromIndexArray(values));
        passes[0] = 0;
        assertTrue(bf.processBitMaps(l -> {
            passes[0]++;
            return true;
        }));
        assertEquals(2, passes[0]);
        // verify exit early if all values in first bitmap and predicate returns false
        // on 2nd block
        values = new int[] { 1, 2, 3, 4 };
        bf = createFilter(getTestShape(), IndexExtractor.fromIndexArray(values));
        passes[0] = 0;
        assertFalse(bf.processBitMaps(l -> {
            final boolean result = passes[0] == 0;
            if (result) {
                passes[0]++;
            }
            return result;
        }));
        assertEquals(1, passes[0]);
    }
}
