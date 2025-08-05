package org.apache.commons.collections4.bloomfilter;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import java.util.function.IntPredicate;
import java.util.function.LongPredicate;
import org.apache.commons.collections4.bloomfilter.*;

/**
 * Test suite for SimpleBloomFilter functionality.
 * Tests cover basic operations, merging, validation, and edge cases.
 */
public class SimpleBloomFilterTest {

    // Test Data Setup
    private static final int SMALL_FILTER_SIZE = 10;
    private static final int MEDIUM_FILTER_SIZE = 64;
    private static final int LARGE_FILTER_SIZE = 1540;

    // Constructor Tests
    
    @Test(expected = NullPointerException.class)
    public void constructor_WithNullShape_ThrowsNullPointerException() {
        new SimpleBloomFilter(null);
    }

    @Test
    public void constructor_WithValidShape_CreatesEmptyFilter() {
        Shape shape = Shape.fromNM(SMALL_FILTER_SIZE, SMALL_FILTER_SIZE);
        SimpleBloomFilter filter = new SimpleBloomFilter(shape);
        
        assertTrue("New filter should be empty", filter.isEmpty());
        assertEquals("New filter should have zero cardinality", 0, filter.cardinality());
        assertFalse("New filter should not be full", filter.isFull());
    }

    // Basic Operations Tests

    @Test
    public void getShape_ReturnsCorrectShape() {
        Shape expectedShape = Shape.fromNM(MEDIUM_FILTER_SIZE, MEDIUM_FILTER_SIZE);
        SimpleBloomFilter filter = new SimpleBloomFilter(expectedShape);
        
        Shape actualShape = filter.getShape();
        assertEquals("Shape should match constructor parameter", expectedShape, actualShape);
    }

    @Test
    public void characteristics_ReturnsZero() {
        Shape shape = Shape.fromKM(4, 4);
        SimpleBloomFilter filter = new SimpleBloomFilter(shape);
        
        assertEquals("Characteristics should return 0", 0, filter.characteristics());
    }

    @Test
    public void clear_EmptiesFilter() {
        Shape shape = Shape.fromKM(23, 23);
        SimpleBloomFilter filter = new SimpleBloomFilter(shape);
        
        // Add some data first
        EnhancedDoubleHasher hasher = new EnhancedDoubleHasher(1L, 2L);
        filter.merge(hasher);
        assertFalse("Filter should not be empty after merge", filter.isEmpty());
        
        // Clear and verify
        filter.clear();
        assertTrue("Filter should be empty after clear", filter.isEmpty());
    }

    @Test
    public void copy_CreatesIndependentCopy() {
        Shape shape = Shape.fromNMK(78, 78, 2);
        SimpleBloomFilter original = new SimpleBloomFilter(shape);
        
        SimpleBloomFilter copy = original.copy();
        
        assertNotSame("Copy should be different instance", original, copy);
        assertEquals("Copy should have same shape", original.getShape(), copy.getShape());
        assertEquals("Copy should have same cardinality", original.cardinality(), copy.cardinality());
    }

    // Merge Operations Tests

    @Test
    public void merge_WithHasher_UpdatesFilter() {
        Shape shape = Shape.fromNMK(78, 78, 2);
        SimpleBloomFilter filter = new SimpleBloomFilter(shape);
        EnhancedDoubleHasher hasher = new EnhancedDoubleHasher(1L, -9223372036854775808L);
        
        boolean changed = filter.merge(hasher);
        
        assertTrue("Merge should return true when filter changes", changed);
        assertFalse("Filter should not be empty after merge", filter.isEmpty());
        assertEquals("Filter should have expected cardinality", 2, filter.cardinality());
    }

    @Test(expected = NullPointerException.class)
    public void merge_WithNullHasher_ThrowsNullPointerException() {
        Shape shape = Shape.fromKM(56, 56);
        SimpleBloomFilter filter = new SimpleBloomFilter(shape);
        
        filter.merge((Hasher) null);
    }

    @Test
    public void merge_WithBloomFilter_CombinesFilters() {
        Shape shape = Shape.fromNM(2, 2);
        SimpleBloomFilter filter1 = new SimpleBloomFilter(shape);
        SparseBloomFilter filter2 = new SparseBloomFilter(shape);
        
        boolean changed = filter1.merge(filter2);
        
        assertTrue("Merge should return true", changed);
    }

    @Test(expected = NullPointerException.class)
    public void merge_WithNullBloomFilter_ThrowsNullPointerException() {
        Shape shape = Shape.fromNM(38, 38);
        SimpleBloomFilter filter = new SimpleBloomFilter(shape);
        
        filter.merge((BloomFilter<?>) null);
    }

