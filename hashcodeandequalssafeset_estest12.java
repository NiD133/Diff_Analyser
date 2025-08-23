package org.mockito.internal.util.collections;

import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.assertTrue;

/**
 * Tests for {@link HashCodeAndEqualsSafeSet}.
 */
public class HashCodeAndEqualsSafeSetTest {

    @Test
    public void containsAll_shouldReturnTrue_whenCheckingAgainstAnEmptyCollection() {
        // Arrange
        // Per the Collection#containsAll contract, any collection is considered to
        // contain all the elements of an empty collection.
        HashCodeAndEqualsSafeSet emptySet = new HashCodeAndEqualsSafeSet();

        // Act & Assert
        assertTrue(
            "containsAll should return true when the provided collection is empty",
            emptySet.containsAll(Collections.emptyList())
        );
    }
}