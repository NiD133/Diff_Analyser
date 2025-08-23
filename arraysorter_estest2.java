package org.apache.commons.lang3;

import org.junit.Test;
import java.util.Comparator;

import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;

/**
 * Unit tests for {@link ArraySorter}.
 */
public class ArraySorterTest {

    /**
     * Tests that sorting an empty array with a comparator returns the same array instance
     * without invoking the comparator.
     */
    @Test
    public void sortWithComparator_shouldReturnSameInstance_whenArrayIsEmpty() {
        // Arrange
        final Object[] emptyArray = new Object[0];
        @SuppressWarnings("unchecked")
        final Comparator<Object> mockComparator = mock(Comparator.class);

        // Act
        final Object[] result = ArraySorter.sort(emptyArray, mockComparator);

        // Assert
        assertSame("Sorting an empty array should return the same instance", emptyArray, result);
        
        // Also, verify that the comparator is never used when the array is empty.
        verifyNoInteractions(mockComparator);
    }
}