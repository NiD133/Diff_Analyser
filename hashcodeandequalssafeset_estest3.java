package org.mockito.internal.util.collections;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

/**
 * Unit tests for {@link HashCodeAndEqualsSafeSet}.
 */
public class HashCodeAndEqualsSafeSetTest {

    @Test
    public void toArray_shouldReturnArrayWithCorrectElement_whenSetContainsOneElement() {
        // Arrange: Create a new set and an element to add to it.
        HashCodeAndEqualsSafeSet safeSet = new HashCodeAndEqualsSafeSet();
        Object elementToAdd = new Object();
        safeSet.add(elementToAdd);

        // Act: Convert the set to an array.
        Object[] resultArray = safeSet.toArray();

        // Assert: Verify that the resulting array has the correct size and content.
        assertEquals("The array should contain exactly one element.", 1, resultArray.length);
        assertSame("The element in the array should be the same instance that was added.", elementToAdd, resultArray[0]);
    }
}