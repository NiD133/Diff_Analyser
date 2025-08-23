package org.apache.commons.cli;

import org.junit.Test;
import static org.junit.Assert.assertNull;

/**
 * Tests for {@link PatternOptionBuilder}.
 */
public class PatternOptionBuilderTest {

    /**
     * Tests that getValueType() returns null when provided with a character
     * that is not a recognized value code (e.g., '@', ':', '%', etc.).
     */
    @Test
    public void getValueTypeShouldReturnNullForUnrecognizedCharacter() {
        // Arrange: Define a character that does not correspond to a known value type.
        // According to the PatternOptionBuilder documentation, valid type characters
        // include '@', '>', '+', '%', '/', and ':'. '8' is not one of them.
        final char unrecognizedCode = '8';

        // Act: Attempt to retrieve the value type for the unrecognized character.
        final Class<?> valueType = PatternOptionBuilder.getValueType(unrecognizedCode);

        // Assert: The method should return null, indicating no type is associated with the character.
        assertNull("Expected null for an unrecognized value code character", valueType);
    }
}