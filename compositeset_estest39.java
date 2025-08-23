package org.apache.commons.collections4.set;

import org.junit.Test;
import java.util.Set;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

/**
 * This test class contains tests for the CompositeSet class.
 * This specific test focuses on the toArray(T[] array) method.
 */
public class CompositeSet_ESTestTest39 extends CompositeSet_ESTest_scaffolding {

    /**
     * Tests that calling toArray(T[] array) on an empty CompositeSet
     * with a zero-length array returns the same empty array instance.
     * This adheres to the contract of java.util.Collection#toArray(T[]).
     */
    @Test
    public void toArrayOnEmptySetWithEmptyArrayReturnsSameArray() {
        // Arrange: Create an empty CompositeSet.
        CompositeSet<Object> emptySet = new CompositeSet<>(new Set[0]);
        Object[] destinationArray = new Object[0];

        // Act: Call the toArray method with the pre-sized destination array.
        Object[] resultArray = emptySet.toArray(destinationArray);

        // Assert: Verify the behavior aligns with the Collection#toArray contract.
        assertNotNull("The returned array should not be null.", resultArray);
        assertEquals("The returned array should be empty.", 0, resultArray.length);

        // The contract states that if the collection fits in the specified array,
        // the array is returned. An empty collection fits in a zero-length array.
        assertSame("The returned array should be the same instance as the one passed in.",
                destinationArray, resultArray);
    }
}