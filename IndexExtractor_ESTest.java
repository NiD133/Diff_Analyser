package org.apache.commons.collections4.bloomfilter;

import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.collections4.bloomfilter.ArrayCountingBloomFilter;
import org.apache.commons.collections4.bloomfilter.BitMapExtractor;
import org.apache.commons.collections4.bloomfilter.EnhancedDoubleHasher;
import org.apache.commons.collections4.bloomfilter.IndexExtractor;
import org.apache.commons.collections4.bloomfilter.Shape;
import org.apache.commons.collections4.bloomfilter.SimpleBloomFilter;
import org.apache.commons.collections4.bloomfilter.SparseBloomFilter;

/**
 * Test suite for IndexExtractor functionality.
 * Tests cover creation methods, error handling, and core operations.
 */
public class IndexExtractorTest {

    // ========== Factory Method Tests ==========

    @Test
    public void fromIndexArray_withNullArray_shouldThrowNullPointerException() {
        IndexExtractor extractor = IndexExtractor.fromIndexArray((int[]) null);
        
        try {
            extractor.asIndexArray();
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected behavior
        }
    }

    @Test
    public void fromBitMapExtractor_withNullExtractor_shouldThrowNullPointerException() {
        try {
            IndexExtractor.fromBitMapExtractor(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            assertEquals("bitMapExtractor", e.getMessage());
        }
    }

    @Test
    public void fromBitMapExtractor_withValidBitMap_shouldCreateCorrectIndexArray() {
        // Create bitmap with all bits set in the third long (index 2)
        long[] bitMapArray = new long[3];
        bitMapArray[2] = -1L; // All 64 bits set
        
        BitMapExtractor bitMapExtractor = BitMapExtractor.fromBitMapArray(bitMapArray);
        IndexExtractor indexExtractor = IndexExtractor.fromBitMapExtractor(bitMapExtractor);
        
        int[] indices = indexExtractor.asIndexArray();
        assertEquals("Should extract 64 indices for all bits set", 64, indices.length);
    }

    @Test
    public void fromBitMapExtractor_withPartialBitMap_shouldExtractCorrectIndices() {
        // Create bitmap with specific bit pattern
        long[] bitMapArray = new long[1];
        bitMapArray[0] = -2401L; // Specific bit pattern
        
        BitMapExtractor bitMapExtractor = BitMapExtractor.fromBitMapArray(bitMapArray);
        IndexExtractor indexExtractor = IndexExtractor.fromBitMapExtractor(bitMapExtractor);
        
        int[] indices = indexExtractor.asIndexArray();
        assertEquals("Should extract 60 indices for this bit pattern", 60, indices.length);
    }

    // ========== Error Handling Tests ==========

    @Test
    public void asIndexArray_withNegativeIndex_shouldThrowIndexOutOfBoundsException() {
        int[] invalidIndices = {-1};
        IndexExtractor extractor = IndexExtractor.fromIndexArray(invalidIndices);
        
        try {
            extractor.uniqueIndices();
            fail("Expected IndexOutOfBoundsException for negative index");
        } catch (IndexOutOfBoundsException e) {
            assertTrue("Error message should mention negative index", 
                      e.getMessage().contains("bitIndex < 0: -1"));
        }
    }

    @Test
    public void asIndexArray_withMaxIntegerIndex_shouldThrowIndexOutOfBoundsException() {
        int[] largeIndices = {Integer.MAX_VALUE, 0, 0};
        IndexExtractor extractor = IndexExtractor.fromIndexArray(largeIndices);
        IndexExtractor uniqueExtractor = extractor.uniqueIndices();
        
        try {
            uniqueExtractor.asIndexArray();
            fail("Expected IndexOutOfBoundsException for Integer.MAX_VALUE index");
        } catch (IndexOutOfBoundsException e) {
            assertTrue("Error message should mention invalid range", 
                      e.getMessage().contains("fromIndex < 0"));
        }
    }

    @Test
    public void uniqueIndices_withNullArray_shouldThrowNullPointerException() {
        IndexExtractor extractor = IndexExtractor.fromIndexArray((int[]) null);
        
        try {
            extractor.uniqueIndices();
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected behavior
        }
    }

    // ========== Core Functionality Tests ==========

    @Test
    public void asIndexArray_withEmptyBloomFilter_shouldReturnEmptyArray() {
        Shape shape = Shape.fromKM(1856, 1856);
        SimpleBloomFilter emptyFilter = new SimpleBloomFilter(shape);
        
        int[] indices = emptyFilter.asIndexArray();
        assertEquals("Empty bloom filter should have no indices", 0, indices.length);
    }

    @Test
    public void uniqueIndices_withDuplicateIndices_shouldRemoveDuplicates() {
        int[] duplicateIndices = {0, 0, 0, 0}; // Four zeros
        IndexExtractor extractor = IndexExtractor.fromIndexArray(duplicateIndices);
        
        IndexExtractor uniqueExtractor = extractor.uniqueIndices();
        int[] uniqueIndices = uniqueExtractor.asIndexArray();
        
        assertEquals("Should have only one unique index", 1, uniqueIndices.length);
        assertArrayEquals("Should contain single zero", new int[]{0}, uniqueIndices);
    }

    @Test
    public void uniqueIndices_calledTwice_shouldReturnSameInstance() {
        int[] indices = {0, 0, 0, 0};
        IndexExtractor extractor = IndexExtractor.fromIndexArray(indices);
        
        IndexExtractor firstUnique = extractor.uniqueIndices();
        IndexExtractor secondUnique = firstUnique.uniqueIndices();
        
        assertSame("Calling uniqueIndices on already unique extractor should return same instance", 
                  firstUnique, secondUnique);
    }

    // ========== Integration Tests with Bloom Filters ==========

    @Test
    public void bloomFilterContains_withZeroIndex_shouldReturnFalse() {
        int[] zeroIndex = {0};
        IndexExtractor extractor = IndexExtractor.fromIndexArray(zeroIndex);
        
        Shape shape = Shape.fromNM(1624, 1624);
        ArrayCountingBloomFilter filter = new ArrayCountingBloomFilter(shape);
        
        boolean contains = filter.contains(extractor);
        assertFalse("Empty filter should not contain any indices", contains);
    }

    @Test
    public void bloomFilterContains_withUniqueIndices_shouldReturnFalse() {
        int[] zeroIndices = {0, 0, 0, 0};
        IndexExtractor extractor = IndexExtractor.fromIndexArray(zeroIndices);
        IndexExtractor uniqueExtractor = extractor.uniqueIndices();
        
        Shape shape = Shape.fromKM(1867, 31);
        SimpleBloomFilter filter = new SimpleBloomFilter(shape);
        
        boolean contains = filter.contains(uniqueExtractor);
        assertFalse("Empty filter should not contain indices", contains);
    }

    @Test
    public void sparseBloomFilterContains_withBitMapIndices_shouldReturnFalse() {
        // Create bitmap with all bits set in one position
        long[] bitMapArray = new long[18];
        bitMapArray[2] = -1L;
        
        BitMapExtractor bitMapExtractor = BitMapExtractor.fromBitMapArray(bitMapArray);
        IndexExtractor indexExtractor = IndexExtractor.fromBitMapExtractor(bitMapExtractor);
        
        Shape shape = Shape.fromNM(2147479552, 2147479552);
        SparseBloomFilter filter = new SparseBloomFilter(shape);
        
        boolean contains = filter.contains(indexExtractor);
        assertFalse("Empty sparse filter should not contain indices", contains);
    }

    // ========== Edge Case Tests ==========

    @Test(expected = RuntimeException.class)
    public void asIndexArray_withExtremelyLargeShape_shouldThrowException() {
        Shape extremeShape = Shape.fromNM(2147352576, 2147352576);
        SimpleBloomFilter filter = new SimpleBloomFilter(extremeShape);
        
        // This should throw an exception due to memory constraints
        filter.asIndexArray();
    }

    @Test(expected = RuntimeException.class)
    public void uniqueIndices_withEnhancedDoubleHasher_shouldHandleNegativeValues() {
        EnhancedDoubleHasher hasher = new EnhancedDoubleHasher(-2285L, -2285L);
        Shape shape = Shape.fromNM(6, 2147352576);
        IndexExtractor extractor = hasher.indices(shape);
        
        // This should throw an exception due to the extreme parameters
        extractor.uniqueIndices();
    }
}