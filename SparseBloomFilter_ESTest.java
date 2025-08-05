package org.apache.commons.collections4.bloomfilter;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.function.IntPredicate;
import java.util.function.LongPredicate;
import org.apache.commons.collections4.bloomfilter.*;

/**
 * Test suite for SparseBloomFilter functionality.
 * Tests cover basic operations, merging, containment checks, and error conditions.
 */
public class SparseBloomFilterTest {

    // Test Data Constants
    private static final int SMALL_SIZE = 22;
    private static final int MEDIUM_SIZE = 64;
    private static final int LARGE_SIZE = 994;
    
    // ========== Constructor Tests ==========
    
    @Test(expected = NullPointerException.class)
    public void constructor_WithNullShape_ThrowsNullPointerException() {
        new SparseBloomFilter(null);
    }
    
    @Test
    public void constructor_WithValidShape_CreatesEmptyFilter() {
        Shape shape = Shape.fromKM(MEDIUM_SIZE, MEDIUM_SIZE);
        SparseBloomFilter filter = new SparseBloomFilter(shape);
        
        assertTrue("New filter should be empty", filter.isEmpty());
        assertEquals("New filter should have zero cardinality", 0, filter.cardinality());
        assertEquals("Filter should have expected characteristics", 1, filter.characteristics());
    }

    // ========== Basic Property Tests ==========
    
    @Test
    public void getShape_ReturnsCorrectShape() {
        Shape expectedShape = Shape.fromNMK(SMALL_SIZE, SMALL_SIZE, SMALL_SIZE);
        SparseBloomFilter filter = new SparseBloomFilter(expectedShape);
        
        Shape actualShape = filter.getShape();
        assertEquals("Shape should match constructor parameter", 
                     expectedShape.estimateMaxN(), actualShape.estimateMaxN(), 0.01);
    }
    
    @Test
    public void isEmpty_OnNewFilter_ReturnsTrue() {
        Shape shape = Shape.fromKM(LARGE_SIZE, LARGE_SIZE);
        SparseBloomFilter filter = new SparseBloomFilter(shape);
        
        assertTrue("New filter should be empty", filter.isEmpty());
    }
    
    @Test
    public void cardinality_OnEmptyFilter_ReturnsZero() {
        Shape shape = Shape.fromKM(LARGE_SIZE * 3, LARGE_SIZE * 3);
        SparseBloomFilter filter = new SparseBloomFilter(shape);
        
        assertEquals("Empty filter should have zero cardinality", 0, filter.cardinality());
    }
    
    @Test
    public void cardinality_AfterMergeWithHasher_ReturnsOne() {
        Shape shape = Shape.fromNM(MEDIUM_SIZE, MEDIUM_SIZE);
        SparseBloomFilter filter = new SparseBloomFilter(shape);
        EnhancedDoubleHasher hasher = new EnhancedDoubleHasher(new byte[6]);
        
        filter.merge(hasher);
        
        assertEquals("Filter with one merge should have cardinality 1", 1, filter.cardinality());
        assertFalse("Filter should no longer be empty", filter.isEmpty());
    }

    // ========== Merge Operation Tests ==========
    
    @Test(expected = NullPointerException.class)
    public void merge_WithNullHasher_ThrowsNullPointerException() {
        Shape shape = Shape.fromNM(10, 10);
        SparseBloomFilter filter = new SparseBloomFilter(shape);
        
        filter.merge((Hasher) null);
    }
    
    @Test
    public void merge_WithValidHasher_ReturnsTrue() {
        Shape shape = Shape.fromKM(SMALL_SIZE, SMALL_SIZE);
        SparseBloomFilter filter = new SparseBloomFilter(shape);
        EnhancedDoubleHasher hasher = new EnhancedDoubleHasher(-394L, -735L);
        
        boolean result = filter.merge(hasher);
        
        assertTrue("Merge with valid hasher should return true", result);
    }
    
