package com.fasterxml.jackson.core.io;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link SerializedString} class.
 */
public class SerializedStringTest {

    /**
     * Tests that the hashCode of a SerializedString is identical to the
     * hashCode of its underlying String value. This ensures consistency
     * and correct behavior when used in hash-based collections like HashMap or HashSet.
     */
    @Test
    public void hashCodeShouldBeConsistentWithUnderlyingString() {
        // Arrange
        String originalValue = ">l2[BA";
        SerializedString serializedString = new SerializedString(originalValue);

        // Act
        int expectedHashCode = originalValue.hashCode();
        int actualHashCode = serializedString.hashCode();

        // Assert
        assertEquals("The hash code of SerializedString should match the hash code of the original String.",
                expectedHashCode, actualHashCode);
    }
}