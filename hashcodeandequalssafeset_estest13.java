package org.mockito.internal.util.collections;

import org.junit.Test;
import static org.junit.Assert.assertFalse;

/**
 * Unit tests for {@link HashCodeAndEqualsSafeSet}.
 */
public class HashCodeAndEqualsSafeSetTest {

    @Test
    public void containsAll_shouldReturnFalse_whenSetIsEmptyAndOtherCollectionIsNotEmpty() {
        // Arrange
        // Create an empty set using the factory method.
        HashCodeAndEqualsSafeSet emptySet = HashCodeAndEqualsSafeSet.of((Iterable<Object>) null);
        
        // Create a non-empty set containing a single null element to check against.
        HashCodeAndEqualsSafeSet setWithNull = HashCodeAndEqualsSafeSet.of((Object) null);

        // Act
        // Check if the empty set contains all elements of the non-empty set.
        boolean result = emptySet.containsAll(setWithNull);

        // Assert
        assertFalse("An empty set cannot contain the elements of a non-empty set.", result);
    }
}