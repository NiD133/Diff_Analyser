package org.apache.commons.collections4.bloomfilter;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.IntPredicate;
import java.util.function.LongPredicate;

import org.junit.Test;

/**
 * Readable unit tests for SparseBloomFilter.
 *
 * These tests cover core behaviors:
 * - construction and basic properties
 * - merging from various sources (Hasher, IndexExtractor, BloomFilter, BitMapExtractor)
 * - containment queries
 * - processing indices and bitmaps
 * - error handling for nulls, shape mismatches, and invalid indices
 */
public class SparseBloomFilterTest {

    // Helpers

    private static SparseBloomFilter newFilter(Shape shape) {
        return new SparseBloomFilter(shape);
    }

    /** Creates a non-empty filter using a simple hasher known to yield at least one index. */
    private static SparseBloomFilter newNonEmptyFilter(Shape shape) {
        SparseBloomFilter f = new SparseBloomFilter(shape);
        // This constructor is used in the original tests and reliably produces at least one index.
        EnhancedDoubleHasher hasher = new EnhancedDoubleHasher(new byte[6]);
        assertTrue("Expected merge(hasher) to add at least one index", f.merge(hasher));
        assertFalse("Filter should not be empty after merge", f.isEmpty());
        return f;
    }

    // Construction and basic properties

    @Test
    public void constructor_rejectsNullShape() {
        NullPointerException ex = assertThrows(NullPointerException.class, () -> new SparseBloomFilter(null));
        assertTrue(String.valueOf(ex.getMessage()).contains("shape"));
    }

    @Test
    public void newFilter_isEmptyWithZeroCardinality_andExpectedCharacteristics() {
        Shape shape = Shape.fromNM(64, 64);
        SparseBloomFilter bf = newFilter(shape);

        assertTrue(bf.isEmpty());
        assertEquals(0, bf.cardinality());
        // The implementation reports a fixed characteristic value for sparse filters (observed as 1).
        assertEquals(1, bf.characteristics());
    }

    @Test
    public void getShape_returnsSameShapeInstance() {
        Shape shape = Shape.fromNM(64, 64);
        SparseBloomFilter bf = newFilter(shape);

        assertSame(shape, bf.getShape());
    }

    // Merge behavior

    @Test
    public void merge_withHasher_makesFilterNonEmpty_andCardinalityIsPositive() {
        Shape shape = Shape.fromNM(64, 64);
        SparseBloomFilter bf = newFilter(shape);
        EnhancedDoubleHasher hasher = new EnhancedDoubleHasher(new byte[6]);

        assertTrue(bf.merge(hasher));
        assertFalse(bf.isEmpty());
        assertTrue(bf.cardinality() >= 1);
    }

    @Test
    public void merge_withIndexExtractor_addsIndices() {
        Shape shape = Shape.fromNM(64, 64);
        SparseBloomFilter bf = newFilter(shape);
        // An index in range [0, m) where m depends on shape; 1 is safe in typical shapes.
        IndexExtractor idx = IndexExtractor.fromIndexArray(new int[] {1});

        assertTrue(bf.merge(idx));
        assertFalse(bf.isEmpty());
        assertTrue(bf.cardinality() >= 1);
    }

    @Test
    public void merge_withIndexExtractor_rejectsNegativeIndex() {
        Shape shape = Shape.fromNM(64, 64);
        SparseBloomFilter bf = newFilter(shape);
        IndexExtractor idx = IndexExtractor.fromIndexArray(new int[] {-1});

        assertThrows(IllegalArgumentException.class, () -> bf.merge(idx));
    }

    @Test
    public void merge_withIndexExtractor_rejectsIndexOutOfRange() {
        Shape shape = Shape.fromNM(12, 12);
        SparseBloomFilter bf = newFilter(shape);
        // 12 is out of range for [0..11]
        IndexExtractor idx = IndexExtractor.fromIndexArray(new int[] {12});

        assertThrows(IllegalArgumentException.class, () -> bf.merge(idx));
    }

