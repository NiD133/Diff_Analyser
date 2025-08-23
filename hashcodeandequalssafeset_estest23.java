package org.mockito.internal.util.collections;

import org.junit.Test;

/**
 * Unit tests for {@link HashCodeAndEqualsSafeSet}.
 */
public class HashCodeAndEqualsSafeSetTest {

    /**
     * This test verifies that the static factory method `of()` correctly throws a
     * NullPointerException when a null array is passed as an argument. This is the
     * expected behavior as the method cannot create a set from a null collection.
     */
    @Test(expected = NullPointerException.class)
    public void of_shouldThrowNullPointerException_whenGivenNullArray() {
        // Act & Assert
        // The following call is expected to throw a NullPointerException.
        // The assertion is handled declaratively by the @Test(expected=...) annotation.
        HashCodeAndEqualsSafeSet.of((Object[]) null);
    }
}