    @Test
    public void merge_WithSelf_ReturnsTrue() {
        Shape shape = Shape.fromKM(22, 22);
        SimpleBloomFilter filter = new SimpleBloomFilter(shape);
        
        boolean changed = filter.merge(filter);
        
        assertTrue("Merging with self should return true", changed);
    }

    @Test
    public void merge_WithBitMapExtractor_UpdatesFilter() {
        Shape shape = Shape.fromNM(MEDIUM_FILTER_SIZE, MEDIUM_FILTER_SIZE);
        SimpleBloomFilter filter = new SimpleBloomFilter(shape);
        
        boolean changed = filter.merge((BitMapExtractor) filter);
        
        assertTrue("Merge should return true", changed);
    }

    @Test(expected = NullPointerException.class)
    public void merge_WithNullBitMapExtractor_ThrowsNullPointerException() {
        Shape shape = Shape.fromKM(1, 1);
        SimpleBloomFilter filter = new SimpleBloomFilter(shape);
        
        filter.merge((BitMapExtractor) null);
    }

    // Validation Tests

    @Test(expected = IllegalArgumentException.class)
    public void merge_WithIndexOutOfRange_ThrowsIllegalArgumentException() {
        Shape shape = Shape.fromKM(1, LARGE_FILTER_SIZE);
        SimpleBloomFilter filter = createEmptyLayeredFilter(shape);
        
        int[] invalidIndices = {5023, 0, 0, 0, 0, 0, 0, 0}; // 5023 is out of range [0, 1540)
        IndexExtractor indexExtractor = IndexExtractor.fromIndexArray(invalidIndices);
        
        filter.merge(indexExtractor);
    }

    @Test(expected = IllegalArgumentException.class)
    public void merge_WithNegativeIndex_ThrowsIllegalArgumentException() {
        Shape shape = Shape.fromNM(SMALL_FILTER_SIZE, SMALL_FILTER_SIZE);
        SimpleBloomFilter filter = new SimpleBloomFilter(shape);
        
        int[] invalidIndices = {0, 0, -89, 0, 0}; // -89 is negative
        IndexExtractor indexExtractor = IndexExtractor.fromIndexArray(invalidIndices);
        
        filter.merge(indexExtractor);
    }

    @Test(expected = IllegalArgumentException.class)
    public void merge_WithTooManyBitMaps_ThrowsIllegalArgumentException() {
        Shape shape1 = Shape.fromNM(49, 49);
        Shape shape2 = Shape.fromKM(49, 5906); // Creates more bit maps
        
        SimpleBloomFilter filter1 = new SimpleBloomFilter(shape1);
        SimpleBloomFilter filter2 = new SimpleBloomFilter(shape2);
        
        filter1.merge(filter2);
    }

    // Processing Tests

    @Test
    public void processIndices_WithValidPredicate_ReturnsTrue() {
        Shape shape = Shape.fromNMK(78, 78, 2);
        SimpleBloomFilter filter = new SimpleBloomFilter(shape);
        IndexFilter.ArrayTracker tracker = new IndexFilter.ArrayTracker(shape);
        
        boolean result = filter.processIndices(tracker);
        
        assertTrue("Processing indices should return true", result);
    }

    @Test(expected = NullPointerException.class)
    public void processIndices_WithNullPredicate_ThrowsNullPointerException() {
        Shape shape = Shape.fromNM(38, 38);
        SimpleBloomFilter filter = new SimpleBloomFilter(shape);
        
        filter.processIndices((IntPredicate) null);
    }

    @Test
    public void processBitMaps_WithValidPredicate_ProcessesCorrectly() {
        Shape shape = Shape.fromNM(SMALL_FILTER_SIZE, SMALL_FILTER_SIZE);
        SimpleBloomFilter filter = new SimpleBloomFilter(shape);
        
        LongPredicate mockPredicate = mock(LongPredicate.class);
        when(mockPredicate.test(anyLong())).thenReturn(false);
        
        boolean result = filter.processBitMaps(mockPredicate);
        
        assertFalse("Processing should return false when predicate returns false", result);
    }

    @Test(expected = NullPointerException.class)
    public void processBitMaps_WithNullPredicate_ThrowsNullPointerException() {
        Shape shape = Shape.fromKM(1, 1);
        SimpleBloomFilter filter = new SimpleBloomFilter(shape);
        
        filter.processBitMaps((LongPredicate) null);
    }

    // Contains Tests

    @Test
    public void contains_EmptyFilterContainsItself_ReturnsTrue() {
        Shape shape = Shape.fromNM(294, 294);
        SimpleBloomFilter filter = createEmptyLayeredFilter(shape);
        
        boolean contains = filter.contains(filter);
        
        assertTrue("Empty filter should contain itself", contains);
    }