    @Test
    public void merge_withBloomFilter_sameShape_succeeds() {
        Shape shape = Shape.fromNM(64, 64);
        SparseBloomFilter a = newNonEmptyFilter(shape);
        SparseBloomFilter b = newFilter(shape);

        assertTrue(b.merge((BloomFilter<?>) a));
        assertFalse(b.isEmpty());
    }

    @Test
    public void merge_withBloomFilter_differentShape_throwsIllegalArgumentException() {
        SparseBloomFilter a = newNonEmptyFilter(Shape.fromNM(64, 64));
        SparseBloomFilter b = newFilter(Shape.fromNM(32, 32));

        assertThrows(IllegalArgumentException.class, () -> b.merge((BloomFilter<?>) a));
    }

    @Test
    public void merge_withBitMapExtractor_selfIsAllowed_whenInRange() {
        Shape shape = Shape.fromNM(64, 64);
        SparseBloomFilter bf = newNonEmptyFilter(shape);

        assertTrue(bf.merge((BitMapExtractor) bf));
    }

    // Null argument handling

    @Test
    public void merge_hasher_null_throwsNPE() {
        Shape shape = Shape.fromNM(10, 10);
        SparseBloomFilter bf = newFilter(shape);

        NullPointerException ex = assertThrows(NullPointerException.class, () -> bf.merge((Hasher) null));
        assertTrue(String.valueOf(ex.getMessage()).contains("hasher"));
    }

    @Test
    public void merge_indexExtractor_null_throwsNPE() {
        Shape shape = Shape.fromNM(10, 10);
        SparseBloomFilter bf = newFilter(shape);

        NullPointerException ex = assertThrows(NullPointerException.class, () -> bf.merge((IndexExtractor) null));
        assertTrue(String.valueOf(ex.getMessage()).contains("indexExtractor"));
    }

    @Test
    public void merge_bitMapExtractor_null_throwsNPE() {
        Shape shape = Shape.fromNM(10, 10);
        SparseBloomFilter bf = newFilter(shape);

        NullPointerException ex = assertThrows(NullPointerException.class, () -> bf.merge((BitMapExtractor) null));
        assertTrue(String.valueOf(ex.getMessage()).contains("bitMapExtractor"));
    }

    @Test
    public void merge_bloomFilter_null_throwsNPE() {
        Shape shape = Shape.fromNM(10, 10);
        SparseBloomFilter bf = newFilter(shape);

        NullPointerException ex = assertThrows(NullPointerException.class, () -> bf.merge((BloomFilter<?>) null));
        assertTrue(String.valueOf(ex.getMessage()).contains("other"));
    }

    // Contains behavior

    @Test
    public void contains_indexExtractor_onEmpty_returnsFalse() {
        Shape shape = Shape.fromNM(64, 64);
        SparseBloomFilter bf = newFilter(shape);
        IndexExtractor idx = IndexExtractor.fromIndexArray(new int[] {1});

        assertFalse(bf.contains(idx));
    }

    @Test
    public void contains_selfAsIndexExtractor_returnsTrue() {
        Shape shape = Shape.fromNM(64, 64);
        SparseBloomFilter bf = newNonEmptyFilter(shape);

        assertTrue(bf.contains((IndexExtractor) bf));
    }

    @Test
    public void contains_selfAsBitMapExtractor_returnsTrue() {
        Shape shape = Shape.fromNM(64, 64);
        SparseBloomFilter bf = newNonEmptyFilter(shape);

        assertTrue(bf.contains((BitMapExtractor) bf));
    }

    @Test
    public void contains_indexExtractor_null_throwsNPE() {
        Shape shape = Shape.fromNM(64, 64);
        SparseBloomFilter bf = newFilter(shape);

        assertThrows(NullPointerException.class, () -> bf.contains((IndexExtractor) null));
    }

