package org.mockito.internal.util.collections;

import org.junit.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotSame;

/**
 * This test class focuses on the behavior of the HashCodeAndEqualsSafeSet,
 * particularly its array conversion methods.
 */
public class HashCodeAndEqualsSafeSetTest {

    /**
     * This test verifies that the toArray(T[] a) method correctly creates and returns a new array
     * when the provided array is too small to hold the set's elements.
     *
     * According to the Collection#toArray(T[] a) contract, if the provided array's length
     * is less than the collection's size, a new array of the same runtime type is allocated
     * and returned.
     */
    @Test
    public void toArrayWithPreallocatedArraySmallerThanSetShouldReturnNewArray() {
        // Arrange
        // Create a set containing a single null element.
        List<Object> sourceCollection = Collections.singletonList(null);
        HashCodeAndEqualsSafeSet setWithNull = HashCodeAndEqualsSafeSet.of(sourceCollection);

        // Create a pre-allocated array that is smaller than the set (it's empty).
        Object[] tooSmallArray = new Object[0];
        Object[] expectedArray = {null};

        // Act
        // Call toArray() with the undersized array.
        Object[] resultArray = setWithNull.toArray(tooSmallArray);

        // Assert
        // The method should return a new array containing the set's elements.
        assertArrayEquals("The returned array should contain the set's elements.", expectedArray, resultArray);

        // Also, verify that a new array instance was created, as per the contract.
        assertNotSame("A new array instance should have been created.", tooSmallArray, resultArray);
    }
}