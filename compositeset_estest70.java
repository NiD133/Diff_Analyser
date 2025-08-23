package org.apache.commons.collections4.set;

import org.junit.Test;

import java.util.HashSet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

/**
 * This class contains the improved test case for the CompositeSet.
 * The original test class name and inheritance are kept for context,
 * but in a real-world scenario, they would likely be refactored.
 */
public class CompositeSet_ESTestTest70 extends CompositeSet_ESTest_scaffolding {

    /**
     * Tests that calling toArray(T[] array) on an empty CompositeSet
     * with an array larger than the set's size (0) returns the original array instance.
     * It also verifies that the element at index 0 of the array is set to null,
     * as per the Collection.toArray(T[]) contract.
     */
    @Test
    public void toArrayWithSufficientlyLargeArrayOnEmptySetShouldReturnTheGivenArray() {
        // Arrange
        // Create a composite set that is empty of elements, but contains two empty component sets.
        CompositeSet<String> emptyCompositeSet = new CompositeSet<>(new HashSet<>(), new HashSet<>());

        // Create a destination array that is larger than the composite set's size (0).
        // Initialize an element to ensure it gets nulled out by the toArray call.
        String[] destinationArray = {"should be overwritten", "b", "c"};

        // Act
        String[] resultArray = emptyCompositeSet.toArray(destinationArray);

        // Assert
        // 1. The method should return the same array instance that was passed in.
        assertSame("The returned array should be the same instance as the destination array",
                     destinationArray, resultArray);

        // 2. The length of the array should be unchanged.
        assertEquals("The array length should not change", 3, resultArray.length);

        // 3. Per the toArray(T[]) contract, the element at index 'size' (which is 0)
        //    should be set to null.
        assertNull("The element at index 0 should be nulled out", resultArray[0]);
    }
}