    @Test(expected = NullPointerException.class)
    public void merge_WithNullBloomFilter_ThrowsNullPointerException() {
        Shape shape = Shape.fromKM(18, 18);
        SparseBloomFilter filter = new SparseBloomFilter(shape);
        
        filter.merge((BloomFilter<?>) null);
    }
    
    @Test
    public void merge_WithSelf_ReturnsTrue() {
        Shape shape = Shape.fromKM(LARGE_SIZE, LARGE_SIZE);
        SparseBloomFilter filter = new SparseBloomFilter(shape);
        
        boolean result = filter.merge(filter);
        
        assertTrue("Merging filter with itself should return true", result);
    }
    
    @Test(expected = NullPointerException.class)
    public void merge_WithNullIndexExtractor_ThrowsNullPointerException() {
        Shape shape = Shape.fromNM(1014, 1014);
        SparseBloomFilter filter = new SparseBloomFilter(shape);
        
        filter.merge((IndexExtractor) null);
    }
    
    @Test
    public void merge_WithValidIndexArray_ReturnsTrue() {
        Shape shape = Shape.fromNM(989, 2268);
        SparseBloomFilter filter = new SparseBloomFilter(shape);
        int[] indices = new int[2]; // Contains zeros, which are valid indices
        IndexExtractor indexExtractor = IndexExtractor.fromIndexArray(indices);
        
        boolean result = filter.merge(indexExtractor);
        
        assertTrue("Merge with valid indices should return true", result);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void merge_WithIndexTooLarge_ThrowsIllegalArgumentException() {
        Shape shape = Shape.fromNMK(12, 12, 12); // Max valid index is 11
        SparseBloomFilter filter = new SparseBloomFilter(shape);
        int[] indices = {12}; // Index 12 is too large
        IndexExtractor indexExtractor = IndexExtractor.fromIndexArray(indices);
        
        filter.merge(indexExtractor);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void merge_WithNegativeIndex_ThrowsIllegalArgumentException() {
        Shape shape = Shape.fromKM(LARGE_SIZE, LARGE_SIZE);
        SparseBloomFilter filter = new SparseBloomFilter(shape);
        int[] indices = {0, 0, 0, -1}; // Negative index is invalid
        IndexExtractor indexExtractor = IndexExtractor.fromIndexArray(indices);
        
        filter.merge(indexExtractor);
    }
    
    @Test(expected = NullPointerException.class)
    public void merge_WithNullBitMapExtractor_ThrowsNullPointerException() {
        Shape shape = Shape.fromKM(989, 989);
        SparseBloomFilter filter = new SparseBloomFilter(shape);
        
        filter.merge((BitMapExtractor) null);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void merge_WithInvalidBitMapValue_ThrowsIllegalArgumentException() {
        Shape shape = Shape.fromKM(3, 3); // Small shape for easy testing
        SparseBloomFilter filter = new SparseBloomFilter(shape);
        long[] bitmaps = new long[3];
        bitmaps[2] = 3L; // This will generate indices that are too large
        BitMapExtractor bitMapExtractor = BitMapExtractor.fromBitMapArray(bitmaps);
        
        filter.merge(bitMapExtractor);
    }

    // ========== Contains Operation Tests ==========
    
    @Test(expected = NullPointerException.class)
    public void contains_WithNullIndexExtractor_ThrowsNullPointerException() {
        Shape shape = Shape.fromNM(1014, 1014);
        SparseBloomFilter filter = new SparseBloomFilter(shape);
        
        filter.contains((IndexExtractor) null);
    }
    
    @Test
    public void contains_WithEmptyIndexArray_ReturnsFalse() {
        Shape shape = Shape.fromNM(MEDIUM_SIZE, MEDIUM_SIZE);
        SparseBloomFilter filter = new SparseBloomFilter(shape);
        int[] indices = new int[4]; // All zeros
        IndexExtractor indexExtractor = IndexExtractor.fromIndexArray(indices);
        
        boolean result = filter.contains(indexExtractor);
        
        assertFalse("Empty filter should not contain any indices", result);
    }
    
    @Test
    public void contains_WithSelf_ReturnsTrue() {
        Shape shape = Shape.fromKM(989, 989);
        SparseBloomFilter filter = new SparseBloomFilter(shape);
        
        boolean result = filter.contains(filter);
        
        assertTrue("Filter should always contain itself", result);
    }
    
    @Test(expected = NullPointerException.class)
    public void contains_WithNullBitMapExtractor_ThrowsNullPointerException() {
        Shape shape = Shape.fromKM(989, 989);
        SparseBloomFilter filter = new SparseBloomFilter(shape);
        
        filter.contains((BitMapExtractor) null);
    }
    
    @Test
    public void contains_EmptyFilterWithAnotherEmptyFilter_ReturnsFalse() {
        Shape shape = Shape.fromKM(SMALL_SIZE, SMALL_SIZE);
        SparseBloomFilter filter1 = new SparseBloomFilter(shape);
        SparseBloomFilter filter2 = new SparseBloomFilter(shape);
        EnhancedDoubleHasher hasher = new EnhancedDoubleHasher(-394L, -735L);
        filter1.merge(hasher);
        
        boolean result = filter2.contains(filter1);
        
        assertFalse("Empty filter should not contain non-empty filter", result);
    }

    // ========== Process Operations Tests ==========
    
    @Test(expected = NullPointerException.class)
    public void processIndices_WithNullPredicate_ThrowsNullPointerException() {
        Shape shape = Shape.fromKM(1004, 1004);
        SparseBloomFilter filter = new SparseBloomFilter(shape);
        
        filter.processIndices((IntPredicate) null);
    }
    
    @Test
    public void processIndices_WithBitMapTracker_ReturnsTrue() {
        Shape shape = Shape.fromKM(2268, 64);
        SparseBloomFilter filter = new SparseBloomFilter(shape);
        IndexFilter.BitMapTracker tracker = new IndexFilter.BitMapTracker(shape);
        
        boolean result = filter.processIndices(tracker);
        
        assertTrue("Processing indices should return true", result);
    }
    
    @Test
    public void processIndices_WithNegatedPredicate_ReturnsFalse() {
        Shape shape = Shape.fromNM(SMALL_SIZE, SMALL_SIZE);
        SparseBloomFilter filter = new SparseBloomFilter(shape);
        EnhancedDoubleHasher hasher = new EnhancedDoubleHasher(-1158L, -613L);
        filter.merge(hasher);
        
        IndexFilter.ArrayTracker tracker = new IndexFilter.ArrayTracker(shape);
        IntPredicate negatedPredicate = tracker.negate();
        boolean result = filter.processIndices(negatedPredicate);
        
        assertFalse("Negated predicate should return false", result);
    }
    
    @Test(expected = NullPointerException.class)
    public void processBitMaps_WithNullPredicate_ThrowsNullPointerException() {
        Shape shape = Shape.fromNM(1014, 1014);
        SparseBloomFilter filter = new SparseBloomFilter(shape);
        
        filter.processBitMaps((LongPredicate) null);
    }

    // ========== Utility Operations Tests ==========
    
    @Test
    public void copy_CreatesIndependentCopy() {
        Shape shape = Shape.fromKM(LARGE_SIZE, LARGE_SIZE);
        SparseBloomFilter original = new SparseBloomFilter(shape);
        
        SparseBloomFilter copy = original.copy();
        
        assertEquals("Copy should have same characteristics", 
                     original.characteristics(), copy.characteristics());
        assertTrue("Copy should be empty like original", copy.isEmpty());
    }
    
    @Test
    public void clear_MakesFilterEmpty() {
        Shape shape = Shape.fromKM(1045, 1045);
        SparseBloomFilter filter = new SparseBloomFilter(shape);
        
        filter.clear(); // Should work even on empty filter
        
        assertEquals("Cleared filter should have expected characteristics", 
                     1, filter.characteristics());
    }
    
    @Test
    public void asBitMapArray_WithPopulatedFilter_ReturnsCorrectArray() {
        Shape shape = Shape.fromNM(1618, 1618);
        SparseBloomFilter filter = new SparseBloomFilter(shape);
        EnhancedDoubleHasher hasher = new EnhancedDoubleHasher(1618, 1618);
        filter.merge(hasher);
        
        long[] bitMapArray = filter.asBitMapArray();
        
        assertEquals("BitMap array should have expected length", 26, bitMapArray.length);
    }

    // ========== Integration Tests ==========
    
    @Test
    public void integrationTest_MergeAndContains() {
        Shape shape = Shape.fromNM(9, 9);
        SparseBloomFilter filter = new SparseBloomFilter(shape);
        EnhancedDoubleHasher hasher = new EnhancedDoubleHasher(0L, 9);
        
        boolean mergeResult = filter.merge(hasher);
        assertTrue("Merge should succeed", mergeResult);
        
        // Test with ArrayCountingBloomFilter
        ArrayCountingBloomFilter countingFilter = new ArrayCountingBloomFilter(shape);
        int maxInsert = countingFilter.getMaxInsert(filter);
        assertEquals("Max insert should be 0", 0, maxInsert);
    }
    
    @Test
    public void integrationTest_MergeWithDifferentFilters() {
        Shape shape = Shape.fromKM(1045, 1045);
        SparseBloomFilter sparseFilter = new SparseBloomFilter(shape);
        EnhancedDoubleHasher hasher = new EnhancedDoubleHasher(997L, -1591L);
        sparseFilter.merge(hasher);
        
        SimpleBloomFilter simpleFilter = new SimpleBloomFilter(shape);
        boolean containsResult = simpleFilter.contains(sparseFilter);
        
        assertFalse("Simple filter should not contain sparse filter elements", containsResult);
    }
    
    @Test
    public void integrationTest_SelfMergeAndContains() {
        Shape shape = Shape.fromKM(LARGE_SIZE, LARGE_SIZE);
        SparseBloomFilter filter = new SparseBloomFilter(shape);
        EnhancedDoubleHasher hasher = new EnhancedDoubleHasher(new byte[4]);
        filter.merge(hasher);
        
        boolean mergeResult = filter.merge(filter);
        assertTrue("Self-merge should succeed", mergeResult);
        
        boolean containsResult = filter.contains(filter);
        assertTrue("Filter should contain itself", containsResult);
    }

    // ========== Error Condition Tests ==========
    
    @Test(expected = IllegalArgumentException.class)
    public void errorCondition_HasherWithInvalidRange() {
        Shape shape = Shape.fromKM(3130, 1); // Very restrictive shape
        EnhancedDoubleHasher hasher = new EnhancedDoubleHasher(3130, 3130);
        SparseBloomFilter filter = new SparseBloomFilter(shape);
        
        filter.merge(hasher); // Should throw due to index out of range
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void errorCondition_MergeWithIncompatibleShape() {
        // Create filter with large shape
        Shape largeShape = Shape.fromKM(LARGE_SIZE, LARGE_SIZE);
        SparseBloomFilter largeFilter = new SparseBloomFilter(largeShape);
        EnhancedDoubleHasher hasher = new EnhancedDoubleHasher(new byte[4]);
        largeFilter.merge(hasher);
        
        // Create filter with small shape
        Shape smallShape = Shape.fromNM(1, 408);
        SparseBloomFilter smallFilter = new SparseBloomFilter(smallShape);
        
        // This should fail because indices from large filter exceed small filter's range
        smallFilter.merge(largeFilter);
    }
}