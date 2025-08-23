package org.mockito.internal.util.collections;

import org.junit.Test;

/**
 * Tests for {@link HashCodeAndEqualsSafeSet}.
 */
public class HashCodeAndEqualsSafeSetTest {

    /**
     * This test verifies that the toArray(T[] a) method behaves consistently
     * with the java.util.Collection contract. Specifically, it should throw
     * an ArrayStoreException if the runtime type of the specified array is not
     * a supertype of the runtime type of every element in this set.
     */
    @Test(expected = ArrayStoreException.class)
    public void toArray_whenGivenArrayHasIncompatibleType_shouldThrowArrayStoreException() {
        // Arrange: Create a set and add a plain Object to it.
        HashCodeAndEqualsSafeSet set = new HashCodeAndEqualsSafeSet();
        set.add(new Object());

        // Arrange: Create an array of a specific, incompatible type.
        // The set contains an `Object`, which cannot be cast to `HashCodeAndEqualsMockWrapper`.
        HashCodeAndEqualsMockWrapper[] incompatibleArray = new HashCodeAndEqualsMockWrapper[1];

        // Act & Assert: Attempting to copy the set's contents into the incompatible
        // array should throw an ArrayStoreException. The assertion is handled by the
        // @Test(expected=...) annotation.
        set.toArray(incompatibleArray);
    }
}