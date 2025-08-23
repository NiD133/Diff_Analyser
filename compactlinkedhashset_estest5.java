package com.google.common.collect;

import static org.junit.Assert.assertSame;

import org.junit.Test;

/**
 * Contains tests for the {@link CompactLinkedHashSet#toArray(Object[])} method.
 */
public class CompactLinkedHashSetToArrayTest {

    /**
     * Verifies that calling toArray(T[]) on an empty set with a zero-sized array
     * returns the original array instance, as specified by the Collection.toArray(T[]) contract.
     */
    @Test
    public void toArray_onEmptySet_withEmptyInputArray_returnsSameArrayInstance() {
        // Arrange: Create an empty set and an empty array to pass to the toArray method.
        CompactLinkedHashSet<String> emptySet = CompactLinkedHashSet.create();
        String[] inputArray = new String[0];

        // Act: Call the toArray method.
        String[] resultArray = emptySet.toArray(inputArray);

        // Assert: The returned array should be the exact same instance as the input array,
        // because the set's elements (zero of them) fit into the provided array.
        assertSame(
            "Expected toArray to return the same instance of the input array when the set is empty.",
            inputArray,
            resultArray);
    }
}