    @Test
    public void contains_WithDifferentFilter_ReturnsFalse() {
        Shape shape = Shape.fromNMK(MEDIUM_FILTER_SIZE, MEDIUM_FILTER_SIZE, 1);
        SimpleBloomFilter filter1 = new SimpleBloomFilter(shape);
        LayeredBloomFilter<SparseBloomFilter> filter2 = createEmptyLayeredBloomFilter(shape);
        
        boolean contains = filter1.contains(filter2);
        
        assertFalse("Different filters should not contain each other", contains);
    }

    @Test(expected = NullPointerException.class)
    public void contains_WithNullIndexExtractor_ThrowsNullPointerException() {
        Shape shape = Shape.fromKM(668, 668);
        SimpleBloomFilter filter = new SimpleBloomFilter(shape);
        
        filter.contains((IndexExtractor) null);
    }

    // State Tests

    @Test
    public void isEmpty_NewFilter_ReturnsTrue() {
        Shape shape = Shape.fromNM(294, 294);
        SimpleBloomFilter filter = createEmptyLayeredFilter(shape);
        
        assertTrue("New filter should be empty", filter.isEmpty());
    }

    @Test
    public void isEmpty_AfterMerge_ReturnsFalse() {
        Shape shape = Shape.fromNM(38, 38);
        SimpleBloomFilter filter = new SimpleBloomFilter(shape);
        EnhancedDoubleHasher hasher = new EnhancedDoubleHasher(38, 38);
        
        filter.merge(hasher);
        
        assertFalse("Filter should not be empty after merge", filter.isEmpty());
        assertEquals("Filter should have cardinality of 1", 1, filter.cardinality());
    }

    @Test
    public void isFull_EmptyFilter_ReturnsFalse() {
        Shape shape = Shape.fromNM(294, 294);
        SimpleBloomFilter filter = createEmptyLayeredFilter(shape);
        
        assertFalse("Empty filter should not be full", filter.isFull());
    }

    @Test
    public void asBitMapArray_ReturnsCorrectSize() {
        Shape shape = Shape.fromNM(2147483605, 2147483605);
        SimpleBloomFilter filter = new SimpleBloomFilter(shape);
        
        long[] bitMapArray = filter.asBitMapArray();
        
        assertEquals("Bit map array should have expected length", 33554432, bitMapArray.length);
    }

    // Helper Methods

    private SimpleBloomFilter createEmptyLayeredFilter(Shape shape) {
        LayerManager<SparseBloomFilter> mockManager = mock(LayerManager.class);
        when(mockManager.processBloomFilters(any())).thenReturn(false);
        
        LayeredBloomFilter<SparseBloomFilter> layered = 
            new LayeredBloomFilter<>(shape, mockManager);
        return layered.flatten();
    }

    private LayeredBloomFilter<SparseBloomFilter> createEmptyLayeredBloomFilter(Shape shape) {
        LayerManager<SparseBloomFilter> mockManager = mock(LayerManager.class);
        when(mockManager.processBloomFilters(any())).thenReturn(false);
        
        return new LayeredBloomFilter<>(shape, mockManager);
    }

    // Integration Tests

    @Test
    public void integration_MergeAndProcess_WorksTogether() {
        Shape shape = Shape.fromKM(19, 19);
        SimpleBloomFilter filter = new SimpleBloomFilter(shape);
        SparseBloomFilter sparseFilter = new SparseBloomFilter(shape);
        EnhancedDoubleHasher hasher = new EnhancedDoubleHasher(19, 2654L);
        
        // Merge hasher
        boolean changed1 = filter.merge(hasher);
        assertTrue("First merge should change filter", changed1);
        
        // Merge sparse filter
        boolean changed2 = filter.merge(sparseFilter);
        assertTrue("Second merge should change filter", changed2);
        
        // Verify final state
        assertFalse("Filter should not be empty", filter.isEmpty());
        assertTrue("Filter cardinality should be positive", filter.cardinality() > 0);
    }

    @Test
    public void integration_ComplexMergeScenario_HandlesCorrectly() {
        Shape shape = Shape.fromNMK(115, 115, 2);
        SimpleBloomFilter filter = new SimpleBloomFilter(shape);
        EnhancedDoubleHasher hasher = new EnhancedDoubleHasher(1L, -9223372036854775808L);
        
        boolean changed = filter.merge(hasher);
        assertTrue("Merge should change filter", changed);
        
        filter.isFull(); // This call is part of the original test
        int cardinality = filter.cardinality();
        assertEquals("Cardinality should match expected value", 2, cardinality);
    }
}