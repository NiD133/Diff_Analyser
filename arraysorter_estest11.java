package org.apache.commons.lang3;

import org.junit.Test;

/**
 * Tests for {@link ArraySorter}.
 */
public class ArraySorterTest {

    /**
     * Tests that calling sort() on an array containing non-Comparable objects
     * throws a ClassCastException, as there is no natural order to use.
     */
    @Test(expected = ClassCastException.class)
    public void sortArrayWithNonComparableElementShouldThrowClassCastException() {
        // Arrange: Create an array containing an element that does not implement Comparable.
        // The java.lang.Object class itself does not implement the Comparable interface.
        final Object[] array = new Object[]{new Object()};

        // Act: Attempt to sort the array. This should fail because the element
        // cannot be cast to Comparable for sorting.
        ArraySorter.sort(array);

        // Assert: The test passes if a ClassCastException is thrown, as specified
        // by the 'expected' parameter in the @Test annotation.
    }
}