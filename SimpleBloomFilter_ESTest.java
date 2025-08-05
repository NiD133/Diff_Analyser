package org.apache.commons.collections4.bloomfilter;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.function.IntPredicate;

/**
 * Unit tests for {@link SimpleBloomFilter}.
 */
public class SimpleBloomFilterTest {

    private final Shape shape = Shape.fromKM(10, 72); // 10 hash functions, 72 bits
    private final Hasher hasher = new EnhancedDoubleHasher(1, 1);

    //
    // Constructor Tests
    //

    @Test
    public void constructor_withValidShape_createsEmptyFilter() {
        // Arrange & Act
        SimpleBloomFilter filter = new SimpleBloomFilter(shape);

        // Assert
        assertTrue("A new filter should be empty", filter.isEmpty());
        assertEquals("A new filter should have zero cardinality", 0, filter.cardinality());
        assertEquals("A new filter should have zero characteristics", 0, filter.characteristics());
        assertFalse("A new filter should not be full", filter.isFull());
        assertArrayEquals("A new filter should have a zeroed bitmap", new long[2], filter.asBitMapArray());
    }

    @Test
    public void constructor_withNullShape_throwsNullPointerException() {
        // Arrange, Act & Assert
        assertThrows(NullPointerException.class, () -> new SimpleBloomFilter(null));
    }

    //
    // Merge Tests
    //

    @Test
    public void merge_withHasher_modifiesFilterAndReturnsTrue() {
        // Arrange
        SimpleBloomFilter filter = new SimpleBloomFilter(shape);
        int initialCardinality = filter.cardinality();

        // Act
        boolean result = filter.merge(hasher);

        // Assert
        assertTrue("merge should return true when the filter is modified", result);
        assertFalse("Filter should not be empty after merge", filter.isEmpty());
        assertTrue("Cardinality should increase after merge", filter.cardinality() > initialCardinality);
    }

    @Test
    public void merge_withSameHasherTwice_doesNotModifyFilterOnSecondCallAndReturnsFalse() {
        // Arrange
        SimpleBloomFilter filter = new SimpleBloomFilter(shape);
        filter.merge(hasher); // First merge
        int cardinalityAfterFirstMerge = filter.cardinality();

        // Act
        boolean result = filter.merge(hasher); // Second merge

        // Assert
        assertFalse("merge should return false when the filter is not modified", result);
        assertEquals("Cardinality should not change on second merge",
                cardinalityAfterFirstMerge, filter.cardinality());
    }

    @Test
    public void merge_withIndexExtractor_modifiesFilterAndReturnsTrue() {
        // Arrange
        SimpleBloomFilter filter = new SimpleBloomFilter(shape);
        IndexExtractor indexExtractor = IndexExtractor.fromIndexArray(new int[]{1, 2, 3});

        // Act
        boolean result = filter.merge(indexExtractor);

        // Assert
        assertTrue("merge should return true when the filter is modified", result);
        assertEquals("Cardinality should be 3 after merging 3 indices", 3, filter.cardinality());
    }

    @Test
    public void merge_withEmptyIndexExtractor_doesNotModifyFilterAndReturnsFalse() {
        // Arrange
        SimpleBloomFilter filter = new SimpleBloomFilter(shape);
        filter.merge(hasher);
        int initialCardinality = filter.cardinality();
        IndexExtractor emptyExtractor = IndexExtractor.fromIndexArray(new int[0]);

        // Act
        boolean result = filter.merge(emptyExtractor);

        // Assert
        assertFalse("merge with an empty extractor should return false", result);
        assertEquals("Cardinality should not change", initialCardinality, filter.cardinality());
    }

    @Test
    public void merge_withBloomFilter_modifiesFilterAndReturnsTrue() {
        // Arrange
        SimpleBloomFilter filter1 = new SimpleBloomFilter(shape);
        SimpleBloomFilter filter2 = new SimpleBloomFilter(shape);
        filter2.merge(hasher); // Make filter2 non-empty

        // Act
        boolean result = filter1.merge(filter2);

        // Assert
        assertTrue("merge should return true when the filter is modified", result);
        assertEquals("filter1 should have the same cardinality as filter2",
                filter2.cardinality(), filter1.cardinality());
    }

