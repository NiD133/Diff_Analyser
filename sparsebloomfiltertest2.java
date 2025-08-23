package org.apache.commons.collections4.bloomfilter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

public class SparseBloomFilterTestTest2 extends AbstractBloomFilterTest<SparseBloomFilter> {

    @Override
    protected SparseBloomFilter createEmptyFilter(final Shape shape) {
        return new SparseBloomFilter(shape);
    }

    @Test
    void testBloomFilterBasedMergeEdgeCases() {
        final BloomFilter bf1 = createEmptyFilter(getTestShape());
        final BloomFilter bf2 = new SimpleBloomFilter(getTestShape());
        bf2.merge(TestingHashers.FROM1);
        bf1.merge(bf2);
        assertTrue(bf2.processBitMapPairs(bf1, (x, y) -> x == y));
    }
}