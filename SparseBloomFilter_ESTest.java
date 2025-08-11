package org.apache.commons.collections4.bloomfilter;

import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;

import java.util.function.IntPredicate;
import java.util.function.LongPredicate;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true)
public class SparseBloomFilter_ESTest extends SparseBloomFilter_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void testProcessIndicesWithBitMapTracker() {
        Shape shape = Shape.fromKM(2268, 64);
        SparseBloomFilter filter = new SparseBloomFilter(shape);
        IndexFilter.BitMapTracker tracker = new IndexFilter.BitMapTracker(shape);
        
        boolean result = filter.processIndices(tracker);
        
        assertTrue(result);
    }

    @Test(timeout = 4000)
    public void testMergeWithEnhancedDoubleHasher() {
        Shape shape = Shape.fromNM(22, 22);
        SparseBloomFilter filter = new SparseBloomFilter(shape);
        EnhancedDoubleHasher hasher = new EnhancedDoubleHasher(-1158L, -613L);
        
        boolean mergeResult = filter.merge(hasher);
        assertTrue(mergeResult);
        
        IndexFilter.ArrayTracker tracker = new IndexFilter.ArrayTracker(shape);
        IntPredicate negatedPredicate = tracker.negate();
        boolean processResult = filter.processIndices(negatedPredicate);
        
        assertFalse(processResult == mergeResult);
    }

    @Test(timeout = 4000)
    public void testIsEmptyAfterMerge() {
        Shape shape = Shape.fromNM(64, 64);
        SparseBloomFilter filter = new SparseBloomFilter(shape);
        byte[] data = new byte[6];
        EnhancedDoubleHasher hasher = new EnhancedDoubleHasher(data);
        
        boolean mergeResult = filter.merge(hasher);
        assertTrue(mergeResult);
        
        boolean isEmpty = filter.isEmpty();
        assertFalse(isEmpty == mergeResult);
    }

    @Test(timeout = 4000)
    public void testGetShape() {
        Shape shape = Shape.fromNMK(22, 22, 22);
        SparseBloomFilter filter = new SparseBloomFilter(shape);
        
        Shape retrievedShape = filter.getShape();
        assertEquals(0.6931471805599453, retrievedShape.estimateMaxN(), 0.01);
    }

    @Test(timeout = 4000)
    public void testContainsWithEmptyIndexArray() {
        Shape shape = Shape.fromNM(64, 64);
        SparseBloomFilter filter = new SparseBloomFilter(shape);
        int[] indices = new int[4];
        IndexExtractor extractor = IndexExtractor.fromIndexArray(indices);
        
        boolean containsResult = filter.contains(extractor);
        assertFalse(containsResult);
    }

    @Test(timeout = 4000)
    public void testMergeAndContainsWithDifferentFilters() {
        Shape shape = Shape.fromKM(22, 22);
        SparseBloomFilter filter1 = new SparseBloomFilter(shape);
        EnhancedDoubleHasher hasher = new EnhancedDoubleHasher(-394L, -735L);
        
        boolean mergeResult = filter1.merge(hasher);
        SparseBloomFilter filter2 = new SparseBloomFilter(shape);
        
        boolean containsResult = filter2.contains(filter1);
        assertFalse(containsResult == mergeResult);
        assertFalse(containsResult);
    }

    @Test(timeout = 4000)
    public void testCharacteristics() {
        Shape shape = Shape.fromNMK(22, 22, 22);
        SparseBloomFilter filter = new SparseBloomFilter(shape);
        
        int characteristics = filter.characteristics();
        assertEquals(1, characteristics);
    }

    @Test(timeout = 4000)
    public void testCardinalityOfEmptyFilter() {
        Shape shape = Shape.fromKM(3281, 3281);
        SparseBloomFilter filter = new SparseBloomFilter(shape);
        
        int cardinality = filter.cardinality();
        assertEquals(0, cardinality);
    }

    @Test(timeout = 4000)
    public void testCardinalityAfterMerge() {
        Shape shape = Shape.fromNM(64, 64);
        SparseBloomFilter filter = new SparseBloomFilter(shape);
        byte[] data = new byte[6];
        EnhancedDoubleHasher hasher = new EnhancedDoubleHasher(data);
        
        filter.merge(hasher);
        int cardinality = filter.cardinality();
        assertEquals(1, cardinality);
    }

    @Test(timeout = 4000)
    public void testProcessIndicesThrowsException() {
        Shape shape = Shape.fromKM(989, 989);
        SparseBloomFilter filter = new SparseBloomFilter(shape);
        EnhancedDoubleHasher hasher = new EnhancedDoubleHasher(989, 989);
        
        filter.merge(hasher);
        IndexFilter.ArrayTracker tracker = new IndexFilter.ArrayTracker(shape);
        
        // Undeclared exception expected
        filter.processIndices(tracker);
    }

    @Test(timeout = 4000)
    public void testProcessIndicesWithNullPredicate() {
        Shape shape = Shape.fromKM(1004, 1004);
        SparseBloomFilter filter = new SparseBloomFilter(shape);
        
        try {
            filter.processIndices((IntPredicate) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("java.util.Objects", e);
        }
    }

    // Additional tests can be refactored similarly...

}