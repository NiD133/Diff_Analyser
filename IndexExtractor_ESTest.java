package org.apache.commons.collections4.bloomfilter;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

/**
 * Readable unit tests for IndexExtractor and related helpers.
 *
 * These tests favor clarity:
 * - Descriptive test names
 * - Single responsibility per test
 * - Assert only what matters (avoid brittle internal exception messages)
 * - Small, safe shapes to avoid OOMs or timeouts
 */
public class IndexExtractorTest {

    @Test
    public void asIndexArray_onEmptySimpleBloomFilter_returnsEmptyArray() {
        // Given an empty filter
        Shape shape = Shape.fromKM(3, 64);
        SimpleBloomFilter filter = new SimpleBloomFilter(shape);

        // Then it contains no indices
        assertArrayEquals(new int[0], filter.asIndexArray());
    }

    @Test
    public void fromIndexArray_null_throwsForAsIndexArrayAndUniqueIndices() {
        IndexExtractor extractor = IndexExtractor.fromIndexArray((int[]) null);

        assertThrows(NullPointerException.class, extractor::asIndexArray);
        assertThrows(NullPointerException.class, extractor::uniqueIndices);
    }

    @Test
    public void fromBitMapExtractor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> IndexExtractor.fromBitMapExtractor(null));
    }

    @Test
    public void uniqueIndices_withNegativeIndex_throwsIndexOutOfBoundsException() {
        IndexExtractor extractor = IndexExtractor.fromIndexArray(-1);

        assertThrows(IndexOutOfBoundsException.class, extractor::uniqueIndices);
    }

    @Test
    public void uniqueIndices_deduplicatesAndSortsAscending() {
        IndexExtractor extractor = IndexExtractor.fromIndexArray(3, 1, 3, 2, 2);

        int[] unique = extractor.uniqueIndices().asIndexArray();

        assertArrayEquals(new int[] {1, 2, 3}, unique);
    }

    @Test
    public void uniqueIndices_isIdempotent() {
        IndexExtractor withDuplicates = IndexExtractor.fromIndexArray(0, 0, 0, 0);

        IndexExtractor uniqueOnce = withDuplicates.uniqueIndices();
        IndexExtractor uniqueTwice = uniqueOnce.uniqueIndices();

        assertArrayEquals(new int[] {0}, uniqueOnce.asIndexArray());
        // Once unique, calling uniqueIndices again should return the same instance
        assertSame(uniqueOnce, uniqueTwice);
    }

    @Test
    public void fromBitMapExtractor_withAllBitsSetInOneWord_produces64Indices() {
        // Only the 3rd 64-bit word has bits set
        long[] words = new long[3];
        words[2] = -1L;

        IndexExtractor extractor = IndexExtractor.fromBitMapExtractor(BitMapExtractor.fromBitMapArray(words));
        int[] indices = extractor.asIndexArray();

        assertEquals(64, indices.length);
        // All indices should be within the 3rd word: [128, 192)
        for (int idx : indices) {
            assertFalse("Index should be within [128,192): " + idx, idx < 128 || idx >= 192);
        }
    }

    @Test
    public void fromBitMapExtractor_countsBitsInWord() {
        long value = -2401L; // arbitrary pattern
        IndexExtractor extractor = IndexExtractor.fromBitMapExtractor(BitMapExtractor.fromBitMapArray(new long[] {value}));

        int[] indices = extractor.asIndexArray();

        assertEquals(Long.bitCount(value), indices.length);
    }

    @Test
    public void contains_withEmptyFiltersAndSomeIndices_returnsFalse() {
        IndexExtractor someIndex = IndexExtractor.fromIndexArray(0);
        Shape shape = Shape.fromKM(3, 64);

        SimpleBloomFilter simple = new SimpleBloomFilter(shape);
        ArrayCountingBloomFilter counting = new ArrayCountingBloomFilter(shape);
        SparseBloomFilter sparse = new SparseBloomFilter(shape);

        assertFalse(simple.contains(someIndex));
        assertFalse(counting.contains(someIndex));
        assertFalse(sparse.contains(someIndex));
    }
}