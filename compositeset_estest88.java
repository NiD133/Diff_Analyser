package org.apache.commons.collections4.set;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Unit tests for the {@link CompositeSet} class, focusing on the toArray() method.
 */
public class CompositeSetToArrayTest {

    /**
     * Tests that calling {@code toArray()} on a newly created, empty CompositeSet
     * returns an empty, non-null object array.
     */
    @Test
    public void toArrayOnEmptySetShouldReturnEmptyArray() {
        // Arrange: Create an empty CompositeSet.
        // The generic type is not critical for this test, so Object is used for generality.
        CompositeSet<Object> emptyCompositeSet = new CompositeSet<>();

        // Act: Call the toArray() method.
        Object[] resultArray = emptyCompositeSet.toArray();

        // Assert: Verify the returned array is what we expect.
        assertNotNull("The result of toArray() should never be null.", resultArray);
        assertEquals("The array from an empty set should have a length of 0.", 0, resultArray.length);
    }
}