package org.mockito.internal.util.collections;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Tests for {@link HashCodeAndEqualsSafeSet}.
 */
public class HashCodeAndEqualsSafeSetTest {

    /**
     * Verifies that the of() factory method creates an empty set
     * when it is called with an empty array.
     */
    @Test
    public void shouldCreateEmptySetFromEmptyArray() {
        // Arrange
        Object[] emptyArray = new Object[0];

        // Act
        HashCodeAndEqualsSafeSet safeSet = HashCodeAndEqualsSafeSet.of(emptyArray);

        // Assert
        assertTrue("The set should be empty", safeSet.isEmpty());
        assertEquals("The set size should be 0", 0, safeSet.size());
    }
}