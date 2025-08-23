package org.mockito.internal.util.collections;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for {@link HashCodeAndEqualsSafeSet}.
 */
public class HashCodeAndEqualsSafeSetTest {

    /**
     * This test verifies the behavior of the toArray(T[] array) method
     * when the set is empty and the provided array is large enough.
     *
     * According to the Collection.toArray(T[]) contract, the method should:
     * 1. Return the same array instance that was passed in.
     * 2. Place a null at the first position (index 0) of the array, as it's empty.
     */
    @Test
    public void toArray_onEmptySetWithSufficientlyLargeArray_returnsSameArrayAndAddsNullTerminator() {
        // Arrange
        HashCodeAndEqualsSafeSet emptySet = new HashCodeAndEqualsSafeSet();
        Object[] destinationArray = new Object[] {"a", "b", "c"}; // Pre-fill to ensure the method actively sets the null.
        int originalArrayLength = destinationArray.length;

        // Act
        Object[] resultArray = emptySet.toArray(destinationArray);

        // Assert
        // The returned array should be the same instance as the one passed in.
        assertSame("Should return the same array instance if it's large enough", destinationArray, resultArray);

        // The length should remain unchanged.
        assertEquals("Array length should not change", originalArrayLength, resultArray.length);

        // The element at index 0 should be set to null, as per the contract for an empty collection.
        assertNull("The first element should be a null terminator", resultArray[0]);
    }
}