    @Test
    public void merge_withIncompatibleBloomFilter_throwsIllegalArgumentException() {
        // Arrange
        Shape shape2 = Shape.fromKM(5, 128); // Different bitmap length
        SimpleBloomFilter filter1 = new SimpleBloomFilter(shape);
        SimpleBloomFilter filter2 = new SimpleBloomFilter(shape2);

        // Act & Assert
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> filter1.merge(filter2));
        assertTrue(e.getMessage().contains("should send at most 2 maps"));
    }

    @Test
    public void merge_withIndexExtractorContainingOutOfBoundsIndex_throwsIllegalArgumentException() {
        // Arrange
        SimpleBloomFilter filter = new SimpleBloomFilter(shape);
        // An index >= shape.getNumberOfBits() (72) is out of bounds.
        IndexExtractor extractor = IndexExtractor.fromIndexArray(new int[]{1, 72});

        // Act & Assert
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> filter.merge(extractor));
        assertTrue(e.getMessage().contains("should only send values in the range[0,72)"));
    }

    @Test
    public void merge_withNullHasher_throwsNullPointerException() {
        // Arrange
        SimpleBloomFilter filter = new SimpleBloomFilter(shape);

        // Act & Assert
        assertThrows(NullPointerException.class, () -> filter.merge((Hasher) null));
    }

    @Test
    public void merge_withNullIndexExtractor_throwsNullPointerException() {
        // Arrange
        SimpleBloomFilter filter = new SimpleBloomFilter(shape);

        // Act & Assert
        assertThrows(NullPointerException.class, () -> filter.merge((IndexExtractor) null));
    }

    @Test
    public void merge_withNullBitMapExtractor_throwsNullPointerException() {
        // Arrange
        SimpleBloomFilter filter = new SimpleBloomFilter(shape);

        // Act & Assert
        assertThrows(NullPointerException.class, () -> filter.merge((BitMapExtractor) null));
    }

    @Test
    public void merge_withNullBloomFilter_throwsNullPointerException() {
        // Arrange
        SimpleBloomFilter filter = new SimpleBloomFilter(shape);

        // Act & Assert
        assertThrows(NullPointerException.class, () -> filter.merge((BloomFilter<?>) null));
    }

    //
    // Contains Tests
    //

    @Test
    public void contains_withAllPresentIndices_returnsTrue() {
        // Arrange
        SimpleBloomFilter filter = new SimpleBloomFilter(shape);
        IndexExtractor extractor = IndexExtractor.fromIndexArray(new int[]{10, 20, 30});
        filter.merge(extractor);

        // Act
        boolean result = filter.contains(extractor);

        // Assert
        assertTrue("contains should return true for a subset of indices", result);
    }

    @Test
    public void contains_withSomeMissingIndices_returnsFalse() {
        // Arrange
        SimpleBloomFilter filter = new SimpleBloomFilter(shape);
        filter.merge(IndexExtractor.fromIndexArray(new int[]{10, 30}));
        IndexExtractor supersetExtractor = IndexExtractor.fromIndexArray(new int[]{10, 20, 30});

        // Act
        boolean result = filter.contains(supersetExtractor);

        // Assert
        assertFalse("contains should return false if an index is missing", result);
    }

    @Test
    public void contains_withEmptyExtractor_returnsTrue() {
        // Arrange
        SimpleBloomFilter filter = new SimpleBloomFilter(shape);
        filter.merge(hasher); // Filter can be populated or not
        IndexExtractor emptyExtractor = IndexExtractor.fromIndexArray(new int[0]);

        // Act
        boolean result = filter.contains(emptyExtractor);

        // Assert
        assertTrue("contains on an empty set of indices is vacuously true", result);
    }

    @Test
    public void contains_withNullIndexExtractor_throwsNullPointerException() {
        // Arrange
        SimpleBloomFilter filter = new SimpleBloomFilter(shape);

        // Act & Assert
        assertThrows(NullPointerException.class, () -> filter.contains(null));
    }

    //
    // State and Property Tests
    //

    @Test
    public void copy_createsIndependentInstanceWithSameState() {
        // Arrange
        SimpleBloomFilter original = new SimpleBloomFilter(shape);
        original.merge(hasher);

        // Act
        SimpleBloomFilter copy = original.copy();

        // Assert
        assertNotSame("Copy should be a different object", original, copy);
        assertEquals("Copy should have the same shape", original.getShape(), copy.getShape());
        assertEquals("Copy should have the same cardinality", original.cardinality(), copy.cardinality());
        assertArrayEquals("Copy should have the same bitmap data", original.asBitMapArray(), copy.asBitMapArray());

        // Modify the copy and verify the original is unchanged
        copy.merge(new EnhancedDoubleHasher(2, 2));
        assertNotEquals("Modifying copy should not change original's cardinality",
                original.cardinality(), copy.cardinality());
    }

    @Test
    public void clear_onPopulatedFilter_resetsToEmpty() {
        // Arrange
        SimpleBloomFilter filter = new SimpleBloomFilter(shape);
        filter.merge(hasher);
        assertFalse("Filter should be populated before clear", filter.isEmpty());

        // Act
        filter.clear();

        // Assert
        assertTrue("Filter should be empty after clear", filter.isEmpty());
        assertEquals("Cardinality should be zero after clear", 0, filter.cardinality());
    }

    @Test
    public void getShape_returnsConstructorShape() {
        // Arrange
        SimpleBloomFilter filter = new SimpleBloomFilter(shape);

        // Act
        Shape retrievedShape = filter.getShape();

        // Assert
        assertEquals(shape, retrievedShape);
    }

    //
    // Processing Tests
    //

    @Test
    public void processIndices_onEmptyFilter_returnsTrue() {
        // Arrange
        SimpleBloomFilter filter = new SimpleBloomFilter(shape);
        IntPredicate predicate = i -> fail("Predicate should not be called for an empty filter");

        // Act
        boolean result = filter.processIndices(predicate);

        // Assert
        assertTrue("processIndices on an empty filter should return true", result);
    }

    @Test
    public void processIndices_whenPredicateReturnsFalse_shortCircuitsAndReturnsFalse() {
        // Arrange
        SimpleBloomFilter filter = new SimpleBloomFilter(shape);
        // This hasher produces indices 8 and 18 for the given shape
        filter.merge(hasher);

        // A predicate that returns false for one of the known indices
        IntPredicate predicate = i -> i != 18;

        // Act
        boolean result = filter.processIndices(predicate);

        // Assert
        assertFalse("processIndices should return false if the predicate returns false", result);
    }
}