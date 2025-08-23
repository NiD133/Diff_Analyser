package org.mockito.internal.util.collections;

import org.junit.Test;

/**
 * Tests for {@link HashCodeAndEqualsSafeSet}.
 */
public class HashCodeAndEqualsSafeSetTest {

    /**
     * This test verifies that the removeAll() method correctly handles null input
     * by throwing an IllegalArgumentException, ensuring robust behavior against invalid arguments.
     */
    @Test(expected = IllegalArgumentException.class)
    public void removeAll_shouldThrowIllegalArgumentException_whenCollectionIsNull() {
        // Arrange: Create an instance of the set to be tested.
        HashCodeAndEqualsSafeSet safeSet = new HashCodeAndEqualsSafeSet();

        // Act: Attempt to remove a null collection from the set.
        safeSet.removeAll(null);

        // Assert: The test will pass only if an IllegalArgumentException is thrown,
        // as specified by the @Test(expected = ...) annotation.
    }
}