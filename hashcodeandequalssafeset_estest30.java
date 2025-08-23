package org.mockito.internal.util.collections;

import org.junit.Test;
import static org.junit.Assert.assertFalse;

/**
 * Tests for {@link HashCodeAndEqualsSafeSet}.
 */
public class HashCodeAndEqualsSafeSetTest {

    @Test
    public void equals_shouldReturnFalse_whenComparedWithNull() {
        // Arrange
        HashCodeAndEqualsSafeSet set = new HashCodeAndEqualsSafeSet();

        // Act
        boolean result = set.equals(null);

        // Assert
        assertFalse("A set instance should never be equal to null.", result);
    }
}