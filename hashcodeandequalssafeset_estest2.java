package org.mockito.internal.util.collections;

import org.junit.Test;
import static org.junit.Assert.assertSame;

/**
 * Tests for {@link HashCodeAndEqualsSafeSet}.
 */
public class HashCodeAndEqualsSafeSetTest {

    /**
     * Verifies that calling {@code toArray(T[])} on an empty set returns the
     * provided array instance, as specified by the {@link java.util.Collection} contract.
     * When the destination array is large enough to hold the collection's elements
     * (which any array is for an empty collection), the collection should use it
     * directly instead of allocating a new one.
     */
    @Test
    public void toArray_whenSetIsEmpty_shouldReturnTheProvidedArray() {
        // Arrange
        HashCodeAndEqualsSafeSet emptySet = new HashCodeAndEqualsSafeSet();
        Object[] destinationArray = new Object[0];

        // Act
        Object[] resultArray = emptySet.toArray(destinationArray);

        // Assert
        assertSame(
            "For an empty set, toArray(T[]) should return the same instance of the provided array.",
            destinationArray,
            resultArray
        );
    }
}