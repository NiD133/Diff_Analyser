package org.jfree.data;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the {@link ComparableObjectItem} class.
 */
public class ComparableObjectItemTest {

    /**
     * Verifies that the compareTo() method correctly delegates the comparison
     * to the internal 'Comparable' object's compareTo() method.
     */
    @Test
    public void compareToShouldDelegateToInternalComparable() {
        // --- Arrange ---

        // 1. Define an arbitrary, non-zero integer that our mock Comparable will return.
        // This value will be the expected outcome of the test.
        final int expectedComparisonResult = 98;

        // 2. Create a mock of the Comparable interface. This mock will serve as the
        // 'x' value within our ComparableObjectItem instance.
        @SuppressWarnings("unchecked") // Safe as we control the mock's behavior.
        Comparable<Object> mockComparable = mock(Comparable.class);

        // 3. Configure the mock's behavior. When its compareTo() method is called
        // with any object, it should return our predefined expected result.
        when(mockComparable.compareTo(any())).thenReturn(expectedComparisonResult);

        // 4. Instantiate the class under test. The second argument (the "object" part)
        // is not relevant for the compareTo logic, so we can use any value.
        ComparableObjectItem item = new ComparableObjectItem(mockComparable, "any-object-value");

        // --- Act ---

        // 5. Call the method under test. We compare the item to itself, which will
        // trigger a call to mockComparable.compareTo(mockComparable).
        int actualResult = item.compareTo(item);

        // --- Assert ---

        // 6. Assert that the result from ComparableObjectItem's compareTo() method
        // is the same value that our mock was configured to return.
        assertEquals("The comparison result should be delegated from the internal Comparable.",
                expectedComparisonResult, actualResult);

        // 7. (Optional but recommended) Verify that the internal Comparable's compareTo method
        // was indeed called exactly once, confirming the delegation.
        verify(mockComparable, times(1)).compareTo(mockComparable);
    }
}