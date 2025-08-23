package org.mockito.internal.util.collections;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link HashCodeAndEqualsSafeSet}.
 */
public class HashCodeAndEqualsSafeSetTest {

    /**
     * Verifies that the toString() method on an empty set returns "[]",
     * which is consistent with the behavior of other Set implementations like HashSet.
     */
    @Test
    public void toString_shouldReturnEmptyBrackets_whenSetIsEmpty() {
        // Arrange
        HashCodeAndEqualsSafeSet emptySet = new HashCodeAndEqualsSafeSet();
        String expectedRepresentation = "[]";

        // Act
        String actualRepresentation = emptySet.toString();

        // Assert
        assertEquals("The string representation of an empty set should be '[]'.",
                     expectedRepresentation,
                     actualRepresentation);
    }
}