    @Test
    public void contains_bitMapExtractor_null_throwsNPE() {
        Shape shape = Shape.fromNM(64, 64);
        SparseBloomFilter bf = newFilter(shape);

        assertThrows(NullPointerException.class, () -> bf.contains((BitMapExtractor) null));
    }

    // Processing callbacks

    @Test
    public void processIndices_onEmpty_returnsTrueAndDoesNotCallConsumer() {
        Shape shape = Shape.fromNM(64, 64);
        SparseBloomFilter bf = newFilter(shape);
        AtomicInteger calls = new AtomicInteger();

        boolean result = bf.processIndices((int idx) -> {
            calls.incrementAndGet();
            return true;
        });

        assertTrue(result);
        assertEquals(0, calls.get());
    }

    @Test
    public void processIndices_onNonEmpty_callsConsumerAndReturnsTrueWhenAllAccept() {
        Shape shape = Shape.fromNM(64, 64);
        SparseBloomFilter bf = newNonEmptyFilter(shape);
        List<Integer> seen = new ArrayList<>();

        boolean result = bf.processIndices((int idx) -> {
            seen.add(idx);
            return true;
        });

        assertTrue(result);
        assertFalse("Expected at least one index to be processed", seen.isEmpty());
    }

    @Test
    public void processIndices_nullConsumer_throwsNPE() {
        Shape shape = Shape.fromNM(64, 64);
        SparseBloomFilter bf = newFilter(shape);

        NullPointerException ex = assertThrows(NullPointerException.class, () -> bf.processIndices((IntPredicate) null));
        assertTrue(String.valueOf(ex.getMessage()).contains("consumer"));
    }

    @Test
    public void processBitMaps_onEmpty_returnsTrueAndDoesNotCallConsumer() {
        Shape shape = Shape.fromNM(64, 64);
        SparseBloomFilter bf = newFilter(shape);
        AtomicInteger calls = new AtomicInteger();

        boolean result = bf.processBitMaps((long bitmap) -> {
            calls.incrementAndGet();
            return true;
        });

        assertTrue(result);
        assertEquals(0, calls.get());
    }

    @Test
    public void processBitMaps_onNonEmpty_callsConsumerAndReturnsTrueWhenAllAccept() {
        Shape shape = Shape.fromNM(64, 64);
        SparseBloomFilter bf = newNonEmptyFilter(shape);
        AtomicInteger calls = new AtomicInteger();

        boolean result = bf.processBitMaps((long bitmap) -> {
            calls.incrementAndGet();
            return true;
        });

        assertTrue(result);
        assertTrue("Expected at least one bitmap to be processed", calls.get() > 0);
    }

    @Test
    public void processBitMaps_nullConsumer_throwsNPE() {
        Shape shape = Shape.fromNM(64, 64);
        SparseBloomFilter bf = newFilter(shape);

        NullPointerException ex = assertThrows(NullPointerException.class, () -> bf.processBitMaps((LongPredicate) null));
        assertTrue(String.valueOf(ex.getMessage()).contains("consumer"));
    }

    // Copy and clear

    @Test
    public void copy_createsIndependentFilterWithSameContent() {
        Shape shape = Shape.fromNM(64, 64);
        SparseBloomFilter original = newNonEmptyFilter(shape);

        SparseBloomFilter copy = original.copy();

        assertSame(shape, copy.getShape());
        assertEquals(original.characteristics(), copy.characteristics());
        // Mutual containment is a simple way to assert equal bitsets without equality contract
        assertTrue(original.contains((IndexExtractor) copy));
        assertTrue(copy.contains((IndexExtractor) original));
    }

    @Test
    public void clear_removesAllIndices() {
        Shape shape = Shape.fromNM(64, 64);
        SparseBloomFilter bf = newNonEmptyFilter(shape);

        bf.clear();

        assertTrue(bf.isEmpty());
        assertEquals(0, bf.cardinality());
    }
}