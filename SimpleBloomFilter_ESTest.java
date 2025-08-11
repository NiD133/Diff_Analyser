package org.apache.commons.collections4.bloomfilter;

import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.IntPredicate;
import java.util.function.LongPredicate;

import static org.junit.Assert.*;

public class SimpleBloomFilterTest {

    // ---------- Constructor and basic properties ----------

    @Test
    public void constructor_nullShape_throwsNullPointerException() {
        NullPointerException ex = assertThrows(NullPointerException.class, () -> new SimpleBloomFilter(null));
        assertEquals("shape", ex.getMessage());
    }

    @Test
    public void newFilter_isEmpty_zeroCardinality_zeroCharacteristics() {
        Shape shape = Shape.fromKM(3, 64);
        SimpleBloomFilter bf = new SimpleBloomFilter(shape);

        assertTrue(bf.isEmpty());
        assertEquals(0, bf.cardinality());
        assertEquals(0, bf.characteristics());
        assertSame(shape, bf.getShape());
    }

    // ---------- Merge via IndexExtractor ----------

    @Test
    public void mergeIndexExtractor_setsBits_andUpdatesCardinality() {
        Shape shape = Shape.fromKM(3, 64);
        SimpleBloomFilter bf = new SimpleBloomFilter(shape);

        int[] indices = {0, 7};
        IndexExtractor extractor = IndexExtractor.fromIndexArray(indices);

        boolean changed = bf.merge(extractor);

        assertTrue("Merging new indices should change the filter", changed);
        assertFalse("Filter should not be empty after setting bits", bf.isEmpty());
        assertEquals("Cardinality should match number of set bits", 2, bf.cardinality());
    }

