package org.apache.commons.lang3;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.Comparator;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link ArraySorter}.
 * Note: The original test class name 'ArraySorterTestTest3' is unusual.
 * A more standard name would be 'ArraySorterTest'.
 */
public class ArraySorterTestTest3 extends AbstractLangTest {

    /**
     * Tests that ArraySorter.sort(T[], Comparator) correctly sorts an array of objects.
     * This test uses a clear Arrange-Act-Assert pattern with a predefined expected result,
     * making its purpose immediately obvious without needing to mentally execute another
     * sort method.
     */
    @Test
    void testSortWithComparatorSortsArray() {
        // Arrange
        final String[] actualArray = {"foo", "bar", "baz"};
        final String[] expectedArray = {"bar", "baz", "foo"};

        // Act
        ArraySorter.sort(actualArray, String::compareTo);

        // Assert
        assertArrayEquals(expectedArray, actualArray);
    }

    /**
     * Tests that ArraySorter.sort(T[], Comparator) returns null when given a null array.
     * This is an important edge case for the method that takes a comparator.
     */
    @Test
    void testSortWithComparatorGivenNullArrayReturnsNull() {
        // Act & Assert
        assertNull(ArraySorter.sort((String[]) null, Comparator.naturalOrder()));
    }

    /**
     * Tests that the naturally ordered ArraySorter.sort(T[]) returns null for a null array.
     * This behavior was part of the original test but has been moved to its own
     * dedicated method to improve clarity and focus.
     */
    @Test
    void testSortWithNaturalOrderGivenNullArrayReturnsNull() {
        // Act & Assert
        assertNull(ArraySorter.sort((String[]) null));
    }
}