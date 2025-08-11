package org.apache.commons.collections4.bloomfilter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.Test;

/**
 * Tests for the {@link SparseBloomFilter}.
 * 
 * The tests below focus on edge cases around processBitMaps, i.e. iteration
 * over 64-bit words that back the Bloom filter. They purposely use indices
 * that either:
 * - Stay within the first 64-bit word, or
 * - Cross the 64-bit word boundary (into the second word),
 * so we can verify early-exit behavior and the extra invocation after the last
 * non-empty word.
 */
class SparseBloomFilterTest extends AbstractBloomFilterTest<SparseBloomFilter> {

    // Indices that span two 64-bit words (1..9 in the first word, 65..71 in the second).
    private static final int[] INDICES_CROSSING_WORD_BOUNDARY =
        {1, 2, 3, 4, 5, 6, 7, 8, 9, 65, 66, 67, 68, 69, 70, 71};

    // A small set of indices entirely within the first 64-bit word.
    private static final int[] INDICES_FIRST_WORD_ONLY = {1, 2, 3, 4};

    @Override
    protected SparseBloomFilter createEmptyFilter(final Shape shape) {
        return new SparseBloomFilter(shape);
    }

    private BloomFilter<?> createFilterWithIndices(final int... indices) {
        return createFilter(getTestShape(), IndexExtractor.fromIndexArray(indices));
    }

    @Test
    void processBitMaps_stopsImmediately_whenPredicateIsFalse() {
        final BloomFilter<?> bf = createFilterWithIndices(INDICES_CROSSING_WORD_BOUNDARY);

        final AtomicInteger callCount = new AtomicInteger();

        final boolean result = bf.processBitMaps(bits -> {
            callCount.incrementAndGet();
            return false; // stop immediately
        });

        assertFalse(result, "processBitMaps should stop when the predicate returns false");
        assertEquals(1, callCount.get(), "Predicate should be called exactly once");
    }

    @Test
    void processBitMaps_stopsAtWordBoundary_whenPredicateTurnsFalseOnSecondCall() {
        final BloomFilter<?> bf = createFilterWithIndices(INDICES_CROSSING_WORD_BOUNDARY);

        final AtomicInteger callCount = new AtomicInteger();
        final AtomicInteger trueCount = new AtomicInteger();

        final boolean result = bf.processBitMaps(bits -> {
            final int n = callCount.getAndIncrement();
            final boolean keepGoing = (n == 0); // true on first word, false on second
            if (keepGoing) {
                trueCount.incrementAndGet();
            }
            return keepGoing;
        });

        assertFalse(result, "processBitMaps should return false when the predicate eventually returns false");
        assertEquals(2, callCount.get(), "Expected two invocations across the 64-bit word boundary");
        assertEquals(1, trueCount.get(), "Only the first invocation should have returned true");
    }

    @Test
    void processBitMaps_invokesTrailingBlock_whenAllIndicesAreInFirstWord() {
        final BloomFilter<?> bf = createFilterWithIndices(INDICES_FIRST_WORD_ONLY);

        final AtomicInteger callCount = new AtomicInteger();

        final boolean result = bf.processBitMaps(bits -> {
            callCount.incrementAndGet();
            return true; // never stop
        });

        // Implementation detail: When all bits are in the first word, the iteration will
        // also include the next (empty) word to signal the end boundary. Hence two calls.
        assertTrue(result, "processBitMaps should return true when the predicate never returns false");
        assertEquals(2, callCount.get(),
                "Expected two invocations: the first non-empty word and a trailing (empty) word after it");
    }

    @Test
    void processBitMaps_stopsOnSecondBlock_whenPredicateTurnsFalse_withAllIndicesInFirstWord() {
        final BloomFilter<?> bf = createFilterWithIndices(INDICES_FIRST_WORD_ONLY);

        final AtomicInteger callCount = new AtomicInteger();
        final AtomicInteger trueCount = new AtomicInteger();

        final boolean result = bf.processBitMaps(bits -> {
            final int n = callCount.getAndIncrement();
            final boolean keepGoing = (n == 0); // true on first (non-empty) word, false on trailing word
            if (keepGoing) {
                trueCount.incrementAndGet();
            }
            return keepGoing;
        });

        assertFalse(result, "processBitMaps should return false when the predicate turns false on the trailing word");
        assertEquals(2, callCount.get(), "Expected two invocations due to the trailing word");
        assertEquals(1, trueCount.get(), "Only the first invocation should have returned true");
    }

    @Test
    void mergeFromAnotherBloomFilter_preservesCompatibilityInBitMapPairsProcessing() {
        final BloomFilter<?> emptySparse = createEmptyFilter(getTestShape());
        final BloomFilter<?> simple = new SimpleBloomFilter(getTestShape());

        // Populate the SimpleBloomFilter and then merge into the SparseBloomFilter.
        simple.merge(TestingHashers.FROM1);
        emptySparse.merge(simple);

        // Validate that processing pairs of bitmaps between the two filters does not diverge.
        assertTrue(simple.processBitMapPairs(emptySparse, (x, y) -> x == y),
                "After merge, corresponding bitmaps should match when processed in pairs");
    }
}