package org.jfree.data;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

/**
 * Unit tests for the {@link ComparableObjectItem} class, focusing on the compareTo method.
 */
public class ComparableObjectItemTest {

    /**
     * Verifies that the compareTo() method correctly delegates the comparison
     * to the underlying 'x' value (the Comparable object).
     */
    @Test
    public void compareToShouldDelegateToInternalComparable() {
        // Arrange
        final int expectedComparisonResult = -1236;

        // Create a mock for the internal Comparable object.
        @SuppressWarnings("unchecked")
        Comparable<Object> mockComparable = mock(Comparable.class);

        // Configure the mock's compareTo method to return a specific, non-zero value.
        // This ensures we can verify that the call is correctly delegated.
        when(mockComparable.compareTo(any())).thenReturn(expectedComparisonResult);

        // Create an instance of ComparableObjectItem using the mock.
        // The second parameter (the 'object') is not relevant for comparison.
        ComparableObjectItem item = new ComparableObjectItem(mockComparable, "any-object-value");

        // Act
        // The compareTo method of ComparableObjectItem should invoke the compareTo
        // method of our mockComparable.
        int actualResult = item.compareTo(item);

        // Assert
        // The result should be the value we configured the mock to return.
        assertEquals("The comparison result must be delegated from the internal Comparable object.",
                expectedComparisonResult, actualResult);
    }
}