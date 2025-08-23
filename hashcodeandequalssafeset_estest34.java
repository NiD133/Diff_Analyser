package org.mockito.internal.util.collections;

import org.junit.Test;
import static org.junit.Assert.assertFalse;

/**
 * Test suite for {@link HashCodeAndEqualsSafeSet}.
 */
public class HashCodeAndEqualsSafeSetTest {

    @Test
    public void shouldReturnFalseWhenRemovingNonExistentElement() {
        // Arrange
        HashCodeAndEqualsSafeSet safeSet = new HashCodeAndEqualsSafeSet();
        Object nonExistentElement = new Object();

        // Act
        boolean wasRemoved = safeSet.remove(nonExistentElement);

        // Assert
        assertFalse("Removing an element that is not in the set should return false.", wasRemoved);
    }
}