    @Test
    public void mergeIndexExtractor_withOutOfRangeIndex_throwsIllegalArgumentException() {
        Shape shape = Shape.fromKM(1, 10);
        SimpleBloomFilter bf = new SimpleBloomFilter(shape);

        // index 10 is out of range [0,10)
        IndexExtractor outOfRange = IndexExtractor.fromIndexArray(new int[]{10});
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> bf.merge(outOfRange));
        assertTrue(ex.getMessage().contains("IndexExtractor should only send values in the range[0,10)"));
    }

    @Test
    public void mergeIndexExtractor_withNegativeIndex_throwsIllegalArgumentException() {
        Shape shape = Shape.fromKM(1, 10);
        SimpleBloomFilter bf = new SimpleBloomFilter(shape);

        IndexExtractor negative = IndexExtractor.fromIndexArray(new int[]{-1});
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> bf.merge(negative));
        assertTrue(ex.getMessage().contains("IndexExtractor should only send values in the range[0,10)"));
    }

    @Test
    public void mergeIndexExtractor_null_throwsNullPointerException() {
        Shape shape = Shape.fromKM(1, 10);
        SimpleBloomFilter bf = new SimpleBloomFilter(shape);

        NullPointerException ex = assertThrows(NullPointerException.class, () -> bf.merge((IndexExtractor) null));
        assertEquals("indexExtractor", ex.getMessage());
    }

    // ---------- Merge via Hasher / BitMapExtractor / BloomFilter ----------

    @Test
    public void mergeHasher_null_throwsNullPointerException() {
        Shape shape = Shape.fromKM(1, 10);
        SimpleBloomFilter bf = new SimpleBloomFilter(shape);

        NullPointerException ex = assertThrows(NullPointerException.class, () -> bf.merge((Hasher) null));
        assertEquals("hasher", ex.getMessage());
    }

    @Test
    public void mergeBitMapExtractor_null_throwsNullPointerException() {
        Shape shape = Shape.fromKM(1, 10);
        SimpleBloomFilter bf = new SimpleBloomFilter(shape);

        NullPointerException ex = assertThrows(NullPointerException.class, () -> bf.merge((BitMapExtractor) null));
        assertEquals("bitMapExtractor", ex.getMessage());
    }

    @Test
    public void mergeBloomFilter_null_throwsNullPointerException() {
        Shape shape = Shape.fromKM(1, 10);
        SimpleBloomFilter bf = new SimpleBloomFilter(shape);

        NullPointerException ex = assertThrows(NullPointerException.class, () -> bf.merge((BloomFilter<?>) null));
        assertEquals("other", ex.getMessage());
    }

    @Test
    public void mergeBloomFilter_withDifferentShape_throwsIllegalArgumentException() {
        // Different shapes should be incompatible
        Shape shapeA = Shape.fromKM(2, 64);
        Shape shapeB = Shape.fromKM(2, 128);

        SimpleBloomFilter a = new SimpleBloomFilter(shapeA);
        SimpleBloomFilter b = new SimpleBloomFilter(shapeB);

        assertThrows(IllegalArgumentException.class, () -> a.merge(b));
    }

    @Test
    public void mergeBloomFilter_withSameShape_andEmptyOther_returnsTrueButNoChange() {
        Shape shape = Shape.fromKM(2, 64);
        SimpleBloomFilter a = new SimpleBloomFilter(shape);
        SimpleBloomFilter b = new SimpleBloomFilter(shape);

        // Merging an empty BF may return true (operation performed) but should not change set bits
        boolean result = a.merge(b);

        assertTrue(result);
        assertTrue(a.isEmpty());
        assertEquals(0, a.cardinality());
    }

    // ---------- Contains ----------

    @Test
    public void contains_selfOnEmpty_returnsTrue() {
        Shape shape = Shape.fromKM(2, 64);
        SimpleBloomFilter bf = new SimpleBloomFilter(shape);

        // An empty Bloom filter contains another empty Bloom filter (vacuously true)
        assertTrue(bf.contains(bf));
    }

    @Test
    public void contains_nullIndexExtractor_throwsNullPointerException() {
        Shape shape = Shape.fromKM(1, 10);
        SimpleBloomFilter bf = new SimpleBloomFilter(shape);

        assertThrows(NullPointerException.class, () -> bf.contains(null));
    }

    // ---------- clear, copy ----------

    @Test
    public void clear_resetsBitsAndCardinalityAndEmptiness() {
        Shape shape = Shape.fromKM(2, 64);
        SimpleBloomFilter bf = new SimpleBloomFilter(shape);

        // Set a few bits via index extractor
        bf.merge(IndexExtractor.fromIndexArray(new int[]{1, 5, 7}));
        assertFalse(bf.isEmpty());
        assertEquals(3, bf.cardinality());

        // Clear should reset
        bf.clear();
        assertTrue(bf.isEmpty());
        assertEquals(0, bf.cardinality());
    }

    @Test
    public void copy_returnsDeepCopy_independentFromOriginal() {
        Shape shape = Shape.fromKM(2, 64);
        SimpleBloomFilter original = new SimpleBloomFilter(shape);

        original.merge(IndexExtractor.fromIndexArray(new int[]{2}));
        SimpleBloomFilter copy = original.copy();

        assertNotSame(original, copy);
        assertSame(original.getShape(), copy.getShape());
        assertEquals(original.cardinality(), copy.cardinality());

        // Mutate the copy and verify original is unchanged
        copy.merge(IndexExtractor.fromIndexArray(new int[]{3}));
        assertEquals(1, original.cardinality());
        assertEquals(2, copy.cardinality());
    }

    // ---------- process APIs (null checks and basic behavior) ----------

    @Test
    public void processIndices_nullPredicate_throwsNullPointerException() {
        Shape shape = Shape.fromKM(2, 64);
        SimpleBloomFilter bf = new SimpleBloomFilter(shape);

        NullPointerException ex = assertThrows(NullPointerException.class, () -> bf.processIndices(null));
        assertEquals("consumer", ex.getMessage());
    }

    @Test
    public void processBitMaps_nullPredicate_throwsNullPointerException() {
        Shape shape = Shape.fromKM(2, 64);
        SimpleBloomFilter bf = new SimpleBloomFilter(shape);

        NullPointerException ex = assertThrows(NullPointerException.class, () -> bf.processBitMaps((LongPredicate) null));
        assertEquals("consumer", ex.getMessage());
    }

    @Test
    public void processBitMapPairs_nullPredicate_throwsNullPointerException() {
        Shape shape = Shape.fromKM(2, 64);
        SimpleBloomFilter bf = new SimpleBloomFilter(shape);

        assertThrows(NullPointerException.class, () -> bf.processBitMapPairs(bf, null));
    }

    @Test
    public void processIndices_onEmptyFilter_invokesNoIndices_andReturnsTrue() {
        Shape shape = Shape.fromKM(2, 64);
        SimpleBloomFilter bf = new SimpleBloomFilter(shape);

        AtomicInteger calls = new AtomicInteger(0);
        IntPredicate counter = i -> {
            calls.incrementAndGet();
            return true; // continue
        };

        boolean result = bf.processIndices(counter);

        assertTrue(result);
        assertEquals("No indices set in an empty filter", 0, calls.get());
    }
}