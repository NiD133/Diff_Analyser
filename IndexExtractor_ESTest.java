package org.apache.commons.collections4.bloomfilter;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;

import org.junit.Test;

/**
 * Tests for the {@link IndexExtractor} interface and its factory methods.
 */
public class IndexExtractorTest {

    /**
     * A helper method to create an IndexExtractor from an array of indices.
     * This is an alias for the static factory method on the interface.
     * @param indices The indices for the extractor.
     * @return An IndexExtractor instance.
     */
    private IndexExtractor fromIndices(int... indices) {
        return IndexExtractor.fromIndexArray(indices);
    }

    //
    // Tests for factory methods
    //

    @Test(expected = NullPointerException.class)
    public void fromBitMapExtractor_shouldThrowNullPointerException_whenExtractorIsNull() {
        // This test verifies that the factory method handles null input gracefully.
        IndexExtractor.fromBitMapExtractor(null);
    }

    //
    // Tests for asIndexArray() method
    //

    @Test
    public void asIndexArray_shouldReturnEmptyArray_forEmptyExtractor() {
        // Arrange
        IndexExtractor emptyExtractor = fromIndices();

        // Act
        int[] result = emptyExtractor.asIndexArray();

        // Assert
        assertEquals("Should return an empty array for an empty extractor", 0, result.length);
    }

    @Test
    public void asIndexArray_shouldReturnCorrectIndices_whenCreatedFromBitMap() {
        // Arrange: -2401L is 0b111111111111111111111111111111111111111111111111010101111111
        // It has 60 bits set.
        long[] bitMap = { -2401L };
        BitMapExtractor bitMapExtractor = BitMapExtractor.fromBitMapArray(bitMap);
        IndexExtractor indexExtractor = IndexExtractor.fromBitMapExtractor(bitMapExtractor);

        // Act
        int[] result = indexExtractor.asIndexArray();

        // Assert
        assertEquals("Should return the correct number of indices", 60, result.length);
    }

    @Test
    public void asIndexArray_shouldReturnAll64Indices_whenBitMapLongIsAllBitsSet() {
        // Arrange: -1L has all 64 bits set. The bitmap places this at the third long (index 2),
        // so indices should be in the range [128, 191].
        long[] bitMap = { 0L, 0L, -1L };
        BitMapExtractor bitMapExtractor = BitMapExtractor.fromBitMapArray(bitMap);
        IndexExtractor indexExtractor = IndexExtractor.fromBitMapExtractor(bitMapExtractor);

        // Act
        int[] result = indexExtractor.asIndexArray();

        // Assert
        assertEquals("A long of -1L should produce 64 indices", 64, result.length);
    }

    @Test(expected = NullPointerException.class)
    public void asIndexArray_shouldThrowNullPointerException_whenCreatedFromNullArray() {
        // Arrange
        IndexExtractor extractor = IndexExtractor.fromIndexArray(null);

        // Act
        extractor.asIndexArray(); // Should throw
    }

    //
    // Tests for uniqueIndices() method
    //

    @Test
    public void uniqueIndices_shouldRemoveDuplicatesAndSortIndices() {
        // Arrange
        IndexExtractor extractorWithDuplicates = fromIndices(5, 1, 10, 2, 5, 1);

        // Act
        IndexExtractor uniqueExtractor = extractorWithDuplicates.uniqueIndices();
        int[] result = uniqueExtractor.asIndexArray();

        // Assert
        int[] expected = {1, 2, 5, 10};
        assertArrayEquals("Should return sorted unique indices", expected, result);
    }

    @Test
    public void uniqueIndices_shouldReturnSameInstance_whenCalledMultipleTimes() {
        // Arrange
        IndexExtractor extractor = fromIndices(1, 2, 3);
        IndexExtractor uniqueExtractor = extractor.uniqueIndices();

        // Act
        IndexExtractor uniqueExtractor2 = uniqueExtractor.uniqueIndices();

        // Assert
        // This tests the optimization that calling uniqueIndices() on an already unique
        // extractor should return itself.
        assertSame("Calling uniqueIndices() again should return the same instance", uniqueExtractor, uniqueExtractor2);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void uniqueIndices_shouldThrowIndexOutOfBounds_whenIndexIsNegative() {
        // Arrange
        IndexExtractor extractor = fromIndices(1, -1, 2);

        // Act
        // The uniqueIndices() method uses a BitSet internally, which cannot handle negative indices.
        extractor.uniqueIndices(); // Should throw
    }

    @Test(expected = NullPointerException.class)
    public void uniqueIndices_shouldThrowNullPointerException_whenCreatedFromNullArray() {
        // Arrange
        IndexExtractor extractor = IndexExtractor.fromIndexArray(null);

        // Act
        extractor.uniqueIndices(); // Should throw
    }

    @Test
    public void uniqueIndices_shouldThrowIndexOutOfBounds_whenHasherProducesNegativeIndex() {
        // Arrange: A hasher with negative seeds can produce negative indices, which are invalid.
        Hasher hasher = new EnhancedDoubleHasher(-2285L, -2285L);
        Shape shape = Shape.fromNM(6, Integer.MAX_VALUE); // Large shape
        IndexExtractor extractor = hasher.indices(shape);

        // Act & Assert
        try {
            extractor.uniqueIndices();
            fail("Expected IndexOutOfBoundsException for negative index");
        } catch (IndexOutOfBoundsException expected) {
            // This is the expected outcome.
        }
    }

    //
    // Integration tests with BloomFilter implementations
    //

    @Test
    public void contains_shouldReturnFalse_forNonEmptyExtractorAndEmptySimpleFilter() {
        // Arrange
        Shape shape = Shape.fromKM(10, 100);
        SimpleBloomFilter emptyFilter = new SimpleBloomFilter(shape);
        IndexExtractor extractor = fromIndices(0, 5, 10);

        // Act
        boolean result = emptyFilter.contains(extractor);

        // Assert
        assertFalse("An empty filter should not contain any indices", result);
    }

    @Test
    public void contains_shouldReturnFalse_forNonEmptyExtractorAndEmptyCountingFilter() {
        // Arrange
        Shape shape = Shape.fromKM(10, 100);
        ArrayCountingBloomFilter emptyFilter = new ArrayCountingBloomFilter(shape);
        IndexExtractor extractor = fromIndices(0, 5, 10);

        // Act
        boolean result = emptyFilter.contains(extractor);

        // Assert
        assertFalse("An empty counting filter should not contain any indices", result);
    }

    @Test
    public void contains_shouldReturnFalse_forNonEmptyExtractorAndEmptySparseFilter() {
        // Arrange
        Shape shape = Shape.fromKM(10, 100);
        SparseBloomFilter emptyFilter = new SparseBloomFilter(shape);
        IndexExtractor extractor = fromIndices(0, 5, 10);

        // Act
        boolean result = emptyFilter.contains(extractor);

        // Assert
        assertFalse("An empty sparse filter should not contain any indices", result);
    }
}