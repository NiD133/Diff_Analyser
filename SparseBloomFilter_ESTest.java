package org.apache.commons.collections4.bloomfilter;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.function.IntPredicate;

/**
 * Test suite for the {@link SparseBloomFilter}.
 */
public class SparseBloomFilterTest {

    private static final Shape SHAPE = Shape.fromKM(17, 72); // k=17 hash functions, m=72 bits
    private static final Hasher HASHER = new EnhancedDoubleHasher(new byte[]{1, 2, 3});

    /**
     * Creates a filter with some indices for testing non-empty scenarios.
     * @return A new SparseBloomFilter containing indices 1, 10, and 25.
     */
    private SparseBloomFilter createPopulatedFilter() {
        SparseBloomFilter filter = new SparseBloomFilter(SHAPE);
        filter.merge(IndexExtractor.fromIndexArray(new int[]{1, 10, 25}));
        return filter;
    }

    //
    // Constructor and State Tests
    //

    @Test
    public void constructor_withValidShape_createsEmptyFilter() {
        // Arrange & Act
        SparseBloomFilter filter = new SparseBloomFilter(SHAPE);

        // Assert
        assertTrue("A new filter should be empty", filter.isEmpty());
        assertEquals("A new filter should have zero cardinality", 0, filter.cardinality());
        assertEquals("The shape should be retained", SHAPE, filter.getShape());
    }

    @Test(expected = NullPointerException.class)
    public void constructor_withNullShape_throwsNullPointerException() {
        // Arrange, Act & Assert
        new SparseBloomFilter(null);
    }

    @Test
    public void clear_onNonEmptyFilter_makesItEmpty() {
        // Arrange
        SparseBloomFilter filter = createPopulatedFilter();
        assertFalse("Filter should be populated before clearing", filter.isEmpty());

        // Act
        filter.clear();

        // Assert
        assertTrue("Filter should be empty after clearing", filter.isEmpty());
        assertEquals("Cardinality should be zero after clearing", 0, filter.cardinality());
    }

    @Test
    public void copy_createsIndependentCopy() {
        // Arrange
        SparseBloomFilter original = createPopulatedFilter();

        // Act
        SparseBloomFilter copy = original.copy();

        // Assert
        assertNotSame("Copy should be a different object", original, copy);
        assertEquals("Copy should have the same cardinality", original.cardinality(), copy.cardinality());
        assertTrue("Copy should contain all original indices", copy.contains(original));

        // Modify the copy and verify the original is unchanged
        copy.clear();
        assertFalse("Original should not be affected by changes to the copy", original.isEmpty());
    }

    @Test
    public void characteristics_shouldReturnSparseFlag() {
        // Arrange
        SparseBloomFilter filter = new SparseBloomFilter(SHAPE);
        // The integer 1 corresponds to the SPARCE characteristic.
        int expected = 1;

        // Act & Assert
        assertEquals(expected, filter.characteristics());
    }

    @Test
    public void asBitMapArray_returnsCorrectBitMapRepresentation() {
        // Arrange
        // Using a shape with more than 64 bits to test multiple longs in the array.
        Shape shape = Shape.fromKM(3, 100);
        SparseBloomFilter filter = new SparseBloomFilter(shape);
        // Indices: 1 (in first long), 65 (bit 1 in second long), 70 (bit 6 in second long)
        filter.merge(IndexExtractor.fromIndexArray(new int[]{1, 65, 70}));

        // Act
        long[] bitMap = filter.asBitMapArray();

        // Assert
        assertEquals("Bitmap array should have 2 longs for 100 bits", 2, bitMap.length);
        assertEquals("First long should have bit 1 set", 1L << 1, bitMap[0]);
        assertEquals("Second long should have bits 1 and 6 set", (1L << 1) | (1L << 6), bitMap[1]);
    }

    //
    // Merge Tests
    //

    @Test
    public void merge_withHasher_addsIndicesAndIncreasesCardinality() {
        // Arrange
        SparseBloomFilter filter = new SparseBloomFilter(SHAPE);

        // Act
        filter.merge(HASHER);

        // Assert
        assertFalse("Filter should not be empty after merge", filter.isEmpty());
        // Assuming no hash collisions for this specific hasher and shape
        assertEquals("Cardinality should equal the number of hash functions",
                SHAPE.getNumberOfHashFunctions(), filter.cardinality());
    }

    @Test
    public void merge_withIndexExtractor_addsIndices() {
        // Arrange
        SparseBloomFilter filter = new SparseBloomFilter(SHAPE);
        IndexExtractor extractor = IndexExtractor.fromIndexArray(new int[]{5, 15, 50});

        // Act
        filter.merge(extractor);

        // Assert
        assertEquals(3, filter.cardinality());
        assertTrue("Filter should contain the merged indices", filter.contains(extractor));
    }

    @Test
    public void merge_withAnotherBloomFilter_addsIndices() {
        // Arrange
        SparseBloomFilter filterToMergeInto = new SparseBloomFilter(SHAPE);
        SparseBloomFilter filterToAdd = createPopulatedFilter();

        // Act
        filterToMergeInto.merge(filterToAdd);

        // Assert
        assertEquals(filterToAdd.cardinality(), filterToMergeInto.cardinality());
        assertTrue("Filter should contain the merged filter's indices", filterToMergeInto.contains(filterToAdd));
    }

    @Test(expected = NullPointerException.class)
    public void merge_withNullHasher_throwsNullPointerException() {
        new SparseBloomFilter(SHAPE).merge((Hasher) null);
    }

