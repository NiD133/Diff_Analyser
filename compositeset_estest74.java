package org.apache.commons.collections4.set;

import org.junit.Test;
import static org.junit.Assert.assertSame;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * This class contains tests for the CompositeSet.
 * The original test class name 'CompositeSet_ESTestTest74' has been kept for context,
 * but would typically be named 'CompositeSetTest'.
 */
public class CompositeSet_ESTestTest74 extends CompositeSet_ESTest_scaffolding {

    /**
     * Tests that toArray(T[]) returns the provided array instance when the composite set is empty.
     * <p>
     * According to the Collection#toArray(T[]) contract, if the collection fits into the
     * specified array, that array should be returned. An empty collection perfectly fits
     * into an empty array.
     */
    @Test
    public void toArrayOnEmptySetWithEmptyArrayShouldReturnSameArrayInstance() {
        // Arrange
        final Set<Object> componentSet = new LinkedHashSet<>();
        final CompositeSet<Object> emptyCompositeSet = new CompositeSet<>(componentSet);
        final Object[] destinationArray = new Object[0];

        // Act
        final Object[] resultArray = emptyCompositeSet.toArray(destinationArray);

        // Assert
        assertSame("The returned array should be the same instance as the input array",
                     destinationArray, resultArray);
    }
}