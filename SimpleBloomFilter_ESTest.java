package org.apache.commons.collections4.bloomfilter;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import java.util.function.IntPredicate;
import java.util.function.LongPredicate;

import org.apache.commons.collections4.bloomfilter.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.ViolatedAssumptionAnswer;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(
    mockJVMNonDeterminism = true, 
    useVFS = true, 
    useVNET = true, 
    resetStaticState = true, 
    separateClassLoader = true
)
public class SimpleBloomFilter_ESTest extends SimpleBloomFilter_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void testMergeWithInvalidIndexExtractor() {
        Shape shape = Shape.fromKM(1, 1540);
        int[] indices = {5023};
        IndexExtractor indexExtractor = IndexExtractor.fromIndexArray(indices);
        LayerManager<SparseBloomFilter> layerManager = mockLayerManager(false);
        LayeredBloomFilter<SparseBloomFilter> layeredBloomFilter = new LayeredBloomFilter<>(shape, layerManager);
        SimpleBloomFilter simpleBloomFilter = layeredBloomFilter.flatten();

        try {
            simpleBloomFilter.merge(indexExtractor);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.apache.commons.collections4.bloomfilter.SimpleBloomFilter", e);
        }
    }

    @Test(timeout = 4000)
    public void testMergeWithHasherAndSparseBloomFilter() {
        Shape shape = Shape.fromKM(19, 19);
        SimpleBloomFilter simpleBloomFilter = new SimpleBloomFilter(shape);
        SparseBloomFilter sparseBloomFilter = new SparseBloomFilter(shape);
        EnhancedDoubleHasher hasher = new EnhancedDoubleHasher(19, 2654L);

        assertTrue(simpleBloomFilter.merge(hasher));
        assertTrue(simpleBloomFilter.merge(sparseBloomFilter));
    }

    @Test(timeout = 4000)
    public void testMergeWithHasherAndSelf() {
        Shape shape = Shape.fromNMK(78, 78, 2);
        SimpleBloomFilter simpleBloomFilter = new SimpleBloomFilter(shape);
        EnhancedDoubleHasher hasher = new EnhancedDoubleHasher(1L, Long.MIN_VALUE);

        assertTrue(simpleBloomFilter.merge(hasher));
        assertTrue(simpleBloomFilter.merge(simpleBloomFilter));
    }

    @Test(timeout = 4000)
    public void testMergeWithHasherAndCheckEmpty() {
        Shape shape = Shape.fromNM(64, 64);
        LayerManager<SparseBloomFilter> layerManager = mockLayerManager(false);
        LayeredBloomFilter<SparseBloomFilter> layeredBloomFilter = new LayeredBloomFilter<>(shape, layerManager);
        SimpleBloomFilter simpleBloomFilter = layeredBloomFilter.flatten();
        EnhancedDoubleHasher hasher = new EnhancedDoubleHasher(Integer.MAX_VALUE, 0L);

        assertTrue(simpleBloomFilter.merge(hasher));
        assertFalse(simpleBloomFilter.isEmpty());
    }

    @Test(timeout = 4000)
    public void testCardinalityAfterMerge() {
        Shape shape = Shape.fromNMK(115, 115, 2);
        SimpleBloomFilter simpleBloomFilter = new SimpleBloomFilter(shape);
        EnhancedDoubleHasher hasher = new EnhancedDoubleHasher(1L, Long.MIN_VALUE);

        assertTrue(simpleBloomFilter.merge(hasher));
        assertEquals(2, simpleBloomFilter.cardinality());
    }

    @Test(timeout = 4000)
    public void testProcessIndicesWithArrayTracker() {
        Shape shape = Shape.fromNMK(78, 78, 2);
        SimpleBloomFilter simpleBloomFilter = new SimpleBloomFilter(shape);
        IndexFilter.ArrayTracker arrayTracker = new IndexFilter.ArrayTracker(shape);

        assertTrue(simpleBloomFilter.processIndices(arrayTracker));
    }

    @Test(timeout = 4000)
    public void testProcessBitMapsWithNegatedPredicate() {
        Shape shape = Shape.fromNMK(3419, 3419, 1);
        SimpleBloomFilter simpleBloomFilter = new SimpleBloomFilter(shape);
        LongBiPredicate longBiPredicate = mockLongBiPredicate(false);
        CountingLongPredicate countingLongPredicate = new CountingLongPredicate(new long[3], longBiPredicate);
        LongPredicate negatedPredicate = countingLongPredicate.negate();

        assertTrue(simpleBloomFilter.processBitMaps(negatedPredicate));
    }

    @Test(timeout = 4000)
    public void testProcessBitMapsWithCountingLongPredicate() {
        Shape shape = Shape.fromNM(10, 10);
        SimpleBloomFilter simpleBloomFilter = new SimpleBloomFilter(shape);
        LongBiPredicate longBiPredicate = mockLongBiPredicate(false);
        CountingLongPredicate countingLongPredicate = new CountingLongPredicate(new long[1], longBiPredicate);

        assertFalse(simpleBloomFilter.processBitMaps(countingLongPredicate));
    }

    @Test(timeout = 4000)
    public void testProcessBitMapPairsWithLayeredBloomFilter() {
        Shape shape = Shape.fromNM(51, 51);
        LayerManager<SparseBloomFilter> layerManager = mockLayerManager(true, false);
        LayeredBloomFilter<SparseBloomFilter> layeredBloomFilter = new LayeredBloomFilter<>(shape, layerManager);
        SimpleBloomFilter simpleBloomFilter = layeredBloomFilter.flatten();
        LongBiPredicate longBiPredicate = mockLongBiPredicate(true);

        assertTrue(simpleBloomFilter.processBitMapPairs(layeredBloomFilter, longBiPredicate));
    }

    @Test(timeout = 4000)
    public void testMergeWithSelfAsIndexExtractor() {
        Shape shape = Shape.fromKM(22, 22);
        SimpleBloomFilter simpleBloomFilter = new SimpleBloomFilter(shape);

        assertTrue(simpleBloomFilter.merge(simpleBloomFilter));
    }

    @Test(timeout = 4000)
    public void testGetShape() {
        Shape shape = Shape.fromNM(294, 294);
        LayerManager<SparseBloomFilter> layerManager = mockLayerManager(false);
        LayeredBloomFilter<SparseBloomFilter> layeredBloomFilter = new LayeredBloomFilter<>(shape, layerManager);
        SimpleBloomFilter simpleBloomFilter = layeredBloomFilter.flatten();

        assertEquals(203.7852710846239, simpleBloomFilter.getShape().estimateMaxN(), 0.01);
    }

    @Test(timeout = 4000)
    public void testContainsWithLayeredBloomFilter() {
        Shape shape = Shape.fromNMK(64, 64, 1);
        LayerManager<SparseBloomFilter> layerManager = mockLayerManager(false);
        LayeredBloomFilter<SparseBloomFilter> layeredBloomFilter = new LayeredBloomFilter<>(shape, layerManager);
        SimpleBloomFilter simpleBloomFilter = new SimpleBloomFilter(shape);

        assertFalse(simpleBloomFilter.contains(layeredBloomFilter));
    }

    @Test(timeout = 4000)
    public void testCharacteristics() {
        Shape shape = Shape.fromKM(4, 4);
        SimpleBloomFilter simpleBloomFilter = new SimpleBloomFilter(shape);

        assertEquals(0, simpleBloomFilter.characteristics());
    }

    @Test(timeout = 4000)
    public void testCardinalityAfterMergeWithHasher() {
        Shape shape = Shape.fromNM(38, 38);
        SimpleBloomFilter simpleBloomFilter = new SimpleBloomFilter(shape);
        EnhancedDoubleHasher hasher = new EnhancedDoubleHasher(38, 38);

        assertTrue(simpleBloomFilter.merge(hasher));
        assertEquals(1, simpleBloomFilter.cardinality());
        assertFalse(simpleBloomFilter.isEmpty());
    }

    @Test(timeout = 4000)
    public void testProcessIndicesWithLargeShape() {
        Shape shape = Shape.fromNM(Integer.MAX_VALUE, Integer.MAX_VALUE);
        SimpleBloomFilter simpleBloomFilter = new SimpleBloomFilter(shape);
        IndexFilter.ArrayTracker arrayTracker = new IndexFilter.ArrayTracker(shape);

        simpleBloomFilter.processIndices(arrayTracker);
    }

    @Test(timeout = 4000)
    public void testProcessIndicesWithNullPredicate() {
        Shape shape = Shape.fromNM(38, 38);
        SimpleBloomFilter simpleBloomFilter = new SimpleBloomFilter(shape);

        try {
            simpleBloomFilter.processIndices((IntPredicate) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("java.util.Objects", e);
        }
    }

    @Test(timeout = 4000)
    public void testProcessBitMapsWithEmptyArray() {
        Shape shape = Shape.fromNM(2147483639, 2147483639);
        SimpleBloomFilter simpleBloomFilter = new SimpleBloomFilter(shape);
        LongBiPredicate longBiPredicate = mockLongBiPredicate(false);
        CountingLongPredicate countingLongPredicate = new CountingLongPredicate(new long[0], longBiPredicate);
        LongPredicate negatedPredicate = countingLongPredicate.negate();

        simpleBloomFilter.processBitMaps(negatedPredicate);
    }

    @Test(timeout = 4000)
    public void testProcessBitMapsWithNullPredicate() {
        Shape shape = Shape.fromKM(1, 1);
        SimpleBloomFilter simpleBloomFilter = new SimpleBloomFilter(shape);

        try {
            simpleBloomFilter.processBitMaps((LongPredicate) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("java.util.Objects", e);
        }
    }

    @Test(timeout = 4000)
    public void testProcessBitMapPairsWithNullPredicate() {
        Shape shape = Shape.fromNMK(15, 15, 15);
        SimpleBloomFilter simpleBloomFilter = new SimpleBloomFilter(shape);

        try {
            simpleBloomFilter.processBitMapPairs(simpleBloomFilter, (LongBiPredicate) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testMergeWithSelfAsIndexExtractorLargeShape() {
        Shape shape = Shape.fromNM(2147483602, 2147483602);
        SimpleBloomFilter simpleBloomFilter = new SimpleBloomFilter(shape);

        simpleBloomFilter.merge(simpleBloomFilter);
    }

    @Test(timeout = 4000)
    public void testMergeWithNullIndexExtractor() {
        Shape shape = Shape.fromKM(668, 668);
        SimpleBloomFilter simpleBloomFilter = new SimpleBloomFilter(shape);

        try {
            simpleBloomFilter.merge((IndexExtractor) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("java.util.Objects", e);
        }
    }

    @Test(timeout = 4000)
    public void testMergeWithHasherLargeShape() {
        Shape shape = Shape.fromKM(2147483605, 2147483605);
        SimpleBloomFilter simpleBloomFilter = new SimpleBloomFilter(shape);
        EnhancedDoubleHasher hasher = new EnhancedDoubleHasher(2147483605, 2147483605);

        simpleBloomFilter.merge(hasher);
    }

    @Test(timeout = 4000)
    public void testMergeWithNullHasher() {
        Shape shape = Shape.fromKM(56, 56);
        SimpleBloomFilter simpleBloomFilter = new SimpleBloomFilter(shape);

        try {
            simpleBloomFilter.merge((Hasher) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("java.util.Objects", e);
        }
    }

    @Test(timeout = 4000)
    public void testMergeWithSelfAsBloomFilterLargeShape() {
        Shape shape = Shape.fromNM(Integer.MAX_VALUE, Integer.MAX_VALUE);
        SimpleBloomFilter simpleBloomFilter = new SimpleBloomFilter(shape);

        simpleBloomFilter.merge(simpleBloomFilter);
    }

    @Test(timeout = 4000)
    public void testMergeWithNullBloomFilter() {
        Shape shape = Shape.fromNM(38, 38);
        SimpleBloomFilter simpleBloomFilter = new SimpleBloomFilter(shape);

        try {
            simpleBloomFilter.merge((BloomFilter<?>) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("java.util.Objects", e);
        }
    }

    @Test(timeout = 4000)
    public void testMergeWithDifferentShapeBloomFilter() {
        Shape shape1 = Shape.fromNM(49, 49);
        Shape shape2 = Shape.fromKM(49, 5906);
        SimpleBloomFilter simpleBloomFilter1 = new SimpleBloomFilter(shape1);
        SimpleBloomFilter simpleBloomFilter2 = new SimpleBloomFilter(shape2);

        try {
            simpleBloomFilter1.merge(simpleBloomFilter2);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.apache.commons.collections4.bloomfilter.SimpleBloomFilter", e);
        }
    }

    @Test(timeout = 4000)
    public void testMergeWithSparseBloomFilterLargeShape() {
        Shape shape = Shape.fromNM(2147483602, 2147483602);
        SimpleBloomFilter simpleBloomFilter = new SimpleBloomFilter(shape);
        SparseBloomFilter sparseBloomFilter = new SparseBloomFilter(shape);

        simpleBloomFilter.merge(sparseBloomFilter);
    }

    @Test(timeout = 4000)
    public void testMergeWithNullBitMapExtractor() {
        Shape shape = Shape.fromKM(1, 1);
        SimpleBloomFilter simpleBloomFilter = new SimpleBloomFilter(shape);

        try {
            simpleBloomFilter.merge((BitMapExtractor) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("java.util.Objects", e);
        }
    }

    @Test(timeout = 4000)
    public void testMergeWithInvalidBitMapExtractor() {
        Shape shape = Shape.fromKM(20, 20);
        SimpleBloomFilter simpleBloomFilter = new SimpleBloomFilter(shape);
        BitMapExtractor bitMapExtractor = BitMapExtractor.fromIndexExtractor(simpleBloomFilter, 283);

        try {
            simpleBloomFilter.merge(bitMapExtractor);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.apache.commons.collections4.bloomfilter.SimpleBloomFilter", e);
        }
    }

    @Test(timeout = 4000)
    public void testIsEmptyAfterMergeWithHasher() {
        Shape shape = Shape.fromNM(Integer.MAX_VALUE, Integer.MAX_VALUE);
        SimpleBloomFilter simpleBloomFilter = new SimpleBloomFilter(shape);
        EnhancedDoubleHasher hasher = new EnhancedDoubleHasher(-154L, Integer.MAX_VALUE);

        simpleBloomFilter.merge(hasher);
        simpleBloomFilter.isEmpty();
    }

    @Test(timeout = 4000)
    public void testCopy() {
        Shape shape = Shape.fromNM(2147483605, 2147483605);
        SimpleBloomFilter simpleBloomFilter = new SimpleBloomFilter(shape);
        SimpleBloomFilter copiedFilter = simpleBloomFilter.copy();

        assertNotSame(copiedFilter, simpleBloomFilter);
    }

    @Test(timeout = 4000)
    public void testContainsWithCellExtractor() {
        Shape shape = Shape.fromNM(2147483605, 2147483605);
        SimpleBloomFilter simpleBloomFilter = new SimpleBloomFilter(shape);
        CellExtractor cellExtractor = CellExtractor.from(simpleBloomFilter);

        simpleBloomFilter.contains(cellExtractor);
    }

    @Test(timeout = 4000)
    public void testContainsWithNullIndexExtractor() {
        Shape shape = Shape.fromKM(668, 668);
        SimpleBloomFilter simpleBloomFilter = new SimpleBloomFilter(shape);

        try {
            simpleBloomFilter.contains((IndexExtractor) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.apache.commons.collections4.bloomfilter.SimpleBloomFilter", e);
        }
    }

    @Test(timeout = 4000)
    public void testContainsWithInvalidIndexExtractor() {
        Shape shape = Shape.fromNM(10, 10);
        SimpleBloomFilter simpleBloomFilter = new SimpleBloomFilter(shape);
        int[] indices = {977};
        IndexExtractor indexExtractor = IndexExtractor.fromIndexArray(indices);

        try {
            simpleBloomFilter.contains(indexExtractor);
            fail("Expecting exception: ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            verifyException("org.apache.commons.collections4.bloomfilter.BitMaps", e);
        }
    }

    @Test(timeout = 4000)
    public void testCardinalityAfterMergeWithHasherLargeShape() {
        Shape shape = Shape.fromNM(2147483605, 2147483605);
        SimpleBloomFilter simpleBloomFilter = new SimpleBloomFilter(shape);
        EnhancedDoubleHasher hasher = new EnhancedDoubleHasher(2147483605, 2147483605);

        simpleBloomFilter.merge(hasher);
        simpleBloomFilter.cardinality();
    }

    @Test(timeout = 4000)
    public void testConstructorWithNullShape() {
        try {
            new SimpleBloomFilter(null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("java.util.Objects", e);
        }
    }

    @Test(timeout = 4000)
    public void testMergeWithSelfAsBitMapExtractor() {
        Shape shape = Shape.fromNM(64, 64);
        LayerManager<SparseBloomFilter> layerManager = mockLayerManager(false);
        LayeredBloomFilter<SparseBloomFilter> layeredBloomFilter = new LayeredBloomFilter<>(shape, layerManager);
        SimpleBloomFilter simpleBloomFilter = layeredBloomFilter.flatten();

        assertTrue(simpleBloomFilter.merge(simpleBloomFilter));
    }

    @Test(timeout = 4000)
    public void testProcessBitMapPairsWithInvalidBitMapExtractor() {
        Shape shape = Shape.fromKM(28, 28);
        SimpleBloomFilter simpleBloomFilter = new SimpleBloomFilter(shape);
        BitMapExtractor bitMapExtractor = BitMapExtractor.fromIndexExtractor(simpleBloomFilter, -1);
        LongBiPredicate longBiPredicate = mockLongBiPredicate(false);

        assertFalse(simpleBloomFilter.processBitMapPairs(bitMapExtractor, longBiPredicate));
    }

    @Test(timeout = 4000)
    public void testProcessBitMapPairsWithSelf() {
        Shape shape = Shape.fromNM(294, 294);
        LayerManager<SparseBloomFilter> layerManager = mockLayerManager(false);
        LayeredBloomFilter<SparseBloomFilter> layeredBloomFilter = new LayeredBloomFilter<>(shape, layerManager);
        SimpleBloomFilter simpleBloomFilter = layeredBloomFilter.flatten();
        LongBiPredicate longBiPredicate = mockLongBiPredicate(false);

        assertFalse(simpleBloomFilter.processBitMapPairs(simpleBloomFilter, longBiPredicate));
    }

    @Test(timeout = 4000)
    public void testMergeWithInvalidHasher() {
        Shape shape = Shape.fromKM(655, 1);
        EnhancedDoubleHasher hasher = new EnhancedDoubleHasher(655, 655);
        SimpleBloomFilter simpleBloomFilter = new SimpleBloomFilter(shape);

        try {
            simpleBloomFilter.merge(hasher);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.apache.commons.collections4.bloomfilter.SimpleBloomFilter", e);
        }
    }

    @Test(timeout = 4000)
    public void testMergeWithInvalidIndexExtractorNegativeIndex() {
        Shape shape = Shape.fromNM(10, 10);
        SimpleBloomFilter simpleBloomFilter = new SimpleBloomFilter(shape);
        int[] indices = {-89};
        IndexExtractor indexExtractor = IndexExtractor.fromIndexArray(indices);

        try {
            simpleBloomFilter.merge(indexExtractor);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.apache.commons.collections4.bloomfilter.SimpleBloomFilter", e);
        }
    }

    @Test(timeout = 4000)
    public void testMergeWithSparseBloomFilter() {
        Shape shape = Shape.fromNM(2, 2);
        SimpleBloomFilter simpleBloomFilter = new SimpleBloomFilter(shape);
        SparseBloomFilter sparseBloomFilter = new SparseBloomFilter(shape);

        assertTrue(simpleBloomFilter.merge(sparseBloomFilter));
    }

    @Test(timeout = 4000)
    public void testMergeWithSelfAndCheckEmpty() {
        Shape shape = Shape.fromNMK(19, 19, 19);
        SimpleBloomFilter simpleBloomFilter = new SimpleBloomFilter(shape);

        assertTrue(simpleBloomFilter.merge(simpleBloomFilter));
        assertTrue(simpleBloomFilter.isEmpty());
    }

    @Test(timeout = 4000)
    public void testIsEmptyWithLayeredBloomFilter() {
        Shape shape = Shape.fromNM(294, 294);
        LayerManager<SparseBloomFilter> layerManager = mockLayerManager(false);
        LayeredBloomFilter<SparseBloomFilter> layeredBloomFilter = new LayeredBloomFilter<>(shape, layerManager);
        SimpleBloomFilter simpleBloomFilter = layeredBloomFilter.flatten();

        assertTrue(simpleBloomFilter.isEmpty());
    }

    @Test(timeout = 4000)
    public void testCardinalityAfterMergeWithSelf() {
        Shape shape = Shape.fromNMK(15, 15, 15);
        SimpleBloomFilter simpleBloomFilter = new SimpleBloomFilter(shape);

        assertTrue(simpleBloomFilter.merge(simpleBloomFilter));
        assertEquals(0, simpleBloomFilter.cardinality());
    }

    @Test(timeout = 4000)
    public void testAsBitMapArray() {
        Shape shape = Shape.fromNM(2147483605, 2147483605);
        SimpleBloomFilter simpleBloomFilter = new SimpleBloomFilter(shape);

        assertEquals(33554432, simpleBloomFilter.asBitMapArray().length);
    }

    @Test(timeout = 4000)
    public void testIsFullWithLayeredBloomFilter() {
        Shape shape = Shape.fromNM(294, 294);
        LayerManager<SparseBloomFilter> layerManager = mockLayerManager(false);
        LayeredBloomFilter<SparseBloomFilter> layeredBloomFilter = new LayeredBloomFilter<>(shape, layerManager);
        SimpleBloomFilter simpleBloomFilter = layeredBloomFilter.flatten();

        assertFalse(simpleBloomFilter.isFull());
    }

    @Test(timeout = 4000)
    public void testClear() {
        Shape shape = Shape.fromKM(23, 23);
        SimpleBloomFilter simpleBloomFilter = new SimpleBloomFilter(shape);

        simpleBloomFilter.clear();
        assertEquals(0, simpleBloomFilter.characteristics());
    }

    @Test(timeout = 4000)
    public void testProcessIndicesWithNegatedPredicate() {
        Shape shape = Shape.fromKM(19, 19);
        SimpleBloomFilter simpleBloomFilter = new SimpleBloomFilter(shape);
        EnhancedDoubleHasher hasher = new EnhancedDoubleHasher(1956L, -2388L);

        assertTrue(simpleBloomFilter.merge(hasher));
        IndexFilter.ArrayTracker arrayTracker = new IndexFilter.ArrayTracker(shape);
        IntPredicate negatedPredicate = arrayTracker.negate();

        assertFalse(simpleBloomFilter.processIndices(negatedPredicate));
    }

    @Test(timeout = 4000)
    public void testContainsWithSelf() {
        Shape shape = Shape.fromNM(294, 294);
        LayerManager<SparseBloomFilter> layerManager = mockLayerManager(false);
        LayeredBloomFilter<SparseBloomFilter> layeredBloomFilter = new LayeredBloomFilter<>(shape, layerManager);
        SimpleBloomFilter simpleBloomFilter = layeredBloomFilter.flatten();

        assertTrue(simpleBloomFilter.contains(simpleBloomFilter));
    }

    @Test(timeout = 4000)
    public void testCopyWithSimpleBloomFilter() {
        Shape shape = Shape.fromNMK(78, 78, 2);
        SimpleBloomFilter simpleBloomFilter = new SimpleBloomFilter(shape);
        SimpleBloomFilter copiedFilter = simpleBloomFilter.copy();

        assertNotSame(simpleBloomFilter, copiedFilter);
    }

    private LayerManager<SparseBloomFilter> mockLayerManager(boolean... returns) {
        LayerManager<SparseBloomFilter> layerManager = mock(LayerManager.class, new ViolatedAssumptionAnswer());
        when(layerManager.processBloomFilters(any())).thenReturn(returns[0], returns);
        return layerManager;
    }

    private LongBiPredicate mockLongBiPredicate(boolean... returns) {
        LongBiPredicate longBiPredicate = mock(LongBiPredicate.class, new ViolatedAssumptionAnswer());
        when(longBiPredicate.test(anyLong(), anyLong())).thenReturn(returns[0], returns);
        return longBiPredicate;
    }
}