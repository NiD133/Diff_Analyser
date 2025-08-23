package org.mockito.internal.util.collections;

import org.junit.Test;

import java.util.Collection;

/**
 * Unit tests for {@link HashCodeAndEqualsSafeSet}.
 */
public class HashCodeAndEqualsSafeSetTest {

    /**
     * This test verifies that the containsAll() method correctly throws
     * an IllegalArgumentException when a null collection is passed as an argument,
     * enforcing its non-null contract.
     */
    @Test(expected = IllegalArgumentException.class)
    public void containsAll_shouldThrowIllegalArgumentException_whenCollectionIsNull() {
        // Given
        HashCodeAndEqualsSafeSet safeSet = new HashCodeAndEqualsSafeSet();
        Collection<?> nullCollection = null;

        // When
        safeSet.containsAll(nullCollection);

        // Then: An IllegalArgumentException is expected, as declared by the @Test annotation.
    }
}