    @Test(expected = NullPointerException.class)
    public void merge_withNullIndexExtractor_throwsNullPointerException() {
        new SparseBloomFilter(SHAPE).merge((IndexExtractor) null);
    }

    @Test(expected = NullPointerException.class)
    public void merge_withNullBitMapExtractor_throwsNullPointerException() {
        new SparseBloomFilter(SHAPE).merge((BitMapExtractor) null);
    }

    @Test(expected = NullPointerException.class)
    public void merge_withNullBloomFilter_throwsNullPointerException() {
        new SparseBloomFilter(SHAPE).merge((BloomFilter<?>) null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void merge_withIncompatibleBloomFilter_throwsIllegalArgumentException() {
        // Arrange
        Shape shapeA = Shape.fromKM(5, 100);
        SparseBloomFilter filterA = new SparseBloomFilter(shapeA);
        filterA.merge(IndexExtractor.fromIndexArray(new int[]{99})); // Valid for shapeA

        Shape shapeB = Shape.fromKM(5, 50); // Incompatible shape
        SparseBloomFilter filterB = new SparseBloomFilter(shapeB);

        // Act: Merging filterA into filterB should fail as index 99 is out of bounds for shapeB.
        filterB.merge(filterA);
    }

    @Test(expected = IllegalArgumentException.class)
    public void merge_withOutOfBoundsIndex_throwsIllegalArgumentException() {
        // Arrange
        SparseBloomFilter filter = new SparseBloomFilter(SHAPE); // Max index is 71
        IndexExtractor extractor = IndexExtractor.fromIndexArray(new int[]{SHAPE.getNumberOfBits()}); // Index 72 is out of bounds

        // Act & Assert
        filter.merge(extractor);
    }

    @Test(expected = IllegalArgumentException.class)
    public void merge_withNegativeIndex_throwsIllegalArgumentException() {
        // Arrange
        SparseBloomFilter filter = new SparseBloomFilter(SHAPE);
        IndexExtractor extractor = IndexExtractor.fromIndexArray(new int[]{-1});

        // Act & Assert
        filter.merge(extractor);
    }

    //
    // Contains Tests
    //

    @Test
    public void contains_aPopulatedFilter_shouldContainItself() {
        // Arrange
        SparseBloomFilter filter = createPopulatedFilter();

        // Act & Assert
        assertTrue("A filter should contain itself (as IndexExtractor)", filter.contains((IndexExtractor) filter));
        assertTrue("A filter should contain itself (as BitMapExtractor)", filter.contains((BitMapExtractor) filter));
    }

    @Test
    public void contains_anEmptyFilter_shouldContainAnEmptyExtractor() {
        // Arrange
        SparseBloomFilter filter = new SparseBloomFilter(SHAPE);
        IndexExtractor emptyExtractor = IndexExtractor.fromIndexArray(new int[0]);

        // Act & Assert
        assertTrue("An empty filter should contain an empty extractor", filter.contains(emptyExtractor));
    }

    @Test
    public void contains_anEmptyFilter_shouldNotContainPopulatedExtractor() {
        // Arrange
        SparseBloomFilter emptyFilter = new SparseBloomFilter(SHAPE);
        IndexExtractor populatedExtractor = IndexExtractor.fromIndexArray(new int[]{1, 2, 3});

        // Act & Assert
        assertFalse("An empty filter should not contain a populated extractor", emptyFilter.contains(populatedExtractor));
    }

    @Test
    public void contains_aPopulatedFilter_shouldContainSubsetOfIndices() {
        // Arrange
        SparseBloomFilter superset = createPopulatedFilter(); // Contains {1, 10, 25}
        IndexExtractor subset = IndexExtractor.fromIndexArray(new int[]{1, 25});

        // Act & Assert
        assertTrue("A filter should contain a subset of its indices", superset.contains(subset));
    }

    @Test
    public void contains_aPopulatedFilter_shouldNotContainSupersetOfIndices() {
        // Arrange
        SparseBloomFilter subset = new SparseBloomFilter(SHAPE);
        subset.merge(IndexExtractor.fromIndexArray(new int[]{1, 10}));
        IndexExtractor superset = IndexExtractor.fromIndexArray(new int[]{1, 10, 25}); // Contains an extra index

        // Act & Assert
        assertFalse("A filter should not contain a superset of its indices", subset.contains(superset));
    }

    //
    // Processing Tests
    //

    @Test
    public void processIndices_onEmptyFilter_returnsTrueAndDoesNotCallPredicate() {
        // Arrange
        SparseBloomFilter filter = new SparseBloomFilter(SHAPE);
        IntPredicate predicate = i -> {
            fail("Predicate should not be called for an empty filter");
            return false;
        };

        // Act & Assert
        assertTrue(filter.processIndices(predicate));
    }

    @Test
    public void processIndices_whenPredicateReturnsFalse_shortCircuitsAndReturnsFalse() {
        // Arrange
        SparseBloomFilter filter = createPopulatedFilter(); // Contains {1, 10, 25}
        // A predicate that returns false for the first element it sees.
        IntPredicate predicate = i -> false;

        // Act & Assert
        assertFalse("processIndices should return false if the predicate returns false", filter.processIndices(predicate));
    }

    @Test
    public void processBitMaps_onEmptyFilter_returnsTrue() {
        // Arrange
        SparseBloomFilter filter = new SparseBloomFilter(SHAPE);

        // Act & Assert
        assertTrue(filter.processBitMaps(l -> {
            fail("Predicate should not be called for an empty filter");
            return false;
        }));
    }
}