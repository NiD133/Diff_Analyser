package org.mockito.internal.util.collections;

import org.junit.Test;

/**
 * Unit tests for {@link HashCodeAndEqualsSafeSet}.
 */
public class HashCodeAndEqualsSafeSetTest {

    /**
     * This test verifies that the toArray(T[] a) method correctly throws a
     * NullPointerException when a null array is passed as an argument.
     * This behavior is consistent with the contract of java.util.Collection.
     */
    @Test(expected = NullPointerException.class)
    public void toArray_shouldThrowNullPointerException_whenGivenNullArray() {
        // Arrange: Create an empty set.
        HashCodeAndEqualsSafeSet emptySet = new HashCodeAndEqualsSafeSet();

        // Act & Assert: Call toArray with a null argument.
        // The @Test(expected=...) annotation asserts that a NullPointerException is thrown.
        emptySet.toArray((Object[]) null);
    }
}