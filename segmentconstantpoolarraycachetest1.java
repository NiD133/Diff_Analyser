package org.apache.commons.compress.harmony.unpack200;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link SegmentConstantPoolArrayCache}.
 */
class SegmentConstantPoolArrayCacheTest {

    private SegmentConstantPoolArrayCache arrayCache;

    @BeforeEach
    void setUp() {
        arrayCache = new SegmentConstantPoolArrayCache();
    }

    @Test
    @DisplayName("Should find all indexes for a value that appears multiple times")
    void shouldFindAllIndexesForValueAppearingMultipleTimes() {
        // Arrange
        final String[] array = {"Zero", "Shared", "Two", "Shared", "Shared"};
        final String valueToFind = "Shared";
        final List<Integer> expectedIndexes = List.of(1, 3, 4);

        // Act
        final List<Integer> actualIndexes = arrayCache.indexesForArrayKey(array, valueToFind);

        // Assert
        assertEquals(expectedIndexes, actualIndexes);
    }

    @Test
    @DisplayName("Should find the correct index for a value that appears once")
    void shouldFindIndexForValueAppearingOnce() {
        // Arrange
        final String[] array = {"Zero", "Shared", "Two", "Shared", "Shared"};
        final String valueToFind = "Two";
        final List<Integer> expectedIndexes = List.of(2);

        // Act
        final List<Integer> actualIndexes = arrayCache.indexesForArrayKey(array, valueToFind);

        // Assert
        assertEquals(expectedIndexes, actualIndexes);
    }

    @Test
    @DisplayName("Should return an empty list for a value not present in the array")
    void shouldReturnEmptyListForValueNotInArray() {
        // Arrange
        final String[] array = {"Zero", "One", "Two"};
        final String valueToFind = "NotFound";

        // Act
        final List<Integer> actualIndexes = arrayCache.indexesForArrayKey(array, valueToFind);

        // Assert
        assertTrue(actualIndexes.isEmpty());
    }

    @Test
    @DisplayName("Should cache and retrieve indexes for multiple arrays independently")
    void shouldHandleMultipleArraysAndCacheHitsCorrectly() {
        // Arrange
        final String[] arrayOne = {"Zero", "Shared", "Two", "Shared", "Shared"};
        final String[] arrayTwo = {"Shared", "One", "Shared", "Shared", "Shared"};

        // Act: Perform multiple lookups across different arrays and for different keys.
        // The cache should handle these lookups correctly and independently.
        final List<Integer> sharedInArrayOne = arrayCache.indexesForArrayKey(arrayOne, "Shared");
        final List<Integer> sharedInArrayTwo = arrayCache.indexesForArrayKey(arrayTwo, "Shared");
        final List<Integer> twoInArrayOne = arrayCache.indexesForArrayKey(arrayOne, "Two");
        final List<Integer> notFoundInArrayTwo = arrayCache.indexesForArrayKey(arrayTwo, "NotFound");
        
        // This second lookup for "Shared" in arrayOne should hit the cache.
        final List<Integer> sharedInArrayOneAgain = arrayCache.indexesForArrayKey(arrayOne, "Shared");

        // Assert
        assertAll(
            () -> assertEquals(List.of(1, 3, 4), sharedInArrayOne, "Should find all 'Shared' in array one"),
            () -> assertEquals(List.of(0, 2, 3, 4), sharedInArrayTwo, "Should find all 'Shared' in array two"),
            () -> assertEquals(List.of(2), twoInArrayOne, "Should find 'Two' in array one"),
            () -> assertEquals(Collections.emptyList(), notFoundInArrayTwo, "Should not find 'NotFound' in array two"),
            () -> assertEquals(sharedInArrayOne, sharedInArrayOneAgain, "Second lookup should return the same result")
        );
    }
}