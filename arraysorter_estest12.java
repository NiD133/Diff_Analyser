package org.apache.commons.lang3;

import org.junit.Test;
import java.util.Comparator;

import static org.junit.Assert.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Tests for {@link ArraySorter}.
 */
public class ArraySorterTest {

    @Test
    public void sortWithComparatorShouldReturnSameArrayInstance() {
        // Arrange: Create an array and a mock comparator.
        // The comparator will treat all elements as equal, which is a valid edge case.
        final Integer[] array = new Integer[5];

        @SuppressWarnings("unchecked") // Necessary for mocking generic types like Comparator.
        final Comparator<Integer> mockComparator = mock(Comparator.class);
        when(mockComparator.compare(any(), any())).thenReturn(0);

        // Act: Call the method under test.
        final Integer[] sortedArray = ArraySorter.sort(array, mockComparator);

        // Assert: Verify the behavior of the sort method.
        // 1. The method should sort the array in-place and return the same instance.
        assertSame("The sorted array should be the same instance as the input array.", array, sortedArray);

        // 2. Verify that our custom comparator was actually invoked by the sorting algorithm.
        // We check for atLeastOnce() because the exact number of calls is an
        // implementation detail of the underlying Arrays.sort() method.
        verify(mockComparator, atLeastOnce()).compare(any(), any());
    }
}