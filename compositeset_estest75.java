package org.apache.commons.collections4.set;

import org.junit.Test;
import java.util.HashSet;
import java.util.Set;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

/**
 * Unit tests for {@link CompositeSet}.
 */
public class CompositeSetTest {

    /**
     * Tests that the toArray(T[] array) method returns the same array instance
     * when the provided array is large enough to hold the set's elements.
     */
    @Test
    public void toArrayWithSufficientlyLargeArrayReturnsSameArrayInstance() {
        // Arrange
        // Create an empty composite set. The set's size is 0.
        CompositeSet<Integer> emptyCompositeSet = new CompositeSet<>(new HashSet<>());

        // Create a destination array that is larger than the set.
        Integer[] destinationArray = new Integer[9];

        // Act
        // Call toArray() with the pre-sized array.
        Integer[] resultArray = emptyCompositeSet.toArray(destinationArray);

        // Assert
        // The contract of Collection.toArray(T[]) states that if the provided
        // array is large enough, it should be used and returned.
        assertSame("The returned array should be the same instance as the one provided",
                destinationArray, resultArray);

        // The length of the array should, of course, be unchanged.
        assertEquals("The length of the returned array should not change",
                9, resultArray.